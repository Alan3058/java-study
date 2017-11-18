package com.ctosb.study.jws;

import javax.xml.ws.Endpoint;

public class JwsServer {

    /**
     * @param args
     * @author Alan
     * @time 2015-10-19 下午09:26:59
     */
    public static void main(String[] args) {
        //发布服务
        Endpoint.publish("http://localhost:8888/ws", new JwsServiceImpl());
    }

}
