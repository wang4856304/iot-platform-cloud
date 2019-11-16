package com.easted.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 车辆消息实体
 */
@Data
@NoArgsConstructor
public class CarMessage {

    /**
     * 消息流水
     */
    private byte[] msgNo;

    /**
     * 消息id
     */
    private byte[] msgId;

    /**
     * 设备号
     */
    private byte[] deviceNo;

    /**
     * 消息体
     */
    private byte[] msgBody;

    /**
     * 消息体属性
     */
    private byte[] msgBodyProp;

    /**
     * 是否分包
     */
    private boolean existBag;

    /**
     * 消息封装项
     */
    private byte[] msgPackage;
}
