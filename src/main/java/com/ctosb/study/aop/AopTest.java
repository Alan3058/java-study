package com.ctosb.study.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;

public class AopTest {

    /**
     * @param args
     */
    public static void main(String[] args) {

        //使用ProxyFactory
        ProxyFactory proxyFactory = new ProxyFactory();

        proxyFactory.addInterface(HelloWorld.class);
        proxyFactory.addInterface(HelloChina.class);

        proxyFactory.addAdvice(new TimeAdvice());
        proxyFactory.addAdvice(new LogAdvice());

        proxyFactory.setTarget(new HelloWorldImpl());

//		HelloWorld helloWorld = (HelloWorld) proxyFactory
//				.getProxy(AopTest.class.getClassLoader());
//		helloWorld.sayHelloWorld();

        HelloChina helloChina = (HelloChina) proxyFactory
                .getProxy(AopTest.class.getClassLoader());
        helloChina.sayHelloChina();


        //使用ProxyFactoryBean
//		ProxyFactoryBean pfb = new ProxyFactoryBean();
//		pfb.addInterface(HelloWorld.class);
//		pfb.addAdvice(new LogAdvice());
//		pfb.setTarget(new HelloWorldImpl());
//		HelloWorld helloWorldA = (HelloWorld) pfb.getObject();
//		helloWorldA.sayHelloWorld();
    }

}

interface HelloWorld {
    void sayHelloWorld();
}

interface HelloChina {
    void sayHelloChina();
}

class HelloWorldImpl implements HelloWorld, HelloChina {

    public void sayHelloChina() {
        System.out.println("Hello China!");

    }

    public void sayHelloWorld() {
        System.out.println("Hello World!");

    }
}

/**
 * 日志拦截器
 *
 * @author jin.xiong
 */
class LogAdvice implements MethodInterceptor {

    public Object invoke(MethodInvocation methodinvocation) throws Throwable {
        System.out.println("start " + methodinvocation.getMethod().getName());
        Object ob = methodinvocation.proceed();
        System.out.println("end " + methodinvocation.getMethod().getName());
        return ob;
    }
}

/**
 * 时间拦截器
 *
 * @author jin.xiong
 */
class TimeAdvice implements MethodInterceptor {

    public Object invoke(MethodInvocation methodinvocation) throws Throwable {
        long start = System.currentTimeMillis();
        Thread.sleep(1000);
        Object ob = methodinvocation.proceed();
        System.out.println("method proccess time "
                + (System.currentTimeMillis() - start));
        return ob;
    }
}
