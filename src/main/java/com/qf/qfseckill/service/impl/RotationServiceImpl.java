package com.qf.qfseckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qf.qfseckill.dao.RotationMapper;
import com.qf.qfseckill.pojo.entity.TbRotation;
import com.qf.qfseckill.service.RotationService;
import org.springframework.stereotype.Service;

@Service
public class RotationServiceImpl extends ServiceImpl<RotationMapper, TbRotation> implements RotationService {
}
