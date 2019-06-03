package org;

import lombok.Data;

@Data
public class ResultWithCause {

    private  Integer code;//0:成功,1:失败,2:部分成功
    private  String errorMsg;
    private  Object object;

    public ResultWithCause(){
        this.code=0;
        this.errorMsg="";
        this.object=null;
    }

    public ResultWithCause(Object object){
        this.code=0;
        this.errorMsg="";
        this.object=object;
    }

    public ResultWithCause(Integer code, String cause){
        this.code=code;
        this.errorMsg=cause;
        this.object=null;
    }

    public ResultWithCause(Integer code, String cause, Object object){
        this.code=code;
        this.errorMsg=cause;
        this.object=object;
    }
}
