package com.qf.qfseckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.qfseckill.config.RedisKey;
import com.qf.qfseckill.pojo.entity.TbRotation;
import com.qf.qfseckill.pojo.resp.BaseResp;
import com.qf.qfseckill.service.RotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/rotation")
@RestController
public class RotationController {

    @Autowired
    RotationService rotationService;

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 将轮播图存储进入redis中。
     * @return
     */
    @RequestMapping("/findAll")
    public BaseResp findAll(){
        //1.判断redis中是否有该Key
        Boolean aBoolean = redisTemplate.hasKey(RedisKey.ROTATION_IMAGE);
        if (!aBoolean){
            //2.没有从数据库中查询，放置到redis中
            QueryWrapper<TbRotation> tbRotationQueryWrapper = new QueryWrapper<>();
            tbRotationQueryWrapper.eq("status",1);
            List<TbRotation> list = rotationService.list(tbRotationQueryWrapper);
            redisTemplate.opsForList().rightPushAll(RedisKey.ROTATION_IMAGE,list);
        }
        //status状态为1de
        List range = redisTemplate.opsForList().range(RedisKey.ROTATION_IMAGE, 0, -1);
        return new BaseResp().OK(range,null);

    }
}
