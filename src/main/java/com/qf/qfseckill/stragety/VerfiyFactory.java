package com.qf.qfseckill.stragety;

import cn.hutool.core.util.RandomUtil;

import com.qf.qfseckill.config.RedisKey;
import com.qf.qfseckill.pojo.req.UserReq;
import com.qf.qfseckill.pojo.resp.BaseResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class VerfiyFactory {

    @Resource(name = "sms")
    VerifyService SmsServiceImpl;
    @Resource(name = "email")
    VerifyService EmailServiceImpl;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    private static final Map map = new HashMap<>();

    @PostConstruct
    public void VerfiyFactory(){
        map.put("sms",SmsServiceImpl);
        map.put("email",EmailServiceImpl);
    }

    public BaseResp sendCode(UserReq userReq){
        //1.判断当前用户是第几次发送验证码
        String s = stringRedisTemplate.opsForValue().get(RedisKey.VERFIY_NUM + userReq.getEmail());
        if (!StringUtils.isEmpty(s)){
            if (Integer.valueOf(s)<3){
                stringRedisTemplate.opsForValue().increment(RedisKey.VERFIY_NUM + userReq.getEmail());
            }else{
                stringRedisTemplate.expire(RedisKey.VERFIY_NUM+userReq.getEmail(),60,TimeUnit.MINUTES);
                return new BaseResp().FAIL("发送次数过多，请稍后再试！");
            }
        }else {
            stringRedisTemplate.opsForValue().set(RedisKey.VERFIY_NUM + userReq.getEmail(),"1",5,TimeUnit.MINUTES);
        }

        Object o = map.get(userReq.getType());
        if (o==null){
           return new BaseResp().FAIL("不支持该种验证方式！");
        }

        VerifyService verifyService =   (VerifyService)o;
        //生成随机6位验证码
        String code = RandomUtil.randomString(6);
        userReq.setCode(code);
        BaseResp verify = verifyService.verify(userReq);
        if (verify.getCode()==1){
            //在此存储进入redis中
            stringRedisTemplate.opsForValue().set(RedisKey.VERFIY_CODE+userReq.getEmail(),code,300, TimeUnit.SECONDS);
            return new BaseResp().OK(null,null);
        }
        return new BaseResp().FAIL("发送失败！");
    }
}
