package com.qf.qfseckill.stragety;


import com.qf.qfseckill.pojo.req.UserReq;
import com.qf.qfseckill.pojo.resp.BaseResp;

public interface VerifyService {

    public BaseResp verify(UserReq userReq);
}
