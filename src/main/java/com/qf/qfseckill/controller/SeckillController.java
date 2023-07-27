package com.qf.qfseckill.controller;

import com.qf.qfseckill.anno.Limiter;
import com.qf.qfseckill.pojo.req.SeckillReq;
import com.qf.qfseckill.pojo.resp.BaseResp;
import com.qf.qfseckill.service.SeckillService;
import com.qf.qfseckill.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/seckill")
public class SeckillController {
    @Autowired
    SeckillService seckillService;

    /**
     * 获取到时间段
     */
    @RequestMapping("/findDate")
    public BaseResp findDate(){
        List<Date> dateMenus = DateUtil.getDateMenus();
        List dateList=new ArrayList();
        for (Date date:dateMenus
             ) {
            String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
            dateList.add(format);
        }
        return new BaseResp().OK(dateList,null);
    }

    @RequestMapping("/findSeckill")
    public BaseResp findSeckill(@RequestBody SeckillReq req){
        //从redis中获取到该时间段下的所有的秒杀商品
    return seckillService.findSeckill(req);
    }

    @Limiter
    @RequestMapping("/seckill")
    public BaseResp seckill(@RequestBody SeckillReq req, HttpServletRequest request){
        return seckillService.seckill(req,request);
    }
}
