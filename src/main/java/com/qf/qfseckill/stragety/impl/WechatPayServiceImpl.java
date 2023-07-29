package com.qf.qfseckill.stragety.impl;

import com.qf.qfseckill.pojo.resp.BaseResp;
import com.qf.qfseckill.stragety.PayService;
import org.springframework.stereotype.Service;

/**微信支付的测试需要 商家的营业执照,无法进行测试
 *
 */
@Service("wechat")
public class WechatPayServiceImpl implements PayService {
    @Override
    public BaseResp pay(Long transcationId, Double money, String goodsName, String userId) {
        System.out.println("支付方式为微信支付");

        return null;
    }
}
