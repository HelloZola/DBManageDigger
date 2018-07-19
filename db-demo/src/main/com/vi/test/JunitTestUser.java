package com.vi.test;

import com.alibaba.fastjson.JSONObject;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
import com.vi.dao.UserDao;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations = {"classpath:conf/spring/spring-*.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class JunitTestUser {

    @Autowired
    private UserDao userDao;

    @Test
    public void test1() {
        System.out.println("*********************************************");
        System.out.println(JSONObject.toJSONString(userDao.getUserList()));
        System.out.println("*********************************************");
    }

    @Test
    public void test2() {
        System.out.println("*********************************************");
        System.out.println(JSONObject.toJSONString(userDao.getUserList2()));
        System.out.println("*********************************************");
    }
}