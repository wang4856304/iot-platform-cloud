package com.easted.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author wangJun
 * @Description //TODO
 * @Date 2018/01/07
 **/

@Component
public class SpringContext implements ApplicationContextAware {

    private static Logger logger = LoggerFactory.getLogger(SpringContext.class);

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContext.context = applicationContext;
    }

    public static ApplicationContext getContext() {
        if (SpringContext.context == null) {
            logger.warn("context is null");
            return null;
        }
        return SpringContext.context;
    }

    public static<T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }


    public static<T> T getBean(String name, Class<?> clazz) {
        return (T)context.getBean(name, clazz);
    }

    public static<T> T getBean(String name) {
        if (!context.containsBean(name)) {
            logger.warn("bean is empty, name={}", name);
            return null;
        }
        return (T)context.getBean(name);
    }

    public static<T> List<T> getBeans(Class<T> tClass) {
        String[] beanNames = context.getBeanNamesForType(tClass);
        return Arrays.stream(beanNames).map(beanName -> (T)getBean(beanName)).collect(Collectors.toList());

    }
}
