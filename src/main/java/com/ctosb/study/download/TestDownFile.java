package com.ctosb.study.download;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;


public class TestDownFile {
    public static void main(String[] args) {
        String sURL = "http://downloadap2.teamviewer.com/download/version_8x/TeamViewer_Setup.exe";
        long startPos = 0;
        int readLength = 0;
        String sName = "inst.exe";
        String sPath = "e:\\";
        try {
            URL url = new URL(sURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            Map<String, List<String>> map = conn.getHeaderFields();
            for (String key : map.keySet()) {
                System.out.println(key + ":" + map.get(key));
            }
            long endPos = conn.getContentLength();
            InputStream input = conn.getInputStream();
            RandomAccessFile file = new RandomAccessFile(sPath + "\\" + sName, "rw");
            byte[] b = new byte[1024 * 10];
            while ((readLength = input.read(b, 0, 1024 * 10)) > 0 && startPos < endPos) {

                file.write(b, 0, readLength);
                startPos += readLength;
                DecimalFormat df = new DecimalFormat("0.00");
                System.out.println("已完成" + df.format((startPos * 100.0 / endPos)) + "%");
            }
            input.close();
            file.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
