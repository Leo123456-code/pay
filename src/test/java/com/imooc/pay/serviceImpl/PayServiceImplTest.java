package com.imooc.pay.serviceImpl;

import com.imooc.pay.PayApplicationTests;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * created by Leo徐忠春
 * created Time 2020/1/2-20:18
 * email 1437665365@qq.com
 */
public class PayServiceImplTest extends PayApplicationTests {
    @Autowired
    private PayService payService;

    @Test
    public void create(){
        payService.create("15894563147894215", BigDecimal.valueOf(0.01), BestPayTypeEnum.WXPAY_NATIVE);

    }

}