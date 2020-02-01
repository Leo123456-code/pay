package com.imooc.pay.service;

import com.imooc.pay.pojo.PayInfo;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayResponse;

import java.math.BigDecimal;

/**
 * created by Leo徐忠春
 * created Time 2020/1/2-19:50
 * email 1437665365@qq.com
 */
public interface IPayService {
    //创建/发起支付
    PayResponse create(String orderId, BigDecimal amount,
                       BestPayTypeEnum bestPayTypeEnum);
    //异步通知处理
    String asycNotify(String notifyData);
    //通过订单号查询支付记录
    PayInfo queryByOrdreId(String orderId);
}
