package com.qf.qfseckill.service.impl;

import com.qf.qfseckill.config.RedisKey;
import com.qf.qfseckill.dao.GoodsMapper;
import com.qf.qfseckill.pojo.entity.TbGoods;
import com.qf.qfseckill.pojo.resp.BaseResp;
import com.qf.qfseckill.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    GoodsMapper goodsMapper;

    @Override
    public BaseResp findAll() {
        Boolean aBoolean = redisTemplate.hasKey(RedisKey.GOODS_LIST);
        if (!aBoolean){
            List<TbGoods> tbGoods = goodsMapper.selectList(null);
            for (TbGoods goods:tbGoods){
                redisTemplate.opsForZSet().add(RedisKey.GOODS_LIST,goods,goods.getGoodsPrice());
            }
        }
        //从低到高进行获取
        Set set = redisTemplate.opsForZSet().reverseRange(RedisKey.GOODS_LIST, 0, -1);
        return new BaseResp().OK(set,null);
    }
}
