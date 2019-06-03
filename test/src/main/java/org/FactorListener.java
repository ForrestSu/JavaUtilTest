package org;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class FactorListener extends AnalysisEventListener<List<String>> {

    private boolean isEof = false;
    /**
     * 保存已经解析的因子属性记录
     */
    private List<ImportFactor> data = new ArrayList<>();
    /**
     * 属性文件 Warn信息
     */
    private List<String> propsWarning = new ArrayList<>();

    @Override
    public void invoke(List<String> list, AnalysisContext analysisContext) {
        Integer rowNum = analysisContext.getCurrentRowNum() + 1;
        if (isEof) return;
        if (list.size() < 1) return;
        String factorID = StringUtils.trim(list.get(0));
        // null or ""
        if (StringUtils.isEmpty(factorID)) {
            propsWarning.add("第 " + rowNum + "行因子ID为空");
            return;
        }
        //filter first line
        if(rowNum == 1 && factorID.contains("因子ID")){
            return;
        }

        // required items
        if (list.size() < 3) {
            if (factorID.startsWith("说明")) {
                isEof = true;
            } else {
                propsWarning.add("第 " + rowNum + "行因子<" +factorID + ">缺少必填项(因子名称/因子方向)");
            }
            return;
        }

        ImportFactor factor = new ImportFactor(rowNum);
        for (int k = 0; k < list.size(); ++k) {
            String value = StringUtils.trim(list.get(k));
            switch (k) {
                case 0:
                    factor.setFactorID(value);
                    break;
                case 1:
                    factor.setFactorName(value);
                    break;
                case 2:
                    factor.setDirection(value);
                    break;
                case 3:
                    factor.setType(value);
                    break;
                case 4:
                    factor.setDescribe(value);
                    break;
                case 5:
                    factor.setUpdateFrequency(value);
                    break;
                case 6:
                    factor.setDepends(value);
                    break;
                default:
                    break;
            }
        }
        String errMsg = factor.checkRequired();
        if (errMsg.isEmpty()) {
            data.add(factor);
        } else {
            propsWarning.add(errMsg);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
       // log.info("props file load complete, total load {} records!", data.size());
        System.out.println("props file load complete, total load {} records!" + data.size());
    }

    public List<ImportFactor> GetData() {
        return data;
    }

    public List<String> GetpropsWarning(){
        return propsWarning;
    }
}