package com.qf.qfseckill.controller;

import com.qf.qfseckill.pojo.req.UserReq;
import com.qf.qfseckill.pojo.resp.BaseResp;
import com.qf.qfseckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 严玉恒Liz
 * @date 2023/7/25 20:19
 *//*yyh*/
    @RestController
    @RequestMapping("/user")
public class UserController {
        @Autowired
    UserService userService;


    @RequestMapping("/sendCode")
    public BaseResp sendCode(@RequestBody UserReq userReq){
        return userService.sendCode(userReq);
    }

    @RequestMapping("/registry")
    public BaseResp registry(@RequestBody UserReq userReq){
        return userService.register(userReq);
    }

    @RequestMapping("/login")
    public BaseResp login(@RequestBody UserReq userReq){
        return userService.login(userReq);
    }
}
