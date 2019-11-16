package com.easted.route.impl;

import com.easted.annotation.Route;
import com.easted.constant.MessageConstant;
import com.easted.route.MessageRouteService;
import com.easted.service.car.CarMessageService;
import com.easted.utils.MessageUtil;
import com.easted.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * 车辆运输消息解析
 */
@Service
@Route(id=0x7e)
public class CarRouteServiceImpl implements MessageRouteService {

    private Logger logger = LoggerFactory.getLogger(CarRouteServiceImpl.class);

    @Override
    public byte[][] routeMessage(byte[] message) {
        if (message == null || message.length == 0) {
            logger.warn("message is empty");
            return null;
        }
        byte[][] msgBags = MessageUtil.parseBag(message, MessageUtil.flag);
        List<byte[]> resultList = new ArrayList<>();
        for (byte[] bytes : msgBags) {
            logger.info("服务器收到客户端分包数据:" + StringUtil.bytes2HexString(bytes, " "));
            //校验位
            /*byte checkFlag = bytes[bytes.length-1];
            if (!MessageUtil.checkMsg(bytes, checkFlag)) {
                logger.error("message check fail");
                return;
            }*/

            //消息id
            byte[] bMsgId = MessageUtil.getMessageId(bytes);
            String msgStr = StringUtil.bytes2HexString(bMsgId);
            logger.info("msgStr={}", msgStr);

            //终端设备号
            byte[] bDeviceNo = MessageUtil.getDeviceNo(bytes);
            String deviceNo = new String(bDeviceNo, Charset.forName("GBK"));


            //消息体属性
            byte[] bMsgBodyProp = MessageUtil.getMessageBodyProp(bytes);

            //判断是否分包
            boolean exist = MessageUtil.existBags(bMsgBodyProp);

            //消息流水号
            byte[] bMsgNo = MessageUtil.getMessageNo(bytes);

            if (exist) {
                //消息包封装项
                byte[] bMsgPackage = MessageUtil.getMessagePackage(bytes);
            }

            //消息体
            byte[] msgBody = MessageUtil.getMessageBody(bytes);

            byte[] tempMsgId = new byte[4];
            tempMsgId[0] = bMsgId[1];
            tempMsgId[1] = bMsgId[0];
            tempMsgId[2] = 0;
            tempMsgId[3] = 0;
            int msgId = MessageUtil.bytes2Int(tempMsgId);
            CarMessageService carMessageService = MessageConstant.CAR_MESSAGE_SERVICE_MAP.get(msgId);
            if (carMessageService == null) {
                logger.error("there is not exist instance for client, deviceNo={}", deviceNo);
                throw new RuntimeException(String.format("there is not exist instance for client, deviceNo=%s", deviceNo));
            }
            byte[] bResult = carMessageService.handleMessage(bytes);
            resultList.add(bResult);
        }
        byte[][] result = new byte[msgBags.length][];
        return resultList.toArray(result);
    }
}
