package com.qf.qfseckill.service;


import com.qf.qfseckill.pojo.req.SeckillReq;
import com.qf.qfseckill.pojo.resp.BaseResp;

import javax.servlet.http.HttpServletRequest;

public interface SeckillService {
    BaseResp findSeckill(SeckillReq req);

    BaseResp seckill(SeckillReq req, HttpServletRequest request);
}
