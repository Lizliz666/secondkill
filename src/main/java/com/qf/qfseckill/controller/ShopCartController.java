package com.qf.qfseckill.controller;

import com.qf.qfseckill.pojo.req.ShopCartReq;
import com.qf.qfseckill.pojo.resp.BaseResp;
import com.qf.qfseckill.service.ShopCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/shopcart")
public class ShopCartController {

    @Autowired
    ShopCartService shopCartService;


    @RequestMapping("/addShopCart")
    public BaseResp addShopCart(@RequestBody ShopCartReq shopCartReq, HttpServletRequest request){
        return shopCartService.addShopCart(shopCartReq,request);
    }

    @RequestMapping("/findShopCart")
    public BaseResp findShopCart(HttpServletRequest request){
        return shopCartService.findShopCart(request);
    }

    @RequestMapping("/updateNum")
    public BaseResp updateNum(@RequestBody ShopCartReq shopCartReq,HttpServletRequest request){
        return shopCartService.updateNum(shopCartReq,request);
    }
}
