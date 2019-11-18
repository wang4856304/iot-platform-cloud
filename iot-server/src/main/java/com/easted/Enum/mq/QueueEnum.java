package com.easted.Enum.mq;

/**
 * @author jun.wang
 * @title: QueueEnum
 * @projectName ownerpro
 * @description: TODO
 * @date 2019/5/30 14:31
 */
public enum QueueEnum {

    PLATFORM_MESSAGE("platform_message", "平台消息队列"),
    CAR_TRANSPORT("car_transport", "车辆运输业务"),
    PLATFORM_MESSAGE_BROADCAST("platform_message_broadcast", "平台消息广播");

    private String queueName;
    private String desc;

    QueueEnum(String queueName, String desc) {
        this.queueName = queueName;
        this.desc = desc;
    }

    public String getQueueName() {
        return queueName;
    }

    public String getDesc() {
        return desc;
    }
}
