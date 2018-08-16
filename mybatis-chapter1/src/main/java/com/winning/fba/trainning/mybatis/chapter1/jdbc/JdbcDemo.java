package com.winning.fba.trainning.mybatis.chapter1.jdbc;

import com.winning.fba.trainning.mybatis.chapter1.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 演示JDBC连接数据库的步骤
 */

public class JdbcDemo {
    private static Logger logger = LoggerFactory.getLogger(JdbcDemo.class);

    public List<User> findUsers ()
    {
        List<User> users = new ArrayList<User>();

        String strCon = "jdbc:sqlserver://192.168.31.101:1433;DatabaseName=SPS";  //连接字符串
        String strUser = "sa";               //数据库用户名
        String strPwd = "123123";                  //口令
        logger.debug("正在连接数据库...");
        try {  //监控异常
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");  //加载驱动程序
            Connection con;
            //获得连接对象
            con = DriverManager.getConnection(strCon, strUser, strPwd);
            logger.debug("成功连接到数据库。");
            Statement sta = con.createStatement();    //创建语句对象
            //执行SQL语句
            String strSql = "SELECT * FROM SPS_USER";
            ResultSet rs = sta.executeQuery(strSql);
            while (rs.next()) {        //循环将结果集游标往下移动，到达末尾返回false
                User user = new User();
                user.setId(rs.getLong("ID"));
                user.setUserName(rs.getString("USER_NAME"));
                user.setHospId(rs.getString("HOSP_ID"));
                user.setUserPwd(rs.getString("USER_PWD"));
                user.setUserType(rs.getString("USER_TYPE"));
                user.setUserStatus(rs.getString("USER_STATUS"));
                user.setExpiryDate(rs.getDate("EXPIRY_DATE"));
                user.setStaffName(rs.getString("STAFF_NAME"));
                user.setStaffCode(rs.getString("STAFF_CODE"));
                users.add(user);
            }

            logger.debug("一共查询到{}条用户信息。",users.size());
            // 必须关闭连接
            sta.close();
            con.close();    //关闭所有已经打开的资源
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return users;
    }

    public static void main(String[] args) {
        JdbcDemo jdbcDemo = new JdbcDemo();
        List<User> users = jdbcDemo.findUsers();
        logger.debug("=======查询结果集{}。",users);
    }
}

