package com.ctosb.study.aop;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;


public class MyBeanPostProcessor implements BeanPostProcessor {


    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        // TODO Auto-generated method stub
        //如果是PostProcessorBean则username信息
        if (bean instanceof PostProcessorBean) {
            System.out.println("PostProcessorBean Bean initialized");
            PostProcessorBean pb = (PostProcessorBean) bean;

            System.out.println("username:" + pb.getUsername());
        }
        return bean;
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        // TODO Auto-generated method stub
        if (bean instanceof PostProcessorBean) {
            System.out.println("PostProcessorBean Bean initializing");
            PostProcessorBean pb = (PostProcessorBean) bean;

            System.out.println("username:" + pb.getUsername());
        }
        return bean;
    }

}
