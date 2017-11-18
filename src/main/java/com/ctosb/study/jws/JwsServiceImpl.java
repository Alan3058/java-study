package com.ctosb.study.jws;

import javax.jws.WebService;

@WebService(endpointInterface = "com.ctosb.study.jws.JwsService")
public class JwsServiceImpl implements JwsService {

    @Override
    public int add(int m, int n) {
        return m + n;
    }

    @Override
    public int del(int m, int n) {
        return m - n;
    }

    public void print(String str) {
        System.out.println(str);
    }

}
