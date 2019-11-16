package com.easted.service.mq.impl;

import com.easted.service.mq.SenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import java.util.UUID;

/**
 * @author jun.wang
 * @title: OtherSender
 * @projectName ownerpro
 * @description: 消息生产api
 * @date 2019/7/24 11:34
 */
@Component
public class SenderServiceImpl implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback, SenderService {

    private final static Logger logger = LoggerFactory.getLogger(SenderServiceImpl.class);

    @Autowired
    @Qualifier("confirmRabbitTemplate")
    private RabbitTemplate rabbitTemplate;

    //消息到达交换机回调
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String message) {
        if (!b) {
            logger.error("message send to exchange fail, id={}, message={}", correlationData.getId(), message);
        }
    }

    @Override
    public void send(String exchangeName, String routeKey, Object msg) {
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnCallback(this);
        rabbitTemplate.setConfirmCallback(this);
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString().replace("-", ""));
        rabbitTemplate.convertAndSend(exchangeName, routeKey, msg, correlationData);
        logger.info("send message to exchange, exchange name={}, message={}", exchangeName, msg);
    }

    //exchange到queue成功,则不回调return,exchange到queue失败,则回调return(需设置mandatory=true,否则不回回调,消息就丢了)
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        logger.error("message={}, replyCode={}, replyText={}, exchange={}, routingKey={}",
                new String(message.getBody()), replyCode, replyText, exchange, routingKey);
    }
}
