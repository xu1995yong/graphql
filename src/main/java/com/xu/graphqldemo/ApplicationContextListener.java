package com.xu.graphqldemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Slf4j
public class ApplicationContextListener implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {
    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        String[] names = context.getBeanDefinitionNames();
        for (String name : names) {
            log.info(">>>>>>       " + name);
        }
    }
}
