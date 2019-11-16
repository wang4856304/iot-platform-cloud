package com.easted.service;

import com.easted.annotation.Message;
import com.easted.annotation.Route;
import com.easted.constant.MessageConstant;
import com.easted.context.SpringContext;
import com.easted.route.MessageRouteService;
import com.easted.service.car.CarMessageService;

import java.util.List;

public class MessageInitialize {

    public static void init() {

        List<MessageRouteService> messageRouteServiceList = SpringContext.getBeans(MessageRouteService.class);
        messageRouteServiceList.stream().map(messageRouteService -> {
            Route route = messageRouteService.getClass().getAnnotation(Route.class);
            if (route != null) {
                int id = route.id();
                MessageConstant.MESSAGE_ROUTE_MAP.put(id, messageRouteService);
            }
            return 0;
        }).count();

        List<CarMessageService> carMessageServiceList = SpringContext.getBeans(CarMessageService.class);
        carMessageServiceList.stream().map(carMessageService -> {
            Message message = carMessageService.getClass().getAnnotation(Message.class);
            if (message != null) {
                int id = message.id();
                MessageConstant.CAR_MESSAGE_SERVICE_MAP.put(id, carMessageService);
            }
            return 0;
        }).count();
    }
}
