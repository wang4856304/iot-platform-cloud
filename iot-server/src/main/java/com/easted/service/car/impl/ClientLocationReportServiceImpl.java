package com.easted.service.car.impl;

import com.easted.annotation.Message;
import com.easted.entity.CarMessage;
import com.easted.service.car.CarMessageService;
import com.easted.utils.MessageUtil;
import org.springframework.stereotype.Service;


/**
 * 位置信息汇报
 */
@Service
@Message(id=0x0200)
public class ClientLocationReportServiceImpl implements CarMessageService {



    @Override
    public byte[] handleMessage(byte[] message) {
        CarMessage carMessage = MessageUtil.getCarMessage(message);
        byte[] msgBody = carMessage.getMsgBody();
        return new byte[0];
    }


    private void parseMessageBody(byte[] msgBody) {

    }
}
