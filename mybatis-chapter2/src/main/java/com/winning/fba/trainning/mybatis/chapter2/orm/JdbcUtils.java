package com.winning.fba.trainning.mybatis.chapter2.orm;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.winning.fba.trainning.mybatis.chapter2.orm.config.PropertiesConfig;
import com.winning.fba.trainning.mybatis.chapter2.orm.config.PropertiesService;
import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * JDBC工具类
 * 1、封装获取数据库连接，关闭数据库连接方法
 * 2、提供访问数据库的基本方法.
 *
 * @author Smart Chow
 */
public class JdbcUtils {
    private static Logger logger = LoggerFactory.getLogger(JdbcUtils.class);

    private static DataSource dataSource = new ComboPooledDataSource();
    private static QueryRunner queryRunner = new QueryRunner(dataSource);
    private static PropertiesConfig propertiesConfig = PropertiesService.getApplicationConfig();

    /**
     * 获取数据库连接对象.
     * @return
     */
    public static Connection getConn() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            logger.error("获取Connection对象出现异常.",e);
            throw new RuntimeException("获取Connection对象出现异常");
        }

    }

    public static QueryRunner getQueryRunner(){
        return queryRunner;
    }
}
