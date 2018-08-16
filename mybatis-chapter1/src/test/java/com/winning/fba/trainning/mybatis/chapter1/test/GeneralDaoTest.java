package com.winning.fba.trainning.mybatis.chapter1.test;

import com.winning.fba.trainning.mybatis.chapter1.domain.User;
import com.winning.fba.trainning.mybatis.chapter1.orm.GeneralDao;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Date;


/**
 * Unit test for simple App.
 */
public class GeneralDaoTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void add() {
        GeneralDao<User> generalDao = new GeneralDao<User>();
        User user = new User();
        user.setId(1l);
        user.setUserName("stephenchow");
        user.setUserPwd("winning211");
        user.setUserStatus("1");
        user.setHospId("HOO1");
        user.setCreateBy("admin");
        user.setUserType("医生");
        user.setStaffCode("0001");
        user.setStaffName("周星星");
        user.setGender("男");
        user.setCheckCode("CHECK001");
        user.setPyCode("zxx");
        user.setWbCode("zkb");
        user.setExpiryDate(new Date());
        user.setCreateDate(new Date());
        user.setMemo("这是第一个添加的账号");
        generalDao.add(user);

    }

    @Test
    public void queryToList() {
        GeneralDao<User> generalDao = new GeneralDao<User>();
        generalDao.queryToList("select * from SPS_USER",new Object[]{},User.class);

    }
}
