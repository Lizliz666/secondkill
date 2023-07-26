package com.qf.qfseckill.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qf.qfseckill.pojo.resp.BaseResp;

/**
 * @author 严玉恒Liz
 * @date 2023/7/26 17:44
 */
public interface GoodsService {
    BaseResp findAll();
}
