package com.qf.qfseckill.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * (TbRotation)实体类
 *
 * @author makejava
 * @since 2023-07-26 14:30:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_rotation")
public class TbRotation implements Serializable {
    private static final long serialVersionUID = 526884040473402746L;
    @TableId(type = IdType.AUTO)

    private Integer id;
    @TableField(value = "img_url")
    private String imgUrl;
    
    private String status;




}

