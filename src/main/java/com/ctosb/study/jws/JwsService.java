package com.ctosb.study.jws;

import javax.jws.Oneway;
import javax.jws.WebService;

@WebService
public interface JwsService {

    public int add(int m, int n);

    //	@Oneway
    public int del(int m, int n);

    @Oneway
    public void print(String str);
}
