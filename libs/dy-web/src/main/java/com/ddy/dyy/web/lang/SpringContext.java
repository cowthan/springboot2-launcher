package com.ddy.dyy.web.lang;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 不建议使用，更不能在应用启动过程中使用，如@PostConstruct中
 */
@Component
public class SpringContext implements ApplicationContextAware {

    private static ApplicationContext context;

    public static ApplicationContext getContext() {
        return context;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * .
     *
     * @param clazz .
     * @param <T>   .
     * @return .
     */
    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }


    public static <T> List<T> getBeanList(Class<T> clazz) {
        List<T> list = new ArrayList<>();
        list.addAll(context.getBeansOfType(clazz).values());
        return list;
    }
}