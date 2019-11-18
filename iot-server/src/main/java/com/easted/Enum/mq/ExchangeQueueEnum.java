package com.easted.Enum.mq;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;

/**
 * @author jun.wang
 * @title: ExchangeQueueEnum
 * @projectName ownerpro
 * @description: TODO
 * @date 2019/5/30 17:51
 */
public enum ExchangeQueueEnum {

    PLATFORM_MESSAGE("platform_message", "exchange_platform_message", Queue.class, TopicExchange.class, "platform.message", "platform-message"),
    CAR_TRANSPORT("car_transport", "exchange_car_transport", Queue.class, TopicExchange.class, "car.transport", "car-transport"),
    PLATFORM_MESSAGE_BROADCAST("platform_message_broadcast", "exchange_platform_message_broadcast", Queue.class,  FanoutExchange.class,"", "platform-message-broadcast");
    private String queueName;
    private String exchangeName;
    private Class<?> queueType;
    private Class<?> exchangeType;
    private String bindKey;
    private String bindName;//不可出现特殊字符,如下划线(_)等

    ExchangeQueueEnum(String queueName, String exchangeName, Class<?> queueType, Class<?> exchangeType, String bindKey, String bindName) {
        this.queueName = queueName;
        this.exchangeName = exchangeName;
        this.queueType = queueType;
        this.exchangeType = exchangeType;
        this.bindKey = bindKey;
        this.bindName = bindName;
    }

    public String getQueueName() {
        return queueName;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public Class<?> getQueueType() {
        return queueType;
    }

    public Class<?> getExchangeType() {
        return exchangeType;
    }

    public String getBindKey() {
        return bindKey;
    }

    public String getBindName() {
        return bindName;
    }
}
