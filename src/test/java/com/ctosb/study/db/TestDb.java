package com.ctosb.study.db;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;

public class TestDb {
    private String url, user, pwd;
    private Connection conn;
    private DatabaseMetaData dbmd;

    @Before
    public void setUp() throws Exception {
        url = "jdbc:mysql://localhost:3306/test";
        user = "root";
        pwd = "lxy123";
        // 加载驱动，这一句也可写为：Class.forName("com.mysql.jdbc.Driver");
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        // 建立到MySQL的连接
        conn = DriverManager.getConnection(url, user, pwd);
        dbmd = conn.getMetaData();
    }

    @Test
    public void testTable() {

        try {

            String[] types = {"TABLE"};
            ResultSet rs = dbmd.getTables(null, null, "%", types);
            System.out.println("------------------测试表start-----------------------");
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME"); // 表名
                String tableType = rs.getString("TABLE_TYPE"); // 表类型
                String remarks = rs.getString("REMARKS"); // 表备注
                System.out.println(tableName + "-" + tableType + "-" + remarks);
            }
            System.out.println("------------------测试表end-----------------------");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testTableRemark() {
        try {
            ResultSet rss = dbmd.getColumns(null, null, "sys_user", null);
            while (rss.next()) {
                System.out.println(rss.getString("COLUMN_NAME"));
                System.out.println(rss.getString("TYPE_NAME"));
                System.out.println(rss.getString("REMARKS"));
            }
            conn.close();
        } catch (Exception ex) {
            System.out.println("Error : " + ex.toString());
        }
    }

    @Test
    public void testSql() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from sys_user");
            ResultSetMetaData rsmd = rs.getMetaData();
            System.out.println("------------------测试sql字段start-----------------------");
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                System.out.println(rsmd.getColumnName(i));
            }
            System.out.println("------------------测试sql字段end--------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {

    }

}
