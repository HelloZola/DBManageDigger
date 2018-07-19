package com.vi.dao;

import java.util.List;

import com.vi.base.BaseDAO;
import com.vi.mapper.UserMapper;
import com.vi.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl extends BaseDAO implements UserDao {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> getUserList() {
        return userMapper.getUserList();
    }

    public List<User> getUserList2() {
        return getSqlSession().selectList(UserMapper.class.getName() + ".getUserList2");
    }
}
