package com.imooc.pay.controller;

import com.imooc.pay.pojo.PayInfo;
import com.imooc.pay.serviceImpl.PayService;
import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * created by Leo徐忠春
 * created Time 2020/1/2-22:06
 * email 1437665365@qq.com
 */
@Controller
@Slf4j
@RequestMapping("/pay")
public class PayController {
    @Autowired
    private PayService payService;
    @Autowired
    private WxPayConfig wxPayConfig;

    @GetMapping("/create")
    public ModelAndView create(@RequestParam("orderId") String orderId,
                               @RequestParam("amount") BigDecimal amount,
                               @RequestParam("payType") BestPayTypeEnum bestPayTypeEnum){

        PayResponse response = payService.create(orderId, amount,bestPayTypeEnum);
        //支付方式不同，渲染就不同
        Map<String,String> map = new HashMap<>();
        if(bestPayTypeEnum==BestPayTypeEnum.WXPAY_NATIVE){
            map.put("codeUrl",response.getCodeUrl());
            map.put("orderId",orderId);
            map.put("returnUrl",wxPayConfig.getReturnUrl());
            //返回一个视图
            return new ModelAndView("createForWeiXinPayNavite",map);
        }else if(bestPayTypeEnum==BestPayTypeEnum.ALIPAY_PC){
            map.put("body",response.getBody());
            return new ModelAndView("createForAliPayPc",map);
        }
        throw new RuntimeException("暂不支持的支付类型");
        }



    @ResponseBody
    @PostMapping("/notify")
    public String asnyNotify(@RequestBody String notifyData ){

        return payService.asycNotify(notifyData);
    }

    //通过订单号查询支付记录
    @GetMapping("/queryByOrderId")
    @ResponseBody
    public PayInfo queryByOrderId(String orderId){
        log.info("查询支付状态...");

        PayInfo payInfo = payService.queryByOrdreId(orderId);
        return payInfo;
    }
}
