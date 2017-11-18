package com.ctosb.study.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class HelloWorldRemoteImpl extends UnicastRemoteObject implements HelloWorldRemote {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    protected HelloWorldRemoteImpl() throws RemoteException {
        super();
    }

    @Override
    public String hello() throws RemoteException {
        System.out.println("client invocation");
        return "hello world,rmi!";
    }

}
