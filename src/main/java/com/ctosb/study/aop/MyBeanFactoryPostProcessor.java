package com.ctosb.study.aop;

import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    public void postProcessBeanFactory(
            ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // TODO Auto-generated method stub
        // BeanFactoryPostProcessor可以修改BEAN的配置信息而BeanPostProcessor不能
        // 我们在这里修改postProcessorBean的username注入属性
        System.out.println("FactoryPostProcessorBean Bean initializing");
        BeanDefinition bd = beanFactory.getBeanDefinition("postProcessorBean");
        MutablePropertyValues pv = bd.getPropertyValues();
        if (pv.contains("username")) {
            pv.addPropertyValue("username", "xiaojun");
            System.out.println(pv.getPropertyValue("username"));
        }
        System.out.println("FactoryPostProcessorBean Bean initializing");

        ProxyFactoryBean proxy = new ProxyFactoryBean();
        proxy.setBeanFactory(beanFactory);
        proxy.setTargetClass(TestBean.class);
        proxy.setInterfaces(TestBean.class.getInterfaces());
        beanFactory.registerSingleton("testBean", proxy.getObject());

    }

}
