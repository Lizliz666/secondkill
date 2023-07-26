package com.qf.qfseckill.stragety.impl;


import com.qf.qfseckill.pojo.req.UserReq;
import com.qf.qfseckill.pojo.resp.BaseResp;
import com.qf.qfseckill.stragety.VerifyService;
import org.springframework.stereotype.Service;

@Service("sms")
public class SmsServiceImpl implements VerifyService {
    @Override
    public BaseResp verify(UserReq userReq) {
        System.out.println("发送短信进行验证");
        return new BaseResp().OK(null,null);
    }
}
