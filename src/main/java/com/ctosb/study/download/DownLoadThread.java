package com.ctosb.study.download;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownLoadThread implements Runnable {

    private long startPos;
    private long endPos;
    private File file;
    private URL url;
    private BufferedInputStream input;
    public static long LENGTH;

    public DownLoadThread(long startPos, long endPos, URL url, File file) {
        this.startPos = startPos;
        this.endPos = endPos;
        this.file = file;
        this.url = url;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        int readLength = 0;
        byte[] b = new byte[1024 * 10];
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setAllowUserInteraction(true);
            conn.setRequestProperty("Range", "bytes=" + startPos + "-" + endPos);
            input = new BufferedInputStream(conn.getInputStream());
            RandomAccessFile ra = new RandomAccessFile(file, "rw");
            ra.seek(startPos);
            while ((readLength = input.read(b, 0, 1024 * 10)) > 0 && startPos < endPos) {
                synchronized (this) {
                    ra.write(b, 0, readLength);
                    LENGTH += readLength;
                }
                startPos += readLength;
            }
            input.close();
            ra.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

}
