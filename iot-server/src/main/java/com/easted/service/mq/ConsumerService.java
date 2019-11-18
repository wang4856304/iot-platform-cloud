package com.easted.service.mq;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

/**
 * @author jun.wang
 * @title: ConsumerService
 * @projectName ownerpro
 * @description: TODO
 * @date 2019/5/30 16:19
 */
public interface ConsumerService {
    void consumer(Message message);
    void manualConsumer(String str, Message message, Channel channel) throws Exception;
    void broadcastConsumer(String str, Message message, Channel channel) throws Exception;
}
