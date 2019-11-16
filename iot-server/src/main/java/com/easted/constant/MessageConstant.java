package com.easted.constant;

import com.easted.route.MessageRouteService;
import com.easted.service.car.CarMessageService;
import java.util.HashMap;
import java.util.Map;

public class MessageConstant {

    public static Map<Integer, CarMessageService> CAR_MESSAGE_SERVICE_MAP = new HashMap<>();//车辆运输业务消息路由
    public static Map<Integer, MessageRouteService> MESSAGE_ROUTE_MAP  = new HashMap<>();//消息路由

    //路由标记
    public static final int CAR_ROUTE_FLAG = 0x7e;//车辆运输路由标志

}
