package com.vi.dao;

import com.vi.vo.Person;
import com.vi.vo.User;

import java.util.List;

public interface UserDao {

    public List<User> getUserList();

    public List<User> getUserList2();

    public void insertPerson(Person person);


}