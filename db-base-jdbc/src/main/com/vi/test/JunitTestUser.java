package com.vi.test;

import com.vi.utils.JDBCUtil;
import org.junit.Test;

import java.sql.*;

public class JunitTestUser {

    @Test
    public void test1() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        conn = JDBCUtil.getConnection();
        try {
            stmt = conn.createStatement();
            //准备sql操作语句
            String sql = "select name,age from person";
            rs = stmt.executeQuery(sql);
            //从结果集中提取数据
            while (rs.next()) {
                String name = rs.getString("name");
                String age = rs.getString("age");
                System.out.println("********************");
                System.out.println("name: " + name);
                System.out.println("age: " + age);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn, stmt, rs);
        }
    }

    @Test
    public void test2() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        conn = JDBCUtil.getConnection();
        try {
            stmt = conn.prepareStatement("select name,age from person");
            //准备sql操作语句
            rs = stmt.executeQuery();
            //从结果集中提取数据
            while (rs.next()) {
                String name = rs.getString("name");
                String age = rs.getString("age");
                System.out.println("********************");
                System.out.println("name: " + name);
                System.out.println("age: " + age);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn, stmt, rs);
        }
    }

}
