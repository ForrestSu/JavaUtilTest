<!-- 
  date: 2018-10-30 19:50
  author: sunquan
  mail: sunquana@gmail.com
-->

<h2> <center> Zookeeper+Kafka集群搭建方案 </center> </h2>
[TOC] 
###  说明
搭建zookeeper和Kafka集群： 
本实验拥有3个节点，均为CentOS 7系统，且均有相同用户名 （本实验为 opadm）

 
主机规划：  
192.168.0.151 　Kafka+Zookeeper  
192.168.0.152 　Kafka+Zookeeper  
192.168.0.153　 Kafka+Zookeeper  

软件下载地址：
```sh
wget http://mirrors.hust.edu.cn/apache/zookeeper/zookeeper-3.4.10/zookeeper-3.4.10.tar.gz
wget http://mirror.bit.edu.cn/apache/kafka/1.1.0/kafka_2.12-1.1.0.tgz
```

三台主机hosts文件一致：
```sh
# cat /etc/hosts
192.168.0.152 master
192.168.0.151 worker1
192.168.0.153 worker2
```

### 一、 安装zookeeper
(1) 下载 `zookeeper-3.4.10.tar.gz`

(2) 在 master 节点上安装 zookeeper  
    解压安装包，并复制配置文件
``` 
# 在master节点上
[opadm@master ~]$ tar -xzvf zookeeper-3.4.10.tar.gz
[opadm@master ~]$ cd zookeeper-3.4.10/conf/
[opadm@master conf]$ cp zoo_sample.cfg zoo.cfg
```

(3) 修改 zoo.cfg
>[opadm@master conf]$ vi zoo.cfg

修改内容如下： 
```
...
# dataDir 最好不要在 /tmp 下
dataDir=/tmp/zookeeper
...
# the port at which the clients will connect
clientPort=2181

# 添加节点，注：端口 8001、8002 不固定
server.0=master:8001:8002
server.1=worker1:8001:8002
server.2=worker2:8001:8002
...
```

(4) 在 dataDir 目录下创建 data 文件夹 和 myid 文件（内容为0）
```
# 在master节点上
[opadm@master ~]$ mkdir /tmp/zookeeper/data/
[opadm@master ~]$ touch /tmp/zookeeper/myid
[opadm@master ~]$ echo 0 > /tmp/zookeeper/myid
```

(5) 将 zookeeper-3.4.10 文件夹复制到另外两个节点下
```
# 在master节点上
[opadm@master ~]$ scp -r zookeeper-3.4.10/ worker1:~/
[opadm@master ~]$ scp -r zookeeper-3.4.10/ worker2:~/
```

并在相同 dataDir 下创建 data 文件夹 和 myid 文件 
注：worker1 的 myid 文件内容为 1，worker2 的 myid 文件内容为 2
```
# 登录 worker1
[opadm@worker1 ~]$ mkdir /tmp/zookeeper/data/
[opadm@worker1 ~]$ touch /tmp/zookeeper/myid
[opadm@worker1 ~]$ echo 1 > /tmp/zookeeper/myid
```

```
# 登录 worker2
[opadm@worker2 ~]$ mkdir /tmp/zookeeper/data/
[opadm@worker2 ~]$ touch /tmp/zookeeper/myid
[opadm@worker2 ~]$ echo 2 > /tmp/zookeeper/myid
```

### 二、 安装Kafka

(1) 下载 kafka_2.11-0.10.0.1.tar.gz 


(2) 在 master 节点上安装 kafka（一个broker） 
解压安装包
```
# 在master节点上
> [opadm@master ~]$ tar -xzvf kafka_2.11-0.10.0.1.tar.gz
```


(3) 修改 server.properties
```
# 在master节点上
[opadm@master ~]$ cd kafka_2.11-0.10.0.1/config/
[opadm@master config]$ vi server.properties
```

修改内容如下：
```
...
# master为0
broker.id=0    
...
# 连接
zookeeper.connect=master:2181,worker1:2181,worker2:2181
# 可删除topic
delete.topic.enable=true
...
```

(4) 将 kafka_2.11-0.10.0.1 文件夹复制到另外两个节点下

```
# 在master节点上
[opadm@master ~]$ scp -r kafka_2.11-0.10.0.1/ worker1:~/
[opadm@master ~]$ scp -r kafka_2.11-0.10.0.1/ worker2:~/
```
并修改每个节点对应的 server.properties 文件的 broker.id： master为0，worker1为1，worker2为2 


