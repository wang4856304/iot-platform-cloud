package com.easted.service;

import io.netty.buffer.ByteBuf;

/**
 * @Author wangJun
 * @Description //TODO
 * @Date ${date} ${time}
 **/
public interface MessageHandlerService {
    void handlerMsg(String ip, String clientId, byte[] message);
}
