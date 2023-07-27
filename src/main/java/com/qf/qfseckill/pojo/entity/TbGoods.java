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
 * @author 严玉恒Liz
 * @date 2023/7/26 17:42
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_goods")
public class TbGoods implements Serializable {
    private static final long serialVersionUID = -75121227502798879L;
    @TableId(value = "goods_id",type = IdType.AUTO)

    private Integer goodsId;
    @TableField("goods_name")
    
    private String goodsName;
    @TableField("goods_image")


    private String goodsImage;
    @TableField("goods_desc")


    private String goodsDesc;
    @TableField("goods_price")

    private Double goodsPrice;
    @TableField("goods_num")


    private Integer goodsNum;
    @TableField("create_time")


    private Date createTime;
    @TableField("update_time")


    private Date updateTime;

    private String path;

    @TableField(exist = false)
    private Integer cartNum;




}

