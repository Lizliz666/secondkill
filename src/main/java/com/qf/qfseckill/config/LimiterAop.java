package com.qf.qfseckill.config;

import cn.hutool.json.JSONUtil;
import com.google.common.util.concurrent.RateLimiter;
import com.qf.qfseckill.pojo.resp.BaseResp;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
@Aspect
public class LimiterAop {

    //每秒产生多少个令牌
    private static final RateLimiter rateLimiter = RateLimiter.create(1);

    @Autowired
    HttpServletResponse response;

    @Pointcut(value = "@annotation(com.qf.qfseckill.anno.Limiter)")
    public void pt1(){}

    /**
     * 1.前置
     * 2.后置
     * 3.异常
     * 4.最终
     * 5.环绕
     */
    @Around("pt1()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint){
        Object proceed=null;
        try {
            System.out.println("前置通知");
            //尝试获取令牌，如果获取到则返回true，否则返回false
            boolean b = rateLimiter.tryAcquire();

            if (b) {
                proceed = proceedingJoinPoint.proceed();
                System.out.println("后置通知");
                return proceed;
            }else{
                //没有获取到令牌，需要直接返回前端
                ResponseWriter(new BaseResp().FAIL("sorry!!!"),response);
            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            System.out.println("出现异常："+throwable.getMessage());
        }finally {
            System.out.println("最终通知");
        }
        return proceed;
    }


    public void ResponseWriter(BaseResp baseResp, HttpServletResponse response){
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        PrintWriter writer=null;
        try {
            writer= response.getWriter();
            writer.write(JSONUtil.toJsonStr(baseResp));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            writer.flush();
            writer.close();

        }

    }
}
