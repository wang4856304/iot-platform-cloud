package com.easted.Enum;

import com.easted.constant.MQConstants;

public enum ProtocolEnum {

    CAR_TRANSPORT(0X7e, MQConstants.CAR_TRANSPORT_QUEUE, "车辆运输业务");

    ProtocolEnum (int protocolFlag, String queueName, String desc) {
        this.protocolFlag = protocolFlag;
        this.queueName = queueName;
        this.desc = desc;
    }

    private int protocolFlag;

    private String queueName;

    private String desc;

    public int getProtocolFlag() {
        return protocolFlag;
    }

    public String getQueueName() {
        return queueName;
    }

    public String getDesc() {
        return desc;
    }
}
