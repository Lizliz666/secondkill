package com.qf.qfseckill.service.impl;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.qf.qfseckill.config.RedisKey;
import com.qf.qfseckill.pojo.entity.TbSeckillGoods;
import com.qf.qfseckill.pojo.req.SeckillReq;
import com.qf.qfseckill.pojo.resp.BaseResp;
import com.qf.qfseckill.service.SeckillService;
import com.qf.qfseckill.utils.DateUtil;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    RedissonClient redissonClient;

    @Override
    public BaseResp findSeckill(SeckillReq req) {
        //将req中的date转为yyyyMMddHH格式。
        String key = DateUtil.date2Str(req.getDate());
        Boolean aBoolean = redisTemplate.hasKey(RedisKey.SECKILL_GOODS + key);
        if (!aBoolean){
            return new BaseResp().FAIL("当前时间段没有秒杀商品！");
        }
        List values = redisTemplate.boundHashOps(RedisKey.SECKILL_GOODS + key).values();
        return new BaseResp().OK(values,null);
    }

    /**
     * 开始秒杀
     * 1.synchronized 可以保证单机情况下的 线程安全性问题。但是当部署服务集群后，还是会出现并发情况。
     * 2.需要使用分布式解决。多台服务拿到的是同一把锁。
     * 3.当使用到分布式锁后，为了防止死锁，需要设置当前锁的失效时间。问题是:如果业务执行时间超过了锁的失效时间，会导致并发安全问题继续出现
     * 4.我们可以设置 这样一把锁，如果正常执行完成，则释放，也设置一定的过期时间，但是如果时间过期了，线程未执行完成，则需要自动对该锁进行延期操作。
     * 分布式锁的框架：redisson 分布式框架解决
     * 5.设置的锁的名称。所有的商品需要设置分段竟态锁。加上每个商品的唯一标识即可
     * @param req
     * @return
     */
    @Override
    public  BaseResp seckill(SeckillReq req, HttpServletRequest request) {
        //1.解析出用户的id
        JWT token = JWTUtil.parseToken(request.getHeader("token"));
        String uid = token.getPayload().getClaim("uid").toString();
        //2.判断当前秒杀的时间是否在秒杀时间段内。
        Date date = new Date();
        if (!req.getDate().before(date)){
            return new BaseResp().FAIL("秒杀尚未开始！");
        }
        Date endDate = DateUtil.addDateHour(req.getDate(), 2);
        if (!endDate.after(date)){
            return new BaseResp().FAIL("秒杀已结束！");
        }
        String key = DateUtil.date2Str(req.getDate());
        //判断当前商品的剩余库存量是否大于0
        //Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent(RedisKey.SEKILL_LOCK, "lock", 10, TimeUnit.MILLISECONDS);
        RLock lock = redissonClient.getLock(RedisKey.SECKILL_LOCK+req.getGoodId());
        try {
            //lock.lockInterruptibly(5,TimeUnit.SECONDS);
            //tryLock 属于非阻塞锁。如果没有获取到则直接返回false
            boolean b = lock.tryLock(3, TimeUnit.SECONDS);
            //阻塞锁
            //lock.lock();
            if (b) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String s = stringRedisTemplate.opsForValue().get(RedisKey.SECKILL_NUM + key + ":" + req.getGoodId());
                if (StringUtils.isEmpty(s)) {
                    return new BaseResp().FAIL("没有秒杀商品！");
                }
                //剩余库存量》0
                if (Integer.valueOf(s) <= 0) {
                    //没有库存
                    return new BaseResp().FAIL("当前商品已售罄");
                }
                //对当前剩余库存量-1操作
                stringRedisTemplate.opsForValue().decrement(RedisKey.SECKILL_NUM + key + ":" + req.getGoodId());
                //2.对商品的详情进行操作
                TbSeckillGoods o = (TbSeckillGoods) redisTemplate.boundHashOps(RedisKey.SECKILL_GOODS + key).get(req.getGoodId());
                o.setStockCount(o.getStockCount() - 1);
                redisTemplate.boundHashOps(RedisKey.SECKILL_GOODS + key).put(req.getGoodId(), o);
                //生成订单
                //让用户进行支付
                //将锁进行释放
                //stringRedisTemplate.delete(RedisKey.SEKILL_LOCK);
                // lock.unlock();
                return new BaseResp().OK(null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            boolean locked = lock.isLocked();
            if (locked&&lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }
       return new BaseResp().FAIL("太多请求了,请稍后重试");
    }
}
