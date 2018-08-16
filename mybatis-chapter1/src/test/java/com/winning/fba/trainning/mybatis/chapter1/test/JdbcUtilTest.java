package com.winning.fba.trainning.mybatis.chapter1.test;

import com.winning.fba.trainning.mybatis.chapter1.orm.JdbcUtils;
import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.assertTrue;

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
