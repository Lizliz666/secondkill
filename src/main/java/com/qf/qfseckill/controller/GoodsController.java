package com.qf.qfseckill.controller;

import com.qf.qfseckill.pojo.resp.BaseResp;
import com.qf.qfseckill.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    GoodsService goodsService;

    @RequestMapping("/findAll")
    public BaseResp findAll(){
    return goodsService.findAll();
    }
}
