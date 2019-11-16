package com.easted.service.mq.impl;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.easted.constant.MQConstants;
import com.easted.netty.ServerHandler;
import com.easted.service.mq.ConsumerService;
import com.easted.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.stereotype.Service;

/**
 * @author jun.wang
 * @title: PlatformServiceImpl
 * @projectName ownerpro
 * @description: TODO
 * @date 2019/5/30 16:20
 */

@Service
//@RabbitListener(queues = {MQConstants.PLATFORM_MESSAGE_RETURN_QUEUE})
public class PlatformServiceImpl implements ConsumerService {

    private static Logger logger = LoggerFactory.getLogger(PlatformServiceImpl.class);

    @Override
    public void consumer(Message message) {
        //手动处理应答，避免消费端失败无限ack循环
        try {
            String msg = new String(message.getBody());
            int i = Integer.valueOf(msg);
            System.out.println(msg);
        }
        catch (Exception e) {
            logger.error("error message", e);
        }
    }

    @Override
    @RabbitHandler
    public void manualConsumer(String str, Message message, Channel c) throws Exception {

        if (message.getBody() == null) {
            logger.warn("message is empty");
            return;
        }
        String msg = new String(message.getBody());
        logger.info("accept message, queue={}, message={}", MQConstants.PLATFORM_MESSAGE_RETURN_QUEUE, msg);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        c.basicAck(deliveryTag, false);//手工ack,发送端必须设置回调,false只确认当前一个消息收到，true确认所有consumer获得的消息
        JSONObject json = JSONObject.parseObject(msg);
        String clientId = json.getString("clientId");
        String messageHexStr = json.getString("message");

        //处理完成业务，向客户端推送信息
        io.netty.channel.Channel channel = ServerHandler.CHANNEL_MAP.get(clientId);
        if (channel != null) {
            logger.debug("send data:{}", messageHexStr);
            channel.writeAndFlush(StringUtil.hexStringToBytes(messageHexStr));
        }
        else {
            logger.error("invalid channel");
        }
    }
}
