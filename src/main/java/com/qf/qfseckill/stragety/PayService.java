package com.qf.qfseckill.stragety;

import com.qf.qfseckill.pojo.resp.BaseResp;

/**
 * @author 严玉恒Liz
 * @date 2023/7/28 20:20
 */
public interface PayService {
    BaseResp pay(Long transactionId,Double money,String goodsName,String userId);


}
