package com.ctosb.study.download;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;


public class TestMoreDownFile {
    public static int threadNum = 20;

    public static void main(String[] args) {
        String sURL = "http://downloadap2.teamviewer.com/download/version_8x/TeamViewer_Setup.exe";
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
            long everySize = (long) Math.ceil(endPos / threadNum);

            File file = new File(sPath + "\\" + sName);
            new Thread(new DownLoadListener(endPos)).start();
            for (int i = 0; i < threadNum; i++) {
                if (i == threadNum - 1) {
                    new Thread(new DownLoadThread(i * everySize, endPos, url, file)).start();
                } else {
                    new Thread(new DownLoadThread(i * everySize, (i + 1) * everySize - 1, url, file)).start();
                }
            }
//			}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
