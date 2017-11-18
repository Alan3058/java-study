package com.ctosb.study.aop;


import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class BeanPostPorcessorTest extends TestCase {
    private ApplicationContext context;

    protected void setUp() throws Exception {
        super.setUp();
        String[] paths = {"classpath:/spring.xml"};

//		context = new ClassPathXmlApplicationContext("spring.xml");
        context = new FileSystemXmlApplicationContext("H:\\workspaceASR\\study\\src\\main\\java\\aop/spring.xml");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testBeanPostProcessor() {

    }

    public void testBeanFactoryPostProcessor() {
        //PostProcessorBean bean =(PostProcessorBean)ServiceLocator.getService("postProcessorBean");
        PostProcessorBean bean = (PostProcessorBean) context.getBean("postProcessorBean");
        System.out.println("---------------testBeanFactoryPostProcessor----------------");
        System.out.println("username:" + bean.getUsername());
        System.out.println("password:" + bean.getPassword());
        //
        ;
//		TestBean b =(TestBean)context.getBean("testBean");
        for (String s : context.getBeanDefinitionNames()) {
            System.out.println(s);
        }
    }
}
