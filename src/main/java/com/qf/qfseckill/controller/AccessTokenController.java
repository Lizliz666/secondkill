package com.qf.qfseckill.controller;

import com.qf.qfseckill.pojo.resp.BaseResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class AccessTokenController {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @RequestMapping("/getToken")
    public BaseResp getToken(){
        //1.随机生成一个token串
        String s = UUID.randomUUID().toString();
        stringRedisTemplate.opsForValue().set(s,s);
        return new BaseResp().OK(s,null);
    }
}
