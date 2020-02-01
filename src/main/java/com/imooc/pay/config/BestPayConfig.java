package com.imooc.pay.config;

import com.lly835.bestpay.config.AliPayConfig;
import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * created by Leo徐忠春
 * created Time 2020/1/3-14:08
 * email 1437665365@qq.com
 */
@Component
public class BestPayConfig {
    @Autowired
    private WxAccountConfig wxAccountConfig;
    @Autowired
    private AlipayAccountConfig alipay;

    @Bean
    public BestPayService bestPayService(WxPayConfig wxPayConfig){
        //阿里网页支付
        AliPayConfig aliPayConfig = new AliPayConfig();
        aliPayConfig.setAppId(alipay.getAppId());
        aliPayConfig.setPrivateKey(alipay.getPrivateKey());
        aliPayConfig.setAliPayPublicKey(alipay.getPayPublicKey());
        aliPayConfig.setNotifyUrl(alipay.getNotifyUrl());
        aliPayConfig.setReturnUrl(alipay.getReturnUrl());
        BestPayServiceImpl bestPayService = new BestPayServiceImpl();
        bestPayService.setWxPayConfig(wxPayConfig);
        bestPayService.setAliPayConfig(aliPayConfig);

        return bestPayService;
    }

    @Bean
    public WxPayConfig wxPayConfig(){
        //微信NAvite支付
        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setAppId(wxAccountConfig.getAppId());
        wxPayConfig.setMchId(wxAccountConfig.getMchId());
        wxPayConfig.setMchKey(wxAccountConfig.getMchKey());
        wxPayConfig.setNotifyUrl(wxAccountConfig.getNotifyUrl());
        wxPayConfig.setReturnUrl(wxAccountConfig.getReturnUrl());
        return wxPayConfig;
    }
}
