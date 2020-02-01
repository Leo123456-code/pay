package com.imooc.pay.serviceImpl;

import com.imooc.pay.dao.PayInfoMapper;
import com.imooc.pay.enums.PayPlatformEnum;
import com.imooc.pay.pojo.PayInfo;
import com.imooc.pay.service.IPayService;
import com.lly835.bestpay.enums.BestPayPlatformEnum;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.enums.OrderStatusEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.BestPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 微信支付 Native支付
 * created by Leo徐忠春
 * created Time 2020/1/2-19:53
 * email 1437665365@qq.com
 */
@Slf4j
@Service
public class PayService implements IPayService {
    @Autowired
    private BestPayService bestPayService;
    @Autowired
    private PayInfoMapper payInfoMapper;

    /**
     * 创建/发起支付
     * @param orderId
     * @param amount
     */
    @Override
    public PayResponse create(String orderId, BigDecimal amount,
                              BestPayTypeEnum bestPayTypeEnum) {

        //写入数据库
        PayInfo payInfo = new PayInfo(Long.parseLong(orderId), PayPlatformEnum.getByBestPayTypeEnum(bestPayTypeEnum).getCode(),
                OrderStatusEnum.NOTPAY.name(),amount);
        payInfoMapper.insertSelective(payInfo);
        PayRequest request = new PayRequest();
        request.setOrderName("7769201-徐忠春开发支付");
        request.setOrderId(orderId);
        request.setOrderAmount(amount.doubleValue());
        request.setPayTypeEnum(bestPayTypeEnum);

        PayResponse response = bestPayService.pay(request);
        log.info("response={}",response);
        return response;
    }

    //异步通知处理
    @Override
    public String asycNotify(String notifyData) {
        //1.签名效验
        PayResponse payResponse=bestPayService.asyncNotify(notifyData);
        log.info("payResponse={}",notifyData);
        //2.金额效验(从数据库查订单)
        PayInfo payInfo = payInfoMapper.selectByOrderNo
                (Long.parseLong(payResponse.getOrderId()));
        if(payInfo==null){
            //告警
            throw new RuntimeException("通过OrderNo查到的数据为空...");
        }
        //如果支付状态不是已支付
        if(!payInfo.getPlatformStatus().equals(OrderStatusEnum.SUCCESS.name())){
            //CompareTo 比较结果为0表示相等,不等于0就抛出异常
           if(payInfo.getPayAmount().compareTo(BigDecimal.valueOf(payResponse.getOrderAmount()))!=0){
               //告警
               throw new RuntimeException("和数据库的金额不一致...OrderNo="+payResponse.getOrderId());
           }

        }
        //3.修改订单支付状态
        payInfo.setPlatformStatus(OrderStatusEnum.SUCCESS.name());
        payInfoMapper.updateByPrimaryKeySelective(payInfo);
        //TODO rabbitMQ  pay项目发送MQ消息,mall接受MQ消息
        //4.告诉微信和支付宝不要再通知
        if(payResponse.getPayPlatformEnum()== BestPayPlatformEnum.WX){
            return "<xml>\n" +
                    "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                    "  <return_msg><![CDATA[OK]]></return_msg>\n" +
                    "</xml>";
        }else if(payResponse.getPayPlatformEnum()==BestPayPlatformEnum.ALIPAY){
            return "success";
        }
          throw new RuntimeException("异步通知中错误的支付平台");
    }

    @Override
    public PayInfo queryByOrdreId(String orderId) {
        PayInfo payInfo = payInfoMapper.selectByOrderNo(Long.parseLong(orderId));
        return payInfo;
    }


}
