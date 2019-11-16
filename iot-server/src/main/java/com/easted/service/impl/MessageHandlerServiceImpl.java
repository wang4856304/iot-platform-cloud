package com.easted.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.easted.Enum.ProtocolEnum;
import com.easted.Enum.mq.ExchangeQueueEnum;
import com.easted.service.MessageHandlerService;
import com.easted.service.mq.SenderService;
import com.easted.utils.StringUtil;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author wangJun
 * @Description //消息处理统一入口
 * @Date 2018/12/24 10:15:32
 **/

@Service
public class MessageHandlerServiceImpl implements MessageHandlerService {

    private Logger logger = LoggerFactory.getLogger(MessageHandlerServiceImpl.class);

    @Resource
    private SenderService senderService;

    @Override
    public void handlerMsg(String ip, String clientId, byte[] message) {
        if (message == null || message.length == 0) {
            logger.warn("message is empty");
            return;
        }

        String messageStr = StringUtil.bytes2HexString(message);
        logger.info("recv message:" + messageStr);

        JSONObject json = new JSONObject();
        json.put("clientId", clientId);
        json.put("message", messageStr);

        int flag = message[0];//标识位，标识业务类型
        boolean protocol = false;

        //根据不同的协议，将消息推送到不同的业务mq中
        for (ProtocolEnum protocolEnum: ProtocolEnum.values()) {
            if (protocolEnum.getProtocolFlag() == flag) {
                //推送消息到mq
                senderService.send(protocolEnum.getExchangeQueue().getExchangeName(), protocolEnum.getExchangeQueue().getBindKey(), json.toJSONString());
                protocol = true;
                break;
            }
        }
        if (!protocol) {
            logger.error("this protocol is not exist in platform");
            throw new RuntimeException("this protocol is not exist in iot platform");
        }
        /*MessageRouteService messageRouteService = MessageConstant.MESSAGE_ROUTE_MAP.get(flag);
        byte[][] result = messageRouteService.routeMessage(message);

        //处理完成业务，向客户端推送信息
        Channel channel = ServerHandler.CHANNEL_MAP.get(clientId);
        if (channel != null) {
            for (byte[] b: result) {
                logger.info("send data:{}", StringUtil.bytes2HexString(b));
                channel.writeAndFlush(b);
            }
        }
        else {
            logger.error("invalid channel");
        }*/
    }
}