package com.ctosb.study.download;

import java.text.DecimalFormat;

public class DownLoadListener implements Runnable {

    private long length;

    public DownLoadListener(long length) {
        this.length = length;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        DecimalFormat df = new DecimalFormat("0.00");
        while (DownLoadThread.LENGTH != length) {
            if (DownLoadThread.LENGTH == 0) {
                System.out.println("已完成" + df.format((DownLoadThread.LENGTH * 100.0 / length)) + "%");
            }
        }
        System.out.println("已完成" + df.format((DownLoadThread.LENGTH * 100.0 / length)) + "%");
    }

}