### 三、 启动集群和测试
** 注：启动时：先启动 zookeeper，后启动 kafka；关闭时：先关闭 kafka，后关闭zookeeper **

(1) 分别在每个节点上启动 zookeeper

```
# 在master节点上
[opadm@master zookeeper-3.4.10]$ bin/zkServer.sh start

# 在worker1节点上
[opadm@worker1 zookeeper-3.4.10]$ bin/zkServer.sh start

# 在worker2节点上
[opadm@worker2 zookeeper-3.4.10]$ bin/zkServer.sh start
```

(2) 验证 zookeeper 集群

```
# 在master节点上
[opadm@master zookeeper-3.4.10]$ bin/zkServer.sh status

# 在worker1节点上
[opadm@worker1 zookeeper-3.4.10]$ bin/zkServer.sh status

# 在worker2节点上
[opadm@worker2 zookeeper-3.4.10]$ bin/zkServer.sh status
```

显示结果为：有一个是 leader，剩下的都是 follower 

(3) 启动 Kafaka 集群
```
# 在master节点上
[opadm@master kafka_2.11-0.10.0.1]$ bin/kafka-server-start.sh config/server.properties &

# 在worker1节点上
[opadm@worker1 kafka_2.11-0.10.0.1]$ bin/kafka-server-start.sh config/server.properties &

# 在worker2节点上
[opadm@worker2 kafka_2.11-0.10.0.1]$ bin/kafka-server-start.sh config/server.properties &
```

(4) 测试 
创建 topic 和 显示 topic 信息
```
# 在master节点上 创建topic
[opadm@master kafka_2.11-0.10.0.1]$ bin/kafka-topics.sh --create --zookeeper master:2181,worker1:2181,worker2:2181 --replication-factor 3 --partitions 3 --topic test
```

```
# 在master节点上 显示topic信息
[opadm@master kafka_2.11-0.10.0.1]$ bin/kafka-topics.sh --describe --zookeeper master:2181,worker1:2181,worker2:2181 --topic test
```

```
# 在master节点上 列出topic
[opadm@master kafka_2.11-0.10.0.1]$ bin/kafka-topics.sh --list --zookeeper master:2181,worker1:2181,worker2:2181
```


创建 producer
```
# 在master节点上 测试生产消息
[opadm@master kafka_2.11-0.10.0.1]$ bin/kafka-console-producer.sh --broker-list master:9092 -topic test
```

创建 consumer
```
# 在master节点上 测试消费
[opadm@master kafka_2.11-0.10.0.1]$ bin/kafka-console-consumer.sh --bootstrap-server master:9092 -topic test --from-beginning

# 在worker1节点上 测试消费
[opadm@worker1 kafka_2.11-0.10.0.1]$ bin/kafka-console-consumer.sh --bootstrap-server worker1:9092 -topic test --from-beginning

# 在worker2节点上 测试消费
[opadm@worker2 kafka_2.11-0.10.0.1]$ bin/kafka-console-consumer.sh --bootstrap-server worker2:9092 -topic test --from-beginning

```

然后在 producer 里输入消息，consumer 中就会显示出同样的内容，表示消费成功 
(5) 删除 topic 和关闭服务
```
# 在master节点上 删除topic
[opadm@master kafka_2.11-0.10.0.1]$ bin/kafka-topics.sh --delete --zookeeper master:2181,worker1:2181,worker2:2181 --topic test
```


关闭kafka 和 zookeeper
```
# 在master节点上 关闭kafka
[opadm@master ~]$ ./kafka_2.11-0.10.0.1/bin/kafka-server-stop.sh
# 在worker1节点上 关闭kafka
[opadm@worker1 ~]$ ./kafka_2.11-0.10.0.1/bin/kafka-server-stop.sh
# 在worker2节点上 关闭kafka
[opadm@worker2 ~]$ ./kafka_2.11-0.10.0.1/bin/kafka-server-stop.sh

# 在master节点上 关闭zookeeper
[opadm@master ~]$ ./zookeeper-3.4.10/bin/zkServer.sh stop
# 在worker1节点上 关闭zookeeper
[opadm@worker1 ~]$ ./zookeeper-3.4.10/bin/zkServer.sh stop
# 在worker2节点上 关闭zookeeper
[opadm@worker2 ~]$ ./zookeeper-3.4.10/bin/zkServer.sh stop
```