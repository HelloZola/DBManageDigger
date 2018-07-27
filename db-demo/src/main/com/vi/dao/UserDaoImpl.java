package com.vi.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vi.base.BaseDAO;
import com.vi.common.utils.TimeUtil;
import com.vi.drds.DrdsTransaction;
import com.vi.mapper.UserMapper;
import com.vi.vo.Person;
import com.vi.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
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

    @Override
    public void insertPerson(Person person) {
        getSqlSession().insert(UserMapper.class.getName() + ".insertPerson", person);
    }

    @Override
    @DrdsTransaction(transactionCheck = false)
    @Transactional(rollbackFor = Exception.class)
    public void transactionTest(Person person1, Person person2) {
        Map result = new HashMap();
        int i = getSqlSession().insert(UserMapper.class.getName() + ".insertPerson", person1);
        int i2 = getSqlSession().insert(UserMapper.class.getName() + ".insertPerson", person2);
       /* result.put("i",i+i2);
        return result;*/
    }

    @Override
    @DrdsTransaction(transactionCheck = true)
    @Transactional(rollbackFor = Exception.class)
    public int transactionTest2() {
        System.out.println(this.testInst());

        Person person = new Person();
        person.setName("chen"+ TimeUtil.getNow(TimeUtil.DATE_TIME_FORMAT));
        person.setAge("10");

        Person person2 = new Person();
        person2.setName("chen"+ TimeUtil.getNow(TimeUtil.DATE_TIME_FORMAT));
        person2.setAge("age");
        int i = getSqlSession().insert(UserMapper.class.getName() + ".insertPerson", person);
        int i2 = getSqlSession().insert(UserMapper.class.getName() + ".insertPerson", person2);
        System.out.println("进入了transactionTest2");
        return (i+i2);
    }

    @Override
    @DrdsTransaction(transactionCheck = true)
    @Transactional(rollbackFor = Exception.class)
    public void transactionTest() {

        System.out.println(this.testInst());

        Map result = new HashMap();
        Person person = new Person();
        person.setName("chen"+ TimeUtil.getNow(TimeUtil.DATE_TIME_FORMAT));
        person.setAge("10");

        Person person2 = new Person();
        person2.setName("chen"+ TimeUtil.getNow(TimeUtil.DATE_TIME_FORMAT));
        person2.setAge("age");
        int i = getSqlSession().insert(UserMapper.class.getName() + ".insertPerson", person);
        int i2 = getSqlSession().insert(UserMapper.class.getName() + ".insertPerson", person2);
    }


    private int testInst(){
        return 9;
    }


}
