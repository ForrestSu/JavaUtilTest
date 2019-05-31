package org;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

public class ImportFactor extends BaseRowModel {

    /**
     * 因子代码
     */
    @ExcelProperty(index = 0)
    private String factorCode;

    /**
     * 因子名称
     */
    @ExcelProperty(index = 1)
    private String factorName;

    /**
     * 因子方向 后面研究怎么直接转成枚举值
     */
    @ExcelProperty(index = 2)
    private String direction;

    /**
     * 因子类型 同上
     */
    @ExcelProperty(index = 3)
    private String type;

    /**
     * 因子描叙
     */
    @ExcelProperty(index = 4)
    private String describe;

    /**
     * 更新频率
     */
    @ExcelProperty(index = 5)
    private String updateFrequency;

    /***
     * 因子依赖信息
     */
    @ExcelProperty(index = 6)
    private  String depend;
}