package com.qf.qfseckill.controller;

import com.qf.qfseckill.pojo.req.PayReq;
import com.qf.qfseckill.pojo.resp.BaseResp;
import com.qf.qfseckill.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 严玉恒Liz
 * @date 2023/7/29 10:49
 *//*yyh*/
    @RestController
public class PayController {
        @Autowired
    PayService payService;

        @RequestMapping("/pay")
    public BaseResp pay(@RequestBody PayReq payReq, HttpServletRequest request){

            return  payService.pay(payReq,request);

        }

        @RequestMapping("/notifyUrl")
    public void notifyUrl(HttpServletRequest request){
            payService.notifyUrl(request);
        }
}
