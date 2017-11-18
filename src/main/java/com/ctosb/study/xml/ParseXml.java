package com.ctosb.study.xml;

import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;
import org.apache.commons.beanutils.BeanUtils;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParseXml {

    public static final String SKU = "sku";
    public static final String ORDER = "order";

    public String json2XML(JSON json) {
        XMLSerializer xmlSerializer = new XMLSerializer();
        return xmlSerializer.write(json);
    }

    public static String xml2JSON(String xml) {
        XMLSerializer xmlSerializer = new XMLSerializer();
        return xmlSerializer.read(xml).toString();
    }

    public static String xml2JSON(File file) {
        XMLSerializer xmlSerializer = new XMLSerializer();
        return xmlSerializer.readFromFile(file).toString();
    }

    public static List<Order> reader(String filename) {
        List<Order> orders = new ArrayList<Order>();
        reader(getRoot(filename), orders);
        return orders;
    }

    public static void reader(Element root, List<Order> orders) {
        Iterator<?> itr = root.elementIterator();
        while (itr.hasNext()) {
            Element sub = (Element) itr.next();
            if (ORDER.equals(sub.getName())) {
                processOrder(sub, orders);
            }
        }
    }

    private static void processOrder(Element e, List<Order> orders) {
        Order order = new Order();
        Iterator<?> orderItr = e.elementIterator();
        while (orderItr.hasNext()) {
            Element proEle = (Element) orderItr.next();
            if (SKU.equals(proEle.getName())) {
                processSku(proEle, order.getSkus());
            } else {
                setProperty(order, proEle.getName(), proEle.getData());
            }
        }
        orders.add(order);
    }

    private static void processSku(Element e, List<Sku> skus) {
        Sku sku = new Sku();
        Iterator<?> skuItr = e.elementIterator();
        while (skuItr.hasNext()) {
            Element proEle = (Element) skuItr.next();
            setProperty(sku, proEle.getName(), proEle.getData());
        }
        skus.add(sku);
    }

    public static void setProperty(Object obj, String propertyName, Object value) {
        try {
            BeanUtils.setProperty(obj, propertyName, value);
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static boolean hasSubElement(Element e) {
        List list = e.elements();
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

    public static void readerByNode(Element e) {
        int nodeCount = e.nodeCount();
        for (int i = 0; i < nodeCount; i++) {
            Node node = e.node(i);
            if (node instanceof Element) {
                readerByNode((Element) node);
            } else if (node instanceof Text) {
                System.out.println(((Text) node).getText());
            } else if (node instanceof Attribute) {
                Attribute attr = (Attribute) node;
                System.out.println(attr.getName() + ":" + attr.getData());
            } else {
                System.out.println("非节点、非文本、非属性。。。。。。");
            }
        }
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
        String filename = ParseXml.class.getResource("test.xml").getPath();
//		 reader(classpath+"xml/test.xml",new Order(),new Sku());
//		 readerByNode(getRoot(filename));
        List<Order> orders = reader(filename);
        System.out.println();
    }

}
