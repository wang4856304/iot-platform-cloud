package com.easted.Enum.mq;

import org.springframework.amqp.core.TopicExchange;

/**
 * @author jun.wang
 * @title: ExchangeEnum
 * @projectName ownerpro
 * @description: TODO
 * @date 2019/5/30 17:26
 */
public enum  ExchangeEnum {

    PLATFORM_MESSAGE("exchange_platform_message", TopicExchange.class,"平台消息交换机"),
    CAR_TRANSPORT("exchange_car_transport", TopicExchange.class, "车辆运输业务");

    private String exchangeName;
    private Class exchangeType;
    private String desc;

    ExchangeEnum(String exchangeName, Class exchangeType, String desc) {
        this.exchangeName = exchangeName;
        this.exchangeType = exchangeType;
        this.desc = desc;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public Class getExchangeType() {
        return exchangeType;
    }

    public String getDesc() {
        return desc;
    }
}
