package com.qf.qfseckill.service;

import com.qf.qfseckill.pojo.req.PayReq;
import com.qf.qfseckill.pojo.resp.BaseResp;

import javax.servlet.http.HttpServletRequest;

public interface PayService {
    BaseResp pay(PayReq payReq, HttpServletRequest request);

    void notifyUrl(HttpServletRequest request);
}
