package com.ctosb.study.xml;

import com.thoughtworks.xstream.XStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.util.*;

public class XStreamTest {
    private XStream xstream = null;
    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;
    private Student bean = null;

    @Before
    public void init() {
        try {
            xstream = new XStream();
            // xstream = new XStream(new DomDriver());
            // 需要xpp3 jar
        } catch (Exception e) {
            e.printStackTrace();
        }
        bean = new Student();
        bean.setAddress("china");
        bean.setEmail("jack@email.com");
        bean.setId(1);
        bean.setName("jack");
        Birthday day = new Birthday();
        day.setBirthday("2010-11-22");
        bean.setBirthday(day);
    }

    @After
    public void destory() {
        xstream = null;
        bean = null;
        try {
            if (out != null) {
                out.flush();
                out.close();
            }
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.gc();
    }

    public final void fail(String string) {
        System.out.println(string);
    }

    public final void failRed(String string) {
        System.err.println(string);
    }

    /**
     * Java对象转换成XML字符串
     *
     * @author alan
     * @date 2014-6-26 下午3:48:51
     */
    // @Test
    public void writeBean2XML() {
        try {
            fail("------------Bean->XML------------");
            fail(xstream.toXML(bean));
            fail("重命名后的XML");
            // 类重命名
            // xstream.alias("account", Student.class);
            // xstream.alias("生日", Birthday.class);
            // xstream.aliasField("生日",Student.class, "birthday");
            // xstream.aliasField("生日",Birthday.class, "birthday");
            // fail(xstream.toXML(bean));
            // 属性重命名
            xstream.aliasField("邮件", Student.class, "email");
            // 包重命名
            xstream.aliasPackage("root", "xml");
            fail(xstream.toXML(bean));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将Java的List集合转换成XML对象
     *
     * @author alan
     * @date 2014-6-26 下午3:48:40
     */
    // @Test
    public void writeList2XML() {
        try { // 修改元素名称
            xstream.alias("student", Student.class);
            fail("----------List-->XML----------");
            List<Object> list = new ArrayList<Object>();
            list.add(bean);
            list.add(bean);
            bean = new Student();
            bean.setAddress("china");
            bean.setEmail("tom@125.com");
            bean.setId(2);
            bean.setName("tom");
            Birthday day = new Birthday("2010-11-22");
            bean.setBirthday(day);
            list.add(bean);
            // 将ListBean中的集合设置空元素，即不显示集合元素标签
            // //xstream.addImplicitCollection(ListBean.class, "list");
            // //设置reference模型
            xstream.setMode(XStream.NO_REFERENCES);// 不引用
            // xstream.setMode(XStream.ID_REFERENCES);// id引用
            // xstream.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);//绝对路径引用
            // 将name设置为父类（Student）的元素的属性
            xstream.useAttributeFor(Student.class, "name");
            xstream.useAttributeFor(Birthday.class, "birthday"); // 修改属性的name
            xstream.aliasAttribute("姓名", "name");
            xstream.aliasField("生日", Birthday.class, "birthday");
            fail(xstream.toXML(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Java Map集合转XML
     *
     * @author alan
     * @date 2014-6-26 下午3:48:29
     */
    // @Test
    public void writeMap2XML() {
        try {
            failRed("---------Map --> XML---------");
            Map<String, Student> map = new HashMap<String, Student>();
            map.put("No.1", bean);// put
            bean = new Student();
            bean.setAddress("china");
            bean.setEmail("tom@125.com");
            bean.setId(2);
            bean.setName("tom");
            Birthday day = new Birthday("2010-11-22");
            bean.setBirthday(day);
            map.put("No.2", bean);// put
            bean = new Student();
            bean.setName("jack");
            map.put("No.3", bean);// put
            xstream.alias("student", Student.class);
            xstream.alias("key", String.class);
            xstream.useAttributeFor(Student.class, "id");
            xstream.useAttributeFor("birthday", String.class);
            fail(xstream.toXML(map));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 用OutStream输出流写XML
     *
     * @author alan
     * @date 2014-6-26 下午3:48:11
     */
    // @Test
    public void writeXML4OutStream() {
        try {
            out = xstream.createObjectOutputStream(System.out);
            Student stu = new Student();
            stu.setName("jack");
            failRed("---------ObjectOutputStream # JavaObject--> XML---------");
            out.writeObject(stu);
            out.writeObject(new Birthday("2010-05-33"));
            out.write(22);// byte
            out.writeBoolean(true);
            out.writeFloat(22.f);
            out.writeUTF("hello");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // xml内容转java对象

    /**
     * 用InputStream将XML文档转换成java对
     *
     * @author alan
     * @date 2014-6-26 下午3:51:42
     */
//	@Test
    public void readXML4InputStream() {
        try {
            String s = "<object-stream><xml.Student><id>0</id><name>jack</name>" + "</xml.Student><xml.Birthday><birthday>2010-05-33</birthday>"
                    + "</xml.Birthday><byte>22</byte><boolean>true</boolean><float>22.0</float>" + "<string>hello</string></object-stream>";
            failRed("---------ObjectInputStream## XML --> javaObject---------");
            StringReader reader = new StringReader(s);
            in = xstream.createObjectInputStream(reader);
            Student stu = (Student) in.readObject();
            Birthday b = (Birthday) in.readObject();
            byte i = in.readByte();
            boolean bo = in.readBoolean();
            float f = in.readFloat();
            String str = in.readUTF();
            System.out.println(stu);
            System.out.println(b);
            System.out.println(i);
            System.out.println(bo);
            System.out.println(f);
            System.out.println(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将XML字符串转换成Java对象
     *
     * @author alan
     * @date 2014-6-26 下午3:59:26
     */
    @Test
    public void readXml2Object() {
        try {
            failRed("-----------Xml >>> Bean--------------");
            Student stu = (Student) xstream.fromXML(xstream.toXML(bean));
            fail(stu.toString());
            List<Student> list = new ArrayList<Student>();
            list.add(bean);// add
            Map<String, Student> map = new HashMap<String, Student>();
            map.put("No.1", bean);// put
            bean = new Student();
            bean.setAddress("china");
            bean.setEmail("tom@125.com");
            bean.setId(2);
            bean.setName("tom");
            Birthday day = new Birthday("2010-11-22");
            bean.setBirthday(day);
            list.add(bean);// add
            map.put("No.2", bean);// put
            bean = new Student();
            bean.setName("jack");
            list.add(bean);// add
            map.put("No.3", bean);// put
            failRed("==========XML >>> List===========");
            List<Student> studetns = (List<Student>) xstream.fromXML(xstream.toXML(list));
            fail("size:" + studetns.size());// 3
            for (Student s : studetns) {
                fail(s.toString());
            }
            failRed("==========XML >>> Map===========");
            Map<String, Student> maps = (Map<String, Student>) xstream.fromXML(xstream.toXML(map));
            fail("size:" + maps.size());// 3
            Set<String> key = maps.keySet();
            Iterator<String> iter = key.iterator();
            while (iter.hasNext()) {
                String k = iter.next();
                fail(k + ":" + map.get(k));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
