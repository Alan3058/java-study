package com.ctosb.study.rmi;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiServer {

    public static void main(String[] args) {
        try {
            HelloWorldRemote hello = new HelloWorldRemoteImpl();
            Registry reg = LocateRegistry.createRegistry(1099);
            Naming.rebind("//localhost/hello", hello);
            System.out.println("server ok");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
