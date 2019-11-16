package com.easted.Enum;


public enum  MessageIdEnum {
    CLIENT_GENERAL_RES(0x0001, "终端通用应答"), CLIENT_REGISTER(0x0100, "终端注册");

    MessageIdEnum(int id, String desc) {
        this.id = id;
        this.desc = desc;
    }
    private int id;
    private String desc;

    public int getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }
}
