package com.ctosb.study.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HelloWorldRemote extends Remote {

    public String hello() throws RemoteException;
}
