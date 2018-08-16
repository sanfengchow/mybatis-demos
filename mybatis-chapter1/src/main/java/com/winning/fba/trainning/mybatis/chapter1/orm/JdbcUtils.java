package com.winning.fba.trainning.mybatis.chapter1.orm;

import com.winning.fba.trainning.mybatis.chapter1.orm.config.PropertiesConfig;
import com.winning.fba.trainning.mybatis.chapter1.orm.config.PropertiesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final String JDBC_DRIVER = "jdbc.driver";
    private static final String JDBC_URL = "jdbc.url";
    private static final String JDBC_USERNAME = "jdbc.username";
    private static final String JDBC_PASSWORD= "jdbc.password";
    private static PropertiesConfig propertiesConfig = PropertiesService.getApplicationConfig();

    /**
     * 获取数据库连接对象.
     * @return
     */
    public static Connection getConn(){
        Connection conn = null;
        try {
            logger.info("driver:{}",propertiesConfig.getProperty(JDBC_DRIVER));

            Class.forName(propertiesConfig.getProperty(JDBC_DRIVER));
            conn = DriverManager.getConnection(propertiesConfig.getProperty(JDBC_URL), propertiesConfig.getProperty(JDBC_USERNAME),  propertiesConfig.getProperty(JDBC_PASSWORD));
        }catch(Exception e){
            logger.error("获取连接对象失败.",e);
        }
        return conn;
    }

    /**
     * 增删改
     * @param sql 预编译SQL语句
     * @param params 参数
     * @return 受影响的记录数目
     */
    public static int excuteUpdate(String sql, Object[] params){
        Connection connection = null;
        PreparedStatement pstmt =null;
        int result = -1;
        try {
            connection = getConn();
            pstmt = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i ++){
                pstmt.setObject(i+1, convert2JdbcType(params[i]));
            }
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("更新数据出现异常.",e);
        } finally {
            release(pstmt, connection);
        }
        return result;  // 更新数据失败
    }

    private static Object convert2JdbcType (Object javaType)
    {
        if (javaType == null) {
            return javaType;
        }
        if (javaType instanceof java.util.Date) {
            java.util.Date tmpDate = (java.util.Date)javaType;
            return new java.sql.Date(tmpDate.getTime());
        }
        return javaType;
    }
    private static Object convert2JavaType (Object jdbcType)
    {
        if (jdbcType == null) {
            return jdbcType;
        }
        if (jdbcType instanceof java.math.BigDecimal) {
            return ((BigDecimal) jdbcType).longValue();
        }
        if (jdbcType instanceof java.sql.Timestamp) {
            return new java.util.Date(((java.sql.Timestamp)jdbcType).getTime());
        }
        return jdbcType;
    }

    public static List<HashMap<String,Object>> executeQuery (String sql, Object[] params){
        List<HashMap<String,Object>> result = new ArrayList<HashMap<String,Object>>();
        Connection connection = null;
        PreparedStatement pstmt =null;

        try {
            connection = getConn();
            pstmt = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i ++){
                pstmt.setObject(i+1, params[i]);
            }
            ResultSet  rs = pstmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            // 将所有结果输出
            if (rs.next()) {
                HashMap<String,Object> rowData = new HashMap<String,Object>();
                for (int i=1; i<=rsmd.getColumnCount(); i++) {
                    logger.info(rsmd.getColumnLabel(i)+"="+rs.getObject(i)+"TYPE="+rsmd.getColumnClassName(i));
                    rowData.put(rsmd.getColumnLabel(i),convert2JavaType(rs.getObject(i)));
                }
                result.add(rowData);
            }
        } catch (SQLException e) {
            logger.error("查询数据出现异常.",e);
        } finally {
            release(pstmt, connection);
        }
        return result;
    }



    public static void release(Statement stmt, Connection conn) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("关闭statement出现异常.",e);
            }
            stmt = null;
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("关闭connection出现异常.",e);
            }
            conn = null;
        }
    }

}
