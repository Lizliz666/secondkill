package com.qf.qfseckill.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.jwt.JWTUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.qfseckill.config.RedisKey;
import com.qf.qfseckill.dao.TbUserMapper;
import com.qf.qfseckill.pojo.entity.TbUser;
import com.qf.qfseckill.pojo.req.UserReq;
import com.qf.qfseckill.pojo.resp.BaseResp;
import com.qf.qfseckill.service.UserService;
import com.qf.qfseckill.stragety.VerfiyFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    VerfiyFactory verfiyFactory;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    TbUserMapper tbUserMapper;

    @Value("${jwt.key}")
    private String key;
    /**
     *1.适配器的设计模式，
     * @param userReq
     * @return
     */
    @Override
    public BaseResp sendCode(UserReq userReq) {
        return verfiyFactory.sendCode(userReq);
    }

    /**
     * 1.判断验证码
     * 2.判断用户名是否重复
     * 3.增加进入数据库，注意密码需要进行加密处理
     * @param userReq
     * @return
     */
    @Override
    public synchronized BaseResp register(UserReq userReq) {

        String s = stringRedisTemplate.opsForValue().get(RedisKey.VERFIY_CODE + userReq.getEmail());
        String num = stringRedisTemplate.opsForValue().get(RedisKey.VERFIY_NUM + userReq.getEmail());
        if (StringUtils.isEmpty(s)&&StringUtils.isEmpty(num)){
            return new BaseResp().FAIL("请先发送验证码！");
        }
        if (StringUtils.isEmpty(s)&&!StringUtils.isEmpty(num)){
            return new BaseResp().FAIL("验证码失效!");
        }
        if (!s.equals(userReq.getCode())){
            return new BaseResp().FAIL("验证码输入错误！");
        }
        //判断邮箱/手机号是否唯一
        QueryWrapper<TbUser> tbUserQueryWrapper = new QueryWrapper<>();
        tbUserQueryWrapper.eq("email",userReq.getEmail());
        TbUser tbUser = tbUserMapper.selectOne(tbUserQueryWrapper);
        if (tbUser!=null){
            return new BaseResp().FAIL("邮箱被占用！");
        }
        tbUser=new TbUser();
        //入库处理
        //bean对象的属性复制操作 1.来源对象，2.目标对象。注意复制时，属性名应保持一致
        BeanUtils.copyProperties(userReq,tbUser);
        //对前端输入的密码进行加密操作
        String pass = DigestUtil.md5Hex(userReq.getPassword());
        tbUser.setPassword(pass);
        tbUserMapper.insert(tbUser);
        return new BaseResp().OK(null,null);
    }


    /**
     * 1.判断用户的登录次数，如果密码比较三次失败，封禁30分钟。
     * 2.登录成功后，生成jwt json串。
     * @param userReq
     * @return
     */
    @Override
    public BaseResp login(UserReq userReq) {
        String email = userReq.getEmail();
        QueryWrapper<TbUser> tbUserQueryWrapper = new QueryWrapper<>();
        tbUserQueryWrapper.eq("email",userReq.getEmail());
        TbUser tbUser = tbUserMapper.selectOne(tbUserQueryWrapper);
        if (tbUser==null){
            return new BaseResp().FAIL("请先注册！");
        }
        String pass = DigestUtil.md5Hex(userReq.getPassword());
        String s = stringRedisTemplate.opsForValue().get(RedisKey.LOGIN_NUM + userReq.getEmail());
        if (!StringUtils.isEmpty(s)&&Integer.valueOf(s)>=3) {
            stringRedisTemplate.expire(RedisKey.LOGIN_NUM + userReq.getEmail(), 30, TimeUnit.MINUTES);
            return new BaseResp().FAIL("输入密码错误次数过多，封禁30分钟");
        }
        if (!tbUser.getPassword().equals(pass)){
            if (StringUtils.isEmpty(s)){
                stringRedisTemplate.opsForValue().set(RedisKey.LOGIN_NUM + userReq.getEmail(),"1",15,TimeUnit.MINUTES);
            }
            if (Integer.valueOf(s)<3){
                stringRedisTemplate.opsForValue().increment(RedisKey.LOGIN_NUM + userReq.getEmail());
            }
            return new BaseResp().FAIL("密码输入有误！");
        }


        //如果通过，则代表用户已经验证通过，进行jwt串的生成。
        //header+payload+sign
        Map<String, Object> map = new HashMap<String, Object>() ;
        map.put("uid", tbUser.getId());
        map.put("expire_time", System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 15);
        //私钥
        String token = JWTUtil.createToken(map, key.getBytes());
        return new BaseResp().OK(token,null);
    }
}
