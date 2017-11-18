package com.ctosb.study.freemarker;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Freemarker {

    public void testFreeMaker() {
        // 加载模板
        String dir = this.getClass().getResource("").getPath();
        Configuration config = new Configuration();
        try {
            config.setDirectoryForTemplateLoading(new File(dir));

            // 设置对象包装器
            config.setObjectWrapper(new DefaultObjectWrapper());

            // 设置异常处理器
            config.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);

            // 定义数据模型
            Map<String, Object> map = new HashMap<String, Object>();
            List<User> users = new ArrayList<User>();
            User user = new User("alan", "123");
            users.add(user);
            user = new User("alan2", "1234");
            users.add(user);
            map.put("users", users);
            map.put("username", "testuser");


            // 通过freemaker解释模板,首先需要获得模板对象
            Template temp = config.getTemplate("test.ftl");

            // 定义模板输出
//			PrintWriter out = new PrintWriter(new BufferedWriter(
//					new FileWriter("d:/aa.txt")));
            StringWriter out = new StringWriter();
            // 模板解释
            temp.process(map, out);
            System.out.println(out.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Freemarker().testFreeMaker();
    }
}
