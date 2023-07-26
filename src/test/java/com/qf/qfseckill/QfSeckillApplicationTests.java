package com.qf.qfseckill;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;


@SpringBootTest
class QfSeckillApplicationTests {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Test
    void contextLoads() {
        //1.声明连接客户端。2.连接的地址。端口号，操作的数据库
//        Jedis jedis = new Jedis("127.0.0.1", 6379);
//        String set = jedis.set("name", "zhangsan");

       // redisTemplate.opsForValue().set("age",18);
        //redisTemplate.opsForValue().increment("age");
        //stringRedisTemplate.opsForValue().set("age","18");
        String age = stringRedisTemplate.opsForValue().get("age");
        System.out.println(age);
        stringRedisTemplate.expire("age",30, TimeUnit.SECONDS);
    }

}
