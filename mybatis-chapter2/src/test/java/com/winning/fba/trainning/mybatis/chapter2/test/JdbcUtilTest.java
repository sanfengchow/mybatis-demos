package com.winning.fba.trainning.mybatis.chapter2.test;

import com.winning.fba.trainning.mybatis.chapter2.orm.JdbcUtils;
import org.junit.Test;

import java.sql.Connection;

/**
 * Unit test for simple App.
 */
public class JdbcUtilTest
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void getConnection()
    {
        Connection connection = JdbcUtils.getConn();
        System.out.print(connection);
    }
}
