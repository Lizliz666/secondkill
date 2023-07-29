package com.qf.qfseckill.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * (TbSeckillOrder)实体类
 *
 * @author makejava
 * @since 2023-07-28 20:01:39
 */
@Data
@TableName("tb_seckill_order")
public class TbSeckillOrder implements Serializable {
    private static final long serialVersionUID = 343110963803433095L;
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField("seckill_id")
    private Long seckillId;
    /**
     * 秒杀商品Id
     */
    @TableField("goods_id")
    private Long goodsId;
    /**
     * 支付金额
     */
    private Double money;
    /**
     * 用户
     */
    @TableField("user_id")
    private String userId;
    /**
     * 商家
     */
    @TableField("seller_id")
    private String sellerId;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 支付时间
     */
    @TableField("pay_time")
    private Date payTime;
    /**
     * 支付状态
     */

    private String status;
    /**
     * 交易流水
     */
    @TableField("transaction_id")
    private String transactionId;
    /**
     * 支付类型
     */
    @TableField("pay_type")
    private String payType;


    public Long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(Long seckillId) {
        this.seckillId = seckillId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

}

