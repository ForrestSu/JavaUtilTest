# factor-job start 逻辑

[TOC]

## 1 获取基础财务或指标数据(MarketData)
(1) FACTOR_DATA_LOG_INFO  
  因子调度日志表中插入一条(uuid, "MarketData", "等待更新")的记录  
(2) 调用 python 脚本  
  使用 getRuntime().exec() (在python脚本目录)  
 > python Update_Market_Data.py ${uuid} 

(3) 定时检查
  每隔5min 检查一次 FACTOR_DATA_LOG_INFO   
  当前uuid 是否有 (uuid, "MarketData", "更新完成") 的记录,
  如果超过30min 返回超时
  

## 2 前期数据准备 (BeforeData)
(1)插入日志
因子调度日志表中插入一条 (uuid, "BeforeData", "等待更新") 的记录  
(2) 调用 matlab 脚本  
使用 getRuntime().exec() (在 script 脚本目录)   
> `matlab -nosplash` -nodesktop -r UpdateData_Before_UpdateFactor(${uuid})

(3) 定时检查
  每隔5min 检查一次 FACTOR_DATA_LOG_INFO    
  当前uuid 是否有 (uuid, "BeforeData", "更新完成") 的记录,  
  如果超过30min 返回超时  
  ` 使用 commonUtil.sendMessage() 推送准备成功的结果，或者超时的结果>> `



## 3 开始真正批量更新因子数据
`等待更新 -> 正在更新 -> 更新完成 -> 正在同步 -> 同步完成 `

(1) 先按照依赖层级进行分组  
  如果id isEmpty, 查询所有因子  
  否则查询指定的单个因子  

(2) 按照分组依次执行 

``` 
  将因子按照N(最大并发数)分组  
  执行一个分组：    
   a 调用 getRuntime().exec() 不等待子进程退出  
   b matlab -nosplash -nodesktop -r Main_Update_Factor('${FactorId}','${Uuid}')   
   c 这个分组全部执行完成(查询每个因子在factor_log_info 里面有 "更新完成" 的记录)  
   d 如果全部执行成功  
       for (每个因子,执行python) {  
          matlab -nosplash -nodesktop -r Update_Factor_To_Store('${FactorId}','${Uuid}')  
          等待这个因子执行成功(数据库中有 "同步完成" 的记录)  
       }
```

## 4 更新后数据处理 (AfterData)
(1)插入日志
因子调度日志表中插入一条 (uuid, "AfterData", "等待更新") 的记录  
(2) 调用 matlab 脚本
使用 getRuntime().exec() (在 script 脚本目录)
> `matlab -nosplash` -nodesktop -r UpdateData_After_UpdateFactor(${uuid})

(3) 定时检查
  每隔5min 检查一次 FACTOR_DATA_LOG_INFO   
  当前uuid 是否有 (uuid, "AfterData", "更新完成") 的记录,  
  如果超过 30min 返回超时  
  <<使用 commonUtil.sendMessage() 推送准备成功的结果，或者超时的结果>>  


# Problems

## 每个python 文件的逻辑

- 1 Update_Market_Data.py   
  入参：`(uuid)`  
  从 `start_date.txt` 文件读取开始日期 yyyy/mm/dd   
  聚源数据库 财务数据+ 日行情数据 读取到 对应的txt文件  

- 2 UpdateData_Before_UpdateFactor.py   
  入参：`(uuid)`    
  将交易日写入 `[Update]_FactorValue.mat` 文件   

- 3 Main_Update_Factor.py  
  入参：`(FactorID, uuid)`  
  将 第1, 2 步产生的数据作为输入  
  执行因子代码 Update_FactorID.m, 每个因子代码生成一个全量的因子值文件(mat)

- 4 Update_Factor_To_Store.py  
  入参：`(FactorID, uuid)`    
  将第3步 产生的多个 *.mat 追加到 `[Update]_FactorValue.mat` 文件   

- 5 UpdateData_After_UpdateFactor.py  
  入参：`(uuid)`  
  mv  [Update]_FactorValue.mat  FactorValue.mat  
  将日期写入 `start_date.txt`
   