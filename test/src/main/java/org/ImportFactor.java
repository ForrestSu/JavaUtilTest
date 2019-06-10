package org;

import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.util.StringUtils;
import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode(exclude={"rowIndex"})
public class ImportFactor {

    /**
     * 行号
     */
    private Integer rowIndex;
    /**
     * 0 因子代码
     */
    private String factorID;

    /**
     * 1 因子名称
     */
    private String factorName;

    /**
     * 因子方向 后面研究怎么直接转成枚举值
     */
    private String direction;

    /**
     * 因子类型 同上
     */
    private String type;

    /**
     * 因子描叙
     */
    private String describe;

    /**
     * 更新频率
     */
    private String updateFrequency;

    /***
     * 因子依赖信息
     */
    private  String depends;

    private List<String> dependsList;

    public ImportFactor(Integer index){
        rowIndex = index;
    }
    /**
     * 检查必填字段，并补充选填字段
     * @return
     */
    public String checkRequired() {
        String result = "";
        if (StringUtils.isEmpty(factorID)) {
            result = "第" + rowIndex + "行缺少因子ID";
            return result;
        }
        if (StringUtils.isEmpty(factorName)) {
            result = "第" + rowIndex + "行缺少因子名称";
            return result;
        }
        if (StringUtils.isEmpty(direction)) {
            result = "第" + rowIndex + "行缺少因子方向";
            return result;
        }
        // 补充选填字段
        if (StringUtils.isEmpty(type)) {
            type = "其他";
        }
        if (StringUtils.isEmpty(updateFrequency)) {
            updateFrequency = "每天";
        }
        if (describe == null) {
            describe = "";
        }
        if (depends == null) {
            depends = "";
        }
        return result;
    }

}