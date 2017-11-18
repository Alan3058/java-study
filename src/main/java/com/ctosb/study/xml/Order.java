package com.ctosb.study.xml;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private String orderNo;
    private String customer;
    private String telphone;
    private String origin;
    private String destination;
    private List<Sku> skus;

    public Order() {
        skus = new ArrayList<Sku>();
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public List<Sku> getSkus() {
        return skus;
    }

    public void setSkus(List<Sku> skus) {
        this.skus = skus;
    }

    public String toString() {
        return orderNo + " " + customer + " " + telphone + " " + origin + " " + destination;
    }

}
