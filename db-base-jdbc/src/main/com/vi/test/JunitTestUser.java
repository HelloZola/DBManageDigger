package com.vi.test;

import com.vi.utils.JDBCUtil;
import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Test
    public void test3() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        conn = JDBCUtil.getConnection();
        try {
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            //准备sql操作语句
            String sql = "insert into person(name,age)values('wang','432')";
            int i = stmt.executeUpdate(sql);
            System.out.println("count:"+i);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn, stmt, rs);
        }
    }

    @Test
    public void test4() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        conn = JDBCUtil.getConnection();
        System.out.println("hashcode:"+conn.getClass().hashCode());
        try {
            System.out.println(conn.getAutoCommit());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            conn.setAutoCommit(false);
//            stmt = conn.prepareStatement("insert into person(name,age)values('zhang','11')");
            stmt = conn.prepareStatement("update person set name='chenxia' where age='12'");
            //准备sql操作语句
            int i = stmt.executeUpdate();
            conn.commit();
            System.out.println("count:"+i);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn, stmt, rs);
        }
    }

    @Test
    public void test5(){
        for(int i=0;i<10;i++){
            new Thread(new Runnable() {
                public void run() {
                    System.out.println("begin...");
                    test4();
                    System.out.println("end...");
                }
            }).start();
        }
    }

    public static void main(String[] args) {
        new JunitTestUser().test5();
    }

}
