package com.vi.dao;

import com.vi.vo.Person;
import com.vi.vo.User;

import java.util.List;
import java.util.Map;

public interface UserDao {

    public List<User> getUserList();

    public List<User> getUserList2();

    public void insertPerson(Person person);

    public void transactionTest(Person person1, Person person2);

    public void transactionTest();

    public int transactionTest2();
}