package com.qf.qfseckill.schedule;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.qfseckill.config.RedisKey;
import com.qf.qfseckill.dao.TbSeckillGoodsMapper;
import com.qf.qfseckill.pojo.entity.TbSeckillGoods;
import com.qf.qfseckill.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author 严玉恒Liz
 * @date 2023/7/27 17:34
 *//*yyh*/
    @Component
public class InitSeckillGoods {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Resource
    TbSeckillGoodsMapper tbSeckillGoodsMapper;
/**
 * 此处的cron表达式可以通过 在线的cron表达式生成器来实现
 */
    @Scheduled(cron = "0/10 * * * * ?")
    public void initSeckillGoods(){
        {
            //设置一把分布式锁
            /**
             * 1.设置一把锁对象
             * 2.执行完成后将锁进行释放
             */
            Boolean aBoolean = stringRedisTemplate.opsForValue().setIfAbsent("init-seckill", "lock");
            if (aBoolean) {
                //1.初始化秒杀商品
                //1.1 获取到当前时间所属的时间段 以及 后续的5个时间
                List<Date> dateMenus = DateUtil.getDateMenus();
                for (Date date:dateMenus
                ) {
                    String key = DateUtil.date2Str(date);
                    //1.当前时间段的起始时间
                    String startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                    Date end = DateUtil.addDateHour(date, 2);
                    String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(end);
                    //2.设置商品的状态，进行查询
                    /**
                     * 1.获取到到的起始时间与数据库的起始时间进行比对。大于等于
                     * 2.获取到的结束时间与数据库的结束时间进行比对。小于等于
                     * 3.剩余库存量必须大于0
                     * 4.审核状态必须为 1 通过状态
                     */
                    QueryWrapper<TbSeckillGoods> tbSeckillGoodsQueryWrapper = new QueryWrapper<>();
                    tbSeckillGoodsQueryWrapper.eq("status",1)
                            .ge("stock_count",0)
                            .ge("start_time",startTime)
                            .le("end_time",endTime);
                    //排除掉已经存在redis中的商品
                    Set keys = redisTemplate.boundHashOps(RedisKey.SECKILL_GOODS + key).keys();
                    //使用notin （已存在的商品id，。。，。。，。。）
                    if (keys!=null&&keys.size()>0){
                        tbSeckillGoodsQueryWrapper.notIn("goods_id",keys);
                    }

                    List<TbSeckillGoods> tbSeckillGoods = tbSeckillGoodsMapper.selectList(tbSeckillGoodsQueryWrapper);
                    //查询出符合条件的商品列表
                    //存储商品进入redis中 使用那种结构? key是什么？当前秒杀段的开始时间
                    //获取到开始时间的key
                    for (TbSeckillGoods goods:tbSeckillGoods
                    ) {

                        //存储秒杀商品
                        redisTemplate.boundHashOps(RedisKey.SECKILL_GOODS+key).put(goods.getGoodsId().toString(),goods);
                        //存储数量
                        stringRedisTemplate.opsForValue().set(RedisKey.SECKILL_NUM+key+":"+goods.getGoodsId(),goods.getStockCount().toString());
                    }
                }
                stringRedisTemplate.delete("init-seckill");
            }
        }
    }


}
