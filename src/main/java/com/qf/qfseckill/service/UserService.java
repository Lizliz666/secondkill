package com.qf.qfseckill.service;

import com.qf.qfseckill.pojo.req.UserReq;
import com.qf.qfseckill.pojo.resp.BaseResp;

/**
 * @author 严玉恒Liz
 * @date 2023/7/25 17:48
 */
public interface UserService {

    BaseResp sendCode (UserReq userReq);

    BaseResp login (UserReq userReq);

    BaseResp register (UserReq userReq);
}
