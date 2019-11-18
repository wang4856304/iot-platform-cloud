package com.easted.service.mq.impl;

import com.alibaba.fastjson.JSONObject;
import com.easted.Enum.mq.ExchangeQueueEnum;
import com.easted.service.mq.SenderService;
import com.rabbitmq.client.Channel;
import com.easted.constant.MQConstants;
import com.easted.netty.ServerHandler;
import com.easted.service.mq.ConsumerService;
import com.easted.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
 * @author jun.wang
 * @title: PlatformServiceImpl
 * @projectName ownerpro
 * @description: 平台接收业务系统消息转发给客户端
 * @date 2019/5/30 16:20
 */

@Service
public class PlatformServiceImpl implements ConsumerService {

    private static Logger logger = LoggerFactory.getLogger(PlatformServiceImpl.class);

    @Resource
    private SenderService senderService;

    private static final int MESSAGE_TYPE = 1;//正常消息类型
    private static final int BROADCAST_MESSAGE_TYPE = 2;//广播消息类型


    //@RabbitListener(queues = {MQConstants.PLATFORM_MESSAGE_RETURN_QUEUE})
    @Override
    public void consumer(Message message) {

        //手动处理应答，避免消费端失败无限ack循环
        /*try {
            String msg = new String(message.getBody());
        }
        catch (Exception e) {
            logger.error("error message", e);
        }*/
    }

    @Override
    @RabbitListener(queues = {MQConstants.PLATFORM_MESSAGE_RETURN_QUEUE})
    @RabbitHandler
    public void manualConsumer(String str, Message message, Channel c) throws Exception {

        sendMessageWithType(message, c, MESSAGE_TYPE);
    }

    /**
     * 处理从集群其他服务器发送的广播消息
     * @param str
     * @param message
     * @param c
     * @throws Exception
     */

    @RabbitListener(queues = {MQConstants.PLATFORM_MESSAGE_BROADCAST_QUEUE})
    @RabbitHandler
    @Override
    public void broadcastConsumer(String str, Message message, Channel c) throws Exception {
        sendMessageWithType(message, c, BROADCAST_MESSAGE_TYPE);
    }

    /**
     * 根据消息类型发送消息
     * @param message
     * @param type
     */
    private void sendMessageWithType(Message message, Channel c, int type) throws Exception {
        if (message.getBody() == null) {
            logger.warn("message is empty");
            return;
        }
        String msg = new String(message.getBody());
        String queueName = "";
        if (MESSAGE_TYPE == type) {
            queueName = MQConstants.PLATFORM_MESSAGE_RETURN_QUEUE;
        }
        else if (BROADCAST_MESSAGE_TYPE == type) {
            queueName = MQConstants.PLATFORM_MESSAGE_BROADCAST_QUEUE;
        }
        logger.info("accept message, queue={}, message={}", queueName, msg);
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
            if (type == MESSAGE_TYPE) {
                senderService.send(ExchangeQueueEnum.PLATFORM_MESSAGE_BROADCAST.getExchangeName(), ExchangeQueueEnum.PLATFORM_MESSAGE_BROADCAST.getBindKey(), msg);
            }
            else if (type == BROADCAST_MESSAGE_TYPE) {
                logger.warn("invalid channel");
            }
        }
    }
}
