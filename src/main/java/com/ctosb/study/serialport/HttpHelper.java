package com.ctosb.study.serialport;

import java.io.*;

public class HttpHelper {

    private InputStream inputStream;
    private OutputStream outputStream;
    private String requestUri;
    private String requestContent;

    HttpHelper(InputStream inputStream, OutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        parseContent();
        parseUri();
    }

    /**
     * 发送数据
     *
     * @param content
     * @author Alan
     * @createTime 2016年1月29日 上午11:15:30
     */
    public void sendData(String content) {
        try {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: text/html;charset=utf-8");
            writer.println("Content-Length:" + content.length());
            writer.println(); // 分开响应头和正文
            writer.println(content);
            writer.flush();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析内容
     *
     * @author Alan
     * @createTime 2016年1月29日 上午11:10:41
     */
    private void parseContent() {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            while ((temp = reader.readLine()) != null) {
                stringBuffer.append(temp);
                if (temp.length() == 0) {
                    // 防止死循环
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        requestContent = stringBuffer.toString();
    }

    /**
     * 解析uri
     *
     * @author Alan
     * @createTime 2016年1月29日 上午11:10:32
     */
    private void parseUri() {
        if (requestContent == null || "".equals(requestContent)) {
            return;
        }
        int fmIndex = requestContent.indexOf(" ");
        int toIndex = requestContent.indexOf(" ", fmIndex + 1);
        if (fmIndex == -1 || toIndex == -1) {
            return;
        }
        requestUri = requestContent.substring(fmIndex + 1, toIndex);
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getRequestContent() {
        return requestContent;
    }

    public void setRequestContent(String requestContent) {
        this.requestContent = requestContent;
    }
}
