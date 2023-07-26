package com.qf.qfseckill.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.io.Serializable;

/**
 * (TbGoods)实体类
 *
 * @author makejava
 * @since 2023-07-26 17:38:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_goods")
public class TbGoods implements Serializable {
    private static final long serialVersionUID = -75121227502798879L;
    @TableId(value = "goods_id",type = IdType.AUTO)

    private Integer goodsId;
    
    private String goodsName;
    
    private String goodsImage;
    
    private String goodsDesc;
    
    private Double goodsPrice;
    
    private Integer goodsNum;
    
    private Date createTime;
    
    private Date updateTime;

    private String path;

    @TableField(exist = false)
    private Integer cartNum;




}

