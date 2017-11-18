package com.ctosb.study.dbutils;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Operation {

    private static String url = "jdbc:mysql://192.168.0.239:3306/ncf_transfer";
    private static String user = "root1";
    private static String password = "sinoservices@2015";
    private static int len;

    static {
        DbUtils.loadDriver("com.mysql.jdbc.Driver");
    }

    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            return conn;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static Connection getConnection(String url, String user,
                                           String password) {
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            return conn;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        Connection conn = getConnection();
        byte[] tmp = new byte[1024];
        QueryRunner runner = new QueryRunner();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 读取文件信息，并保存到字节数组
        FileInputStream fis = new FileInputStream("c:\\Users\\Alan\\Desktop\\T0000000002.pdf");
        while ((len = fis.read(tmp, 0, 1024)) != -1) {
            baos.write(tmp, 0, len);
        }
        //插入文件字节数组信息到表中
        runner.update(conn, "insert into file_test(content) values(?)", baos.toByteArray());

        // 查询文件，并生成文件信息
        List<byte[]> list = runner.query(conn, "select content from file_test", new ColumnListHandler<byte[]>("content"));
        int i = 0;
        for (byte[] bytes : list) {
            OutputStream os = new FileOutputStream("c:\\Users\\Alan\\Desktop\\" + (i++) + ".pdf");
            os.write(bytes);
        }

    }
}
