package com.qf.qfseckill.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.jwt.JWTUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.qfseckill.config.RedisKey;
import com.qf.qfseckill.pojo.entity.TbUser;

import com.qf.qfseckill.dao.TbUserMapper;
import com.qf.qfseckill.pojo.req.UserReq;
import com.qf.qfseckill.pojo.resp.BaseResp;
import com.qf.qfseckill.stragety.VerfiyFactory;
import com.qf.qfseckill.service.UserService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 严玉恒Liz
 * @date 2023/7/25 17:48
 *//*yyh*/
    @Service
public class UserServiceImpl  implements UserService {
@Autowired
VerfiyFactory verfiyFactory;
@Autowired
    StringRedisTemplate stringRedisTemplate;

@Autowired
TbUserMapper tbUserMapper;

    @Value("${jwt.key}")
    private String key;

    @Override
    public BaseResp sendCode(UserReq userReq) {

        return verfiyFactory.sendCode(userReq);
    }

    /**判断用户的 登录状态,如果输入密码大于三次,封禁三十分钟
     *登陆成功之后,生成jwt的json串
     *
     * @param userReq
     * @return
     */
    @Override
    public BaseResp login(UserReq userReq) {
        String email = userReq.getEmail();
        QueryWrapper<TbUser> tbUserQueryWrapper = new QueryWrapper<>();
        tbUserQueryWrapper.eq("email", userReq.getEmail());
        TbUser tbUser = tbUserMapper.selectOne(tbUserQueryWrapper);
        if (tbUser == null) {
            return  new BaseResp().FAIL("还没有注册,请先注册");
        }
        String pass = DigestUtil.md5Hex(userReq.getPassword());
        String s = stringRedisTemplate.opsForValue().get(RedisKey.LOGIN_NUM+ userReq.getEmail());
        if (!tbUser.getPassword().equals(pass)){
            if (StringUtils.isEmpty(s)){
                stringRedisTemplate.opsForValue().set(RedisKey.LOGIN_NUM + userReq.getEmail(),"1",15, TimeUnit.MINUTES);
            }
            if (Integer.valueOf(s)<3){
                stringRedisTemplate.opsForValue().increment(RedisKey.LOGIN_NUM + userReq.getEmail());
            }
            return new BaseResp().FAIL("密码输入有误！");
        }
        if(!StringUtils.isEmpty(s)&&Integer.valueOf(s)>=3){
            //此处的含义为 设置 封禁时间为30min
            stringRedisTemplate.expire(RedisKey.LOGIN_NUM + userReq.getEmail(),30,TimeUnit.MINUTES);
            return  new BaseResp().FAIL("密码错误次数过多,请30分钟后重试");
        }
        //如果通过，则代表用户已经验证通过，进行jwt串的生成。
        //header+payload+sign
        Map<String ,Object> map = new HashMap<String,Object>();
                map.put("uid",tbUser.getId());
                map.put("expire_time",System.currentTimeMillis()+1000*60*60*24*15);
                //私钥
        String token = JWTUtil.createToken(map, key.getBytes());
        return  new BaseResp().OK(token,null);
    }

    /**
     * 注册的相关业务实现
     * @param userReq
     * @return
     */
    @Override
    public BaseResp register(UserReq userReq) {

        String s= stringRedisTemplate.opsForValue().get(RedisKey.VERFIY_CODE + userReq.getEmail());
        String num = stringRedisTemplate.opsForValue().get(RedisKey.VERFIY_NUM + userReq.getEmail());
        if (StringUtils.isEmpty(s)&&StringUtils.isEmpty(num)){

            return  new BaseResp().FAIL("请先发送验证码");
        }
        if(StringUtils.isEmpty(s)&&StringUtils.isEmpty(num)){
            return  new BaseResp().FAIL("验证码已经失效");
        }
        if(!s.equals(userReq.getCode())){
            return  new BaseResp().FAIL("验证码输入有误,请重新输入");
        }
        //以上执行完之后,验证码已经输入没有问题,可以使用
        QueryWrapper<TbUser> tbUserQueryWrapper= new QueryWrapper<>();
        tbUserQueryWrapper.eq("email",userReq.getEmail());
        TbUser tbUser = tbUserMapper.selectOne(tbUserQueryWrapper);
        if (tbUser!=null){
            return new BaseResp().FAIL("邮箱被占用");

        }
        tbUser= new TbUser();
        //此处已经执行完毕,可以进行注册,执行入库操作
        //bean对象的属性复制操作 1.来源对象，2.目标对象。注意复制时，属性名应保持一致
        BeanUtils.copyProperties(userReq,tbUser);
        //对前端输入的密码进行加密操作
        String pass= DigestUtil.md5Hex(userReq.getPassword());
        tbUser.setPassword(pass);
        tbUserMapper.insert(tbUser);
        return  new BaseResp().OK(null,null);



    }
}
