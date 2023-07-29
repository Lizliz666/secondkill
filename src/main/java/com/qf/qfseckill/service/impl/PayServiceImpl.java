package com.qf.qfseckill.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.qfseckill.dao.TbSeckillGoodsMapper;
import com.qf.qfseckill.dao.TbSeckillOrderMapper;
import com.qf.qfseckill.pojo.entity.TbSeckillGoods;
import com.qf.qfseckill.pojo.entity.TbSeckillOrder;
import com.qf.qfseckill.pojo.req.PayReq;
import com.qf.qfseckill.pojo.resp.BaseResp;
import com.qf.qfseckill.service.PayService;
import com.qf.qfseckill.stragety.PayFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class PayServiceImpl implements PayService {

    @Autowired
    PayFactory payFactory;

    @Autowired
    TbSeckillOrderMapper tbSeckillOrderMapper;
    @Autowired
    TbSeckillGoodsMapper tbSeckillGoodsMapper;
    @Override
    public BaseResp pay(PayReq payReq, HttpServletRequest request) {


        return  payFactory.pay(payReq);
    }

    @Override
    public void notifyUrl(HttpServletRequest request) {
        Map<String, String> stringStringMap = convertRequestParamsToMap(request);
        boolean signVerified = false; //调用SDK验证签名
        try {
            signVerified = AlipaySignature.rsaCheckV1(stringStringMap, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAk0DyD+UXz6sUJ2stg8dXmbKdN+QKcNDSPVLPlb32hLTQ+RtB8cjHlxZc9qZLVnZY6Yycwcb0WAv+yZgOvksCpkto9n+/S4OML1NBswlpByS+ILhw8lMFBBupi7ReolX4GflM3Db1fUeWagGMkGsz946Nmi4TFDiBFu5vruA+Rf5/uAUV0cartkxBrLdYdYFvH8z5ZJfr6bqJ72ZlwnvyA6TDXYlA0XHmdTjKETRJEl0x5RB6PR4EtoJIN/qOKXtsmqv47NbBdxL6SDgz1NHaz3HAkZnQPvySfFwkO08cYpCHYtawlqI7SFmGSPBcqIPhwrR9CslQVw/ekcUSUYvEtQIDAQAB", "utf-8", "RSA2");
            if(signVerified){
                // TODO 验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure
                //校验1.订单号是否存在。2.支付金额是否匹配。3.支付宝返回的结果是否未成功
                String out_trade_no = stringStringMap.get("out_trade_no");
                String total_amount = stringStringMap.get("total_amount");
                String trade_status = stringStringMap.get("trade_status");
                if (trade_status.equals("TRADE_SUCCESS")){
                    //根据流水号查询订单信息
                    QueryWrapper<TbSeckillOrder> tbSeckillOrderQueryWrapper = new QueryWrapper<>();
                    tbSeckillOrderQueryWrapper.eq("transaction_id",out_trade_no);
                    TbSeckillOrder tbSeckillOrder = tbSeckillOrderMapper.selectOne(tbSeckillOrderQueryWrapper);
                    //比较支付的金额
                    System.out.println("秒杀应付的钱:"+tbSeckillOrder.getMoney());
                    System.out.println("支付的总金额"+Double.valueOf(total_amount));
                    if (tbSeckillOrder.getMoney().compareTo(Double.valueOf(total_amount))==0){
                            //修改订单状态，减去具体的数据库的秒杀商品的库存数量
                        tbSeckillOrder.setStatus("1");
                        tbSeckillOrder.setPayTime(new Date());
                        tbSeckillOrderMapper.updateById(tbSeckillOrder);
                        //获取秒杀id
                        Long seckillId = tbSeckillOrder.getSeckillId();
                        TbSeckillGoods tbSeckillGoods = tbSeckillGoodsMapper.selectById(seckillId);
                        tbSeckillGoods.setStockCount(tbSeckillGoods.getStockCount()-1);
                        tbSeckillGoodsMapper.updateById(tbSeckillGoods);

                    }
                }

            }else{
                // TODO 验签失败则记录异常日志，并在response中返回failure.
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

    }

    private static Map<String, String> convertRequestParamsToMap(HttpServletRequest request) {
        Map<String, String> retMap = new HashMap<String, String>();

        Set<Map.Entry<String, String[]>> entrySet = request.getParameterMap().entrySet();

        for (Map.Entry<String, String[]> entry : entrySet) {
            String name = entry.getKey();
            String[] values = entry.getValue();
            int valLen = values.length;

            if (valLen == 1) {
                retMap.put(name, values[0]);
            } else if (valLen > 1) {
                StringBuilder sb = new StringBuilder();
                for (String val : values) {
                    sb.append(",").append(val);
                }
                retMap.put(name, sb.toString().substring(1));
            } else {
                retMap.put(name, "");
            }
        }

        return retMap;
    }
}
