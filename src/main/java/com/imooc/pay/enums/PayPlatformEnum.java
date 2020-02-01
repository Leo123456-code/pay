package com.imooc.pay.enums;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import lombok.Getter;

/**
 * 支付平台枚举
 * created by Leo徐忠春
 * created Time 2020/1/3-21:09
 * email 1437665365@qq.com
 */
@Getter
public enum PayPlatformEnum {
    //1.支付宝 2.枚举
    ALIPAY(1),
    WX(2),
    ;
    Integer code;

    PayPlatformEnum(Integer code) {
        this.code = code;
    }

    public static PayPlatformEnum getByBestPayTypeEnum(BestPayTypeEnum bestPayTypeEnum) {
//        if (bestPayTypeEnum.getPlatform().name().equals(PayPlatformEnum.ALIPAY.name())) {
//            return PayPlatformEnum.ALIPAY;
//        } else if (bestPayTypeEnum.getPlatform().name().equals(PayPlatformEnum.WX.name())) {
//            return PayPlatformEnum.WX;
//        }


        for (PayPlatformEnum payPlatforEnum : PayPlatformEnum.values()) {
            if (bestPayTypeEnum.getPlatform().name().equals(payPlatforEnum.name())) {
                return payPlatforEnum;
            }
        }
        throw new RuntimeException("错误的支付平台:" + bestPayTypeEnum.name());
    }
}
