package com.qf.qfseckill.stragety.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.qf.qfseckill.dao.TbUserMapper;
import com.qf.qfseckill.pojo.entity.TbUser;
import com.qf.qfseckill.pojo.req.UserReq;
import com.qf.qfseckill.pojo.resp.BaseResp;
import com.qf.qfseckill.stragety.VerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service("email")
public class EmailServiceImpl implements VerifyService {

    @Autowired
    TbUserMapper tbUserMapper;

    @Autowired
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;




//    @Override
//    public BaseResp verify(UserReq userReq) {
//        QueryWrapper<TbUser> tbUserQueryWrapper = new QueryWrapper<>();
//        tbUserQueryWrapper.eq("email",userReq.getEmail());
//        TbUser tbUser = tbUserMapper.selectOne(tbUserQueryWrapper);
//        if (tbUser!=null){
//            return new BaseResp().FAIL("邮箱已被占用！");
//        }
//        //发送验证码逻辑
//        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//        simpleMailMessage.setFrom(from);
//        simpleMailMessage.setTo(userReq.getEmail());
//        simpleMailMessage.setSubject("验证码发送：");
//        simpleMailMessage.setText(userReq.getCode());
//        javaMailSender.send(simpleMailMessage);
//        System.out.println("发送邮箱验证");
//        return new BaseResp().OK(null,null);
//    }

    @Override
    public BaseResp verify(UserReq userReq) {
        QueryWrapper<TbUser> tbUserQueryWrapper = new QueryWrapper<>();
        tbUserQueryWrapper.eq("email",userReq.getEmail());
        TbUser tbUser = tbUserMapper.selectOne(tbUserQueryWrapper);
        if (tbUser!=null){
            return new BaseResp().FAIL("邮箱已被占用！");
        }
        //发送验证码逻辑
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(userReq.getEmail());
        simpleMailMessage.setSubject("验证码发送：");
        simpleMailMessage.setText(userReq.getCode());
        javaMailSender.send(simpleMailMessage);
        System.out.println("发送邮箱验证");
        return new BaseResp().OK(null,null);
    }
}
