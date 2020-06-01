package com.zx.enums;

/**
 * @ClassName: PayMethodEnum
 * @Author: zhengxin
 * @Description: 支付方式 枚举
 * @Date: 2020/5/24 22:05
 * @Version: 1.0
 */
public enum PayMethodEnum {

	WEIXIN(1, "微信"),
	ALIPAY(2, "支付宝");

	public final Integer type;
	public final String value;

	PayMethodEnum(Integer type, String value){
		this.type = type;
		this.value = value;
	}

}
