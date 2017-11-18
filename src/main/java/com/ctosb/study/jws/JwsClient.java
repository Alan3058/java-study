package com.ctosb.study.jws;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;


public class JwsClient {

    /**
     * @param args
     * @author Alan
     * @time 2015-10-19 下午09:29:22
     */
    public static void main(String[] args) {
        try {
            URL url = new URL("http://localhost:8888/ws?wsdl");
            QName qName = new QName("http://jws.study.ctosb.com/", "JwsServiceImplService");
            Service service = Service.create(url, qName);
            //获取webservice服务端的类
            JwsService jwsService = service.getPort(JwsService.class);
            System.out.println(jwsService.add(1, 2));
            System.out.println(jwsService.del(19, 11));
            jwsService.print("hello");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
