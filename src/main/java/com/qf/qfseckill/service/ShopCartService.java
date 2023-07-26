package com.qf.qfseckill.service;

import com.qf.qfseckill.pojo.req.ShopCartReq;
import com.qf.qfseckill.pojo.resp.BaseResp;

import javax.servlet.http.HttpServletRequest;

public interface ShopCartService {
    BaseResp addShopCart(ShopCartReq shopCartReq, HttpServletRequest request);

    BaseResp findShopCart(HttpServletRequest request);

    BaseResp updateNum(ShopCartReq shopCartReq, HttpServletRequest request);
}
