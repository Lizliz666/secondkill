package com.qf.qfseckill.stragety.impl;

import cn.hutool.json.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.qf.qfseckill.pojo.resp.BaseResp;
import com.qf.qfseckill.stragety.PayService;
import org.springframework.stereotype.Service;

@Service("alipay")
public class AlipayServiceImpl implements PayService {
    @Override
    public BaseResp pay(Long transcationId, Double money, String goodsName, String userId) {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi-sandbox.dl.alipaydev.com/gateway.do","9021000122684840","MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCLajX1c6M+E81bmLYELq9udeGmJVu/H7Ve+mDfJVmWY6tJR5/Gli/3NLy2xLyfYvmAU6k3qyJQC9JYkRIbsEpNSl6n53MY8SVj9vvQ8U4CB8TkW4DVKn32prJfanE9HIvOQcXtAnYZ7C7A6vFQYr3Ih/wCKzGk5zLscHT/Hge2SFRCGbjyReiQzkiTjtk46MYlrudOsMBIzCjaNZEQRdc7PeUv29irE92hxovzuMyr/zgqVfqZPtVIIlTuwCFC1xuPd98STuLW73mXpk73QMeBhwA5PKuOEFZpy28Ume/zeTl88uBjWsH3zZNYuWCQ8Ym5Iv8A4IG0SlT1cBXwdhf7AgMBAAECggEAVtkRSBiDZtZ7JUZajPxwOMWijhfTsZoVKFedJSVlS7RHLd06gUFqG/t0W/7u6fOELhbv4UfwliDnJlkpCKqnfsKHeN1L7Au9GSrw79aCm+0vEHqYJTLVSWPK1nm+lS1FQnXi0KSp4bgp3+/75/G6/EMKEGfMA0grhvL/TzXr4/b7V828StUbbTmZfgBqRDiZLRDbjmnsmAe1KZ14AkjmvmXX+y2/VepTLkkQ9c5stvADM5s/QSP+CoHFUdra4mPdU3a/N58aLjGsNIVzOWDaMWdVlh75DtEljJRLH4zsRQ4FuAJYsJFq08n+0ovaaaoYKjXfD5tPDU6fBBSHkBCGAQKBgQDDAI5unfFgXf8vAt2h9Fqf/eoQw61FQYtID86H+XSexh+QLJ3tK1IB6eBIHHgkURUcKwwrxLSs4CMu15b2zBMHjLdYm9Ftbh78xrqwbADjN7ectYqQ/KoLw7u9CikRoAnA6xhLPju7RyIIe1geyXPFoQlV8JPoEa8pF4W6aVIjewKBgQC3BlGQOsNHvEoytmQjFdWi/afqH8dNaroaz0boYjiDuHkAV36D+Zhw4KTLA91nmOZ1hHihd4cQucPuX38ROTwl0r6aKZlZ4uPCt/QPnwa7gNfVzeKK2V8KY+IfjM35Y7yn+YFJPbA63aMWAQrX35EeLKed6v/4Z2OzSwNA8yF1gQKBgGASRhn5dpIjvTVMFCVWXc+1KTxZK2SThitankYfgHU45xQA502RqjWHJBhi7k32JJBcJQLrRphlVPAfByFQKY/uo+Xj3qorNtkSj6ebjlRjfvKV/k6wLJbfN/9KojEsx6FkMfPgFU2PHhVPr3p7ha44SIEupbAQBlwYWqoceXtBAoGAN3fsxD9q65S/D/uDAbceVhV2DFixOF8+I93p6YfLBPCli1r49tpwPtV2XCjBsyH9xmPtg1IMVx9VqCq9AeVMu1HRfjtZRIqk6GCD0TArBCyaPSv2mooiaa233EP6MLjvdiEB88aEYHLHeGW8eYxCGNkDu7J6/TeoWbBEPizxWQECgYBrpyWPOCmx9ZFrcmEO5Ixqa+Wa8YWDA5tN6CLDLQjl6VRoXIX+QYrmk1qZqaYj2HlwXEdLleqCfDIWrfxjkbK7kz0lafs9RWCRLT27Xed09E87FunkSggrFsH9cvPOSp74rsQFkaoynaB3n9xbSILjBySa34YA/MsaWVpynEqZCA==","json","UTF-8","MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAk0DyD+UXz6sUJ2stg8dXmbKdN+QKcNDSPVLPlb32hLTQ+RtB8cjHlxZc9qZLVnZY6Yycwcb0WAv+yZgOvksCpkto9n+/S4OML1NBswlpByS+ILhw8lMFBBupi7ReolX4GflM3Db1fUeWagGMkGsz946Nmi4TFDiBFu5vruA+Rf5/uAUV0cartkxBrLdYdYFvH8z5ZJfr6bqJ72ZlwnvyA6TDXYlA0XHmdTjKETRJEl0x5RB6PR4EtoJIN/qOKXtsmqv47NbBdxL6SDgz1NHaz3HAkZnQPvySfFwkO08cYpCHYtawlqI7SFmGSPBcqIPhwrR9CslQVw/ekcUSUYvEtQIDAQAB","RSA2");
        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
//异步接收地址，仅支持http/https，公网可访问
        request.setNotifyUrl("http://qmbyyz.natappfree.cc/notifyUrl");
//同步跳转地址，仅支持http/https
        request.setReturnUrl("http://localhost:8081/#/index/userorder");
/******必传参数******/
        JSONObject bizContent = new JSONObject();
//商户订单号，商家自定义，保持唯一性
        bizContent.put("out_trade_no", transcationId+"");
//支付金额，最小值0.01元
        bizContent.put("total_amount", money);
//订单标题，不可使用特殊符号
        bizContent.put("subject", goodsName);

/******可选参数******/
//手机网站支付默认传值FAST_INSTANT_TRADE_PAY
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");



        request.setBizContent(bizContent.toString());
        AlipayTradeWapPayResponse response = null;
        try {
            response = alipayClient.pageExecute(request);
            if(response.isSuccess()){
                System.out.println("调用成功");
                return new BaseResp().OK(response.getBody(),null);
            } else {
                return new BaseResp().FAIL("支付支付不可用！");
            }

        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return new BaseResp().FAIL("支付支付不可用！");

    }
}
