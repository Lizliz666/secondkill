package com.qf.qfseckill.stragety;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.qfseckill.dao.TbSeckillGoodsMapper;
import com.qf.qfseckill.dao.TbSeckillOrderMapper;
import com.qf.qfseckill.pojo.entity.TbSeckillOrder;
import com.qf.qfseckill.pojo.req.PayReq;
import com.qf.qfseckill.pojo.resp.BaseResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 严玉恒Liz
 * @date 2023/7/28 20:19
 *//*yyh*/
    @Component
public class PayFactory {
    @Resource(name = "alipay")
    PayService alipayServiceIml;
    @Resource (name = "wechat")
    PayService wechatPayServiceImpl;

    @Autowired
    TbSeckillOrderMapper tbSeckillOrderMapper;

    public  static  final Map map = new HashMap<>();

    @PostConstruct
    public  void  initPayType(){
        map.put("alipay",alipayServiceIml);
        map.put("wechat",wechatPayServiceImpl);

    }

    /**
     * payreq 流水号 和 支付方式
     *  private  String   transaction;
     *     private  String  payType;
     * @param payReq
     * @return
     */
    public BaseResp pay(PayReq payReq){
        //通过不同的支付方式获取到对应的实现类
        Object o = map.get(payReq.getPayType());
        if(o==null){
            return  new BaseResp().FAIL("暂不支持该种支付方式");
        }
        PayService payService = (PayService) o;
        //通过流水号查询唯一的订单信息
        QueryWrapper<TbSeckillOrder> tbSeckillOrderQueryWrapper = new QueryWrapper<>();
        tbSeckillOrderQueryWrapper.eq("transaction_id",payReq.getTransactionId());
        TbSeckillOrder tbSeckillOrder = tbSeckillOrderMapper.selectOne(tbSeckillOrderQueryWrapper);
        if(tbSeckillOrder==null){
            return  new BaseResp().FAIL("订单信息为空,ID或者类型为空");

        }
        //修改字符的方式
        tbSeckillOrder.setPayType(payReq.getPayType());
        tbSeckillOrderMapper.updateById(tbSeckillOrder);
        String transactionId = tbSeckillOrder.getTransactionId();
        tbSeckillOrder.setCreateTime(new Date());
        tbSeckillOrder.setPayTime(new Date());

        BaseResp pay = payService.pay(Long.valueOf(transactionId), tbSeckillOrder.getMoney(), "秒杀商品", tbSeckillOrder.getUserId());
        //将该笔订单存储到数据库中,标注成未支付状态
        return  pay;
    }
}
