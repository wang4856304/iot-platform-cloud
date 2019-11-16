package com.easted.Enum;

import com.easted.Enum.mq.ExchangeQueueEnum;

public enum ProtocolEnum {

    CAR_TRANSPORT(0x7e, ExchangeQueueEnum.CAR_TRANSPORT, "车辆运输业务");

    ProtocolEnum(int protocolFlag, ExchangeQueueEnum ExchangeQueue, String desc) {
        this.protocolFlag = protocolFlag;
        this.ExchangeQueue = ExchangeQueue;
        this.desc = desc;
    }

    private int protocolFlag;//协议标记

    private ExchangeQueueEnum ExchangeQueue;//对应mq

    private String desc;

    public int getProtocolFlag() {
        return protocolFlag;
    }

    public ExchangeQueueEnum getExchangeQueue() {
        return ExchangeQueue;
    }

    public String getDesc() {
        return desc;
    }
}
