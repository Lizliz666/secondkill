package com.qf.qfseckill.pojo.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResp {

    private Integer code;
    private String msg;
    private Object data;
    private Long total;

    public BaseResp OK(Object data,Long total){
        return new BaseResp(1,"success",data,total);
    }

    public BaseResp FAIL(String msg){
        return new BaseResp(0,msg,null,null);
    }


}
