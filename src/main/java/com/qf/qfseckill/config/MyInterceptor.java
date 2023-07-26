package com.qf.qfseckill.config;

import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWTUtil;
import com.qf.qfseckill.pojo.resp.BaseResp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@Component
public class MyInterceptor implements HandlerInterceptor {

    @Value("${jwt.key}")
    private String key;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断用户是否登录
        String token = request.getHeader("token");
        //没有token则代表未登录状态
        if (StringUtils.isEmpty(token)){
            ResponseWriter(new BaseResp().FAIL("请先登录！"),response);
            return false;
        }
        //2.有token串
        try {
            boolean verify = JWTUtil.verify(token, key.getBytes(StandardCharsets.UTF_8));
            if (!verify){
                ResponseWriter(new BaseResp().FAIL("登录信息有误！"),response);
                return false;
            }

        }catch (Exception e){
            ResponseWriter(new BaseResp().FAIL("登录信息有误！"),response);
            return false;
        }
        return true;
    }

    public void ResponseWriter(BaseResp baseResp,HttpServletResponse response){
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
