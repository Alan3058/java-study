package com.ctosb.study.xml;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JacksonXml {

    public static final String SKU = "sku";
    public static final String ORDER = "order";

    /**
     * xml转object
     *
     * @param xml
     * @return
     * @author alan
     * @date 2014-6-25 下午5:42:39
     */
    public static Object xml2Object(String xml) {
        JSON json = xml2JSON(xml);
        return JSONObject.toBean((JSONObject) json);
    }

    /**
     * object转xml
     *
     * @param object
     * @return
     * @author alan
     * @date 2014-6-25 下午5:42:25
     */
    public static String object2XML(Object object) {
        return json2XML(JSONObject.fromObject(object));
    }

    public static String list2XML(List list) {
        return json2XML(JSONArray.fromObject(list));
    }

    /**
     * json转XML
     *
     * @param json
     * @return
     * @author alan
     * @date 2014-6-25 下午5:42:07
     */
    public static String json2XML(JSON json) {
        XMLSerializer xmlSerializer = new XMLSerializer();
        xmlSerializer.setRootName("root");
        xmlSerializer.setTypeHintsEnabled(false);
        xmlSerializer.setElementName("Order");

        return xmlSerializer.write(json, "utf-8");
    }

    /**
     * xml转换成json
     *
     * @param xml
     * @return
     * @author alan
     * @date 2014-6-25 下午5:40:44
     */
    public static JSON xml2JSON(String xml) {
        XMLSerializer xmlSerializer = new XMLSerializer();
        return xmlSerializer.read(xml);
    }

    /**
     * xml文件转换成json
     *
     * @param file
     * @return
     * @author alan
     * @date 2014-6-25 下午5:39:02
     */
    public static JSON xml2JSON(File file) {
        XMLSerializer xmlSerializer = new XMLSerializer();
        StringBuffer sb = new StringBuffer();
        try {
            //设置读取文件的编码格式
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "utf-8");
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //避免乱码
//		return xmlSerializer.readFromFile(file);
        return xmlSerializer.read(sb.toString());
    }

    public static boolean hasSubElement(Element e) {
        List<?> list = e.elements();
        if (list == null || list.size() <= 0) {
            return false;
        }
        return true;
    }

    public static Element getRoot(String filename) {
        SAXReader reader = new SAXReader();
        Document doc = null;
        try {
            doc = reader.read(new File(filename));
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return doc == null ? null : getRoot(doc);
    }

    public static Element getRoot(Document doc) {
        return doc.getRootElement();
    }

    public static void writer(String filename, Document doc) {
        try {
            // 紧凑的格式
            // OutputFormat format = OutputFormat.createCompactFormat();
            //排版缩进的格式
            OutputFormat format = OutputFormat.createPrettyPrint();
            // 设置编码
            format.setEncoding("UTF-8");
            // 创建XMLWriter对象,指定了写出文件及编码格式
            XMLWriter writer = new XMLWriter(new FileWriter(new File(filename)), format);
            writer.write(doc);
            // 刷新使字符立即写入文件
            writer.flush();
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        String filename = JacksonXml.class.getResource("test.xml").getPath();
        JSON json = xml2JSON(new File(filename));
//		System.out.println(json.toString());
//		System.out.println(json2XML(json));
        List<Order> lists = new ArrayList<Order>();
        Order order = new Order();
        order.setCustomer("ddd");
        order.setOrigin("dsfs");
        lists.add(order);
        order = new Order();
        order.setCustomer("123wq");
        order.setOrigin("dsfvx");
        ObjectMapper mapper = new ObjectMapper();
        try {
            String s = mapper.writeValueAsString(lists);
            System.out.println(s);
        } catch (JsonGenerationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//		System.out.println(list2XML(lists));
//		XStream xstream = new XStream();
//		String xml = xstream.toXML(lists);
//		System.out.println(xml);


    }

}
