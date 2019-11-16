package com.easted.service.car.impl;

import com.easted.annotation.Message;
import com.easted.entity.CarMessage;
import com.easted.service.car.CarMessageService;
import com.easted.utils.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 * 终端注册
 */

@Service
@Message(id=0x0100)
public class ClientRegisterServiceImpl implements CarMessageService {

    private static Logger logger = LoggerFactory.getLogger(ClientRegisterServiceImpl.class);


    @Override
    public byte[] handleMessage(byte[] message) {

        CarMessage carMessage = MessageUtil.getCarMessage(message);

        //响应
        byte[] response = new byte[1];
        response[0] = 1;

        return MessageUtil.response(carMessage.getMsgNo(), carMessage.getMsgId(), response);
    }
}
