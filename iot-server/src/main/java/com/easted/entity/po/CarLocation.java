package com.easted.entity.po;


import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 车辆位置基本信息
 */
@Data
@NoArgsConstructor
public class CarLocation {

    /**
     * 报警列表
     */
    private String alarmList;

    /**
     * 车辆状态列表
     */
    private String stateList;


    /**
     * 经度
     */
    private double longitude;

    /**
     * 纬度
     */
    private double latitude;

    /**
     * 海拔
     */
    private double altitude;

    /**
     * 速度
     */
    private double speed;

    /**
     * 方向
     */
    private int direction;

    /**
     * 上报时间,YY-MM-DD-hh-mm-ss
     */
    private String time;

}
