package com.qf.qfseckill.service.impl;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.qf.qfseckill.config.RedisKey;
import com.qf.qfseckill.dao.GoodsMapper;
import com.qf.qfseckill.pojo.entity.TbGoods;
import com.qf.qfseckill.pojo.req.ShopCartReq;
import com.qf.qfseckill.pojo.resp.BaseResp;
import com.qf.qfseckill.service.ShopCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class ShopCartServiceImpl implements ShopCartService {


    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    GoodsMapper goodsMapper;

    /**
     * 1.判断用户是否已经拥有购物车 hash结构 2层key 1.用户的标识。2.商品的唯一标识。3.商品信息
     * 2.判断该商品是否已经存在，如果存在则数量+1
     * @param shopCartReq
     * @param request
     * @return
     */
    @Override
    public BaseResp addShopCart(ShopCartReq shopCartReq, HttpServletRequest request) {
        //1.解析出用户的Id
        JWT token = JWTUtil.parseToken(request.getHeader("token"));
        String uid = token.getPayload().getClaim("uid").toString();
        //2.
        //3.判断是否添加的商品再购物车中已经存在
        Boolean aBoolean1 = redisTemplate.boundHashOps(RedisKey.SHOP_CART + uid).hasKey(shopCartReq.getGoodsId().toString());
        if (!aBoolean1){
            TbGoods goods = goodsMapper.selectById(shopCartReq.getGoodsId());
            if (goods==null){
                return new BaseResp().FAIL("商品已下架！");
            }
            goods.setCartNum(1);
            redisTemplate.boundHashOps(RedisKey.SHOP_CART + uid).put(goods.getGoodsId().toString(),goods);

        }else{
            TbGoods tbGoods = (TbGoods) redisTemplate.boundHashOps(RedisKey.SHOP_CART + uid).get(shopCartReq.getGoodsId().toString());
            tbGoods.setCartNum(tbGoods.getCartNum()+1);
            redisTemplate.boundHashOps(RedisKey.SHOP_CART + uid).put(tbGoods.getGoodsId().toString(),tbGoods);

        }
        return new BaseResp().OK(null,null);
    }

    @Override
    public BaseResp findShopCart(HttpServletRequest request) {
        //1.解析出用户的Id
        JWT token = JWTUtil.parseToken(request.getHeader("token"));
        String uid = token.getPayload().getClaim("uid").toString();
        List values = redisTemplate.boundHashOps(RedisKey.SHOP_CART + uid).values();
        return new BaseResp().OK(values,null);
    }

    /**
     * 1.解析用户id
     * 2.判断数量如果为0直接移除。如果大于0 .则将前端传入的数量，更新到redis中
     * @param shopCartReq
     * @param request
     * @return
     */
    @Override
    public BaseResp updateNum(ShopCartReq shopCartReq, HttpServletRequest request) {
        //1.解析出用户的Id
        JWT token = JWTUtil.parseToken(request.getHeader("token"));
        String uid = token.getPayload().getClaim("uid").toString();
        if (shopCartReq.getCartNum()<=0){
            redisTemplate.boundHashOps(RedisKey.SHOP_CART+uid).delete(shopCartReq.getGoodsId().toString());
        }else{
            TbGoods tbGoods = (TbGoods)redisTemplate.boundHashOps(RedisKey.SHOP_CART + uid).get(shopCartReq.getGoodsId().toString());
            if (shopCartReq.getCartNum()>0&&shopCartReq.getCartNum()<=99){
                tbGoods.setCartNum(shopCartReq.getCartNum());
                redisTemplate.boundHashOps(RedisKey.SHOP_CART + uid).put(shopCartReq.getGoodsId().toString(),tbGoods);
            }

        }
        return new BaseResp().OK(null,null);
    }
}
