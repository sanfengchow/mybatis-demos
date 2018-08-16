package com.winning.fba.trainning.mybatis.chapter2.test;

import com.winning.fba.trainning.mybatis.chapter2.domain.User;
import com.winning.fba.trainning.mybatis.chapter2.orm.GeneralDao;
import org.junit.Test;

import java.util.Date;
import java.util.List;


/**
 * Unit test for simple App.
 */
public class GeneralDaoTest {

    @Test
    public void queryToList() {
        GeneralDao<User> generalDao = new GeneralDao<User>();
        List<User> users = generalDao.queryToList("select * from SPS_USER",new Object[]{},User.class);
        System.out.print(users);
    }
}
