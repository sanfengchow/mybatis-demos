package com.winning.fba.trainning.mybatis.chapter2.orm;

import com.winning.fba.trainning.mybatis.chapter2.orm.annotation.Table;
import com.winning.fba.trainning.mybatis.chapter2.orm.config.PropertiesConfig;
import com.winning.fba.trainning.mybatis.chapter2.orm.config.PropertiesService;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 通用数据库访问层
 * 将java pojo对象映射成数据库字段
 *
 * @author Smart Chow
 */
public class GeneralDao<T> {

    private static Logger logger = LoggerFactory.getLogger(GeneralDao.class);

    private static PropertiesConfig propertiesConfig = PropertiesService.getApplicationConfig();
    /**
     * 添加一个对象
     * @param pojo 要添加的对象
     * @return 添加成功返回 1，否则返回 0
     */
    public int add(T pojo) {
        if (pojo == null) {
            throw new IllegalArgumentException("插入的对象为空.");
        }
        Class clazz = pojo.getClass();
        String tableName = getTableName(clazz);
        Field[] fields = clazz.getDeclaredFields();
        if (fields == null || fields.length == 0) {
            throw new RuntimeException(pojo + "没有属性.");
        }
        String columnsAndValue =  getSqlColumnsAndValues(pojo,fields);
        Object[] params = getSqlParams(pojo, fields);
        String sql = getInsertSql(tableName, columnsAndValue);
        logger.info("insert sql = {}",sql.toString());
        logger.info("insert params = {}",Arrays.toString(params));
        return 1;
//        return  JdbcUtils.excuteUpdate(sql, params);
    }
    public List<T> queryToList(String sql,Object[] params,Class<T> clazz){
        String tableName = getTableName(clazz);
        String idName = "ID";//getIdName(clazz);
        StringBuilder selectSql = new StringBuilder();
        selectSql.append("select * from ").append(tableName).append(" where ").append(idName).append(" = ?");
        List<T> result = null;
        try {
            result = JdbcUtils.getQueryRunner().query(selectSql.toString(), new BeanListHandler<T>(clazz), 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据对象获取sql语句的参数
     * @param element 值对象
     * @param fields 值对象包含的Field
     * @return sql 的参数
     */
    private Object[] getSqlParams(T element, Field[] fields) {
        Object[] params = new Object[fields.length];
        for (int i = 0; i < fields.length; i ++){
            fields[i].setAccessible(true);
            try {
                params[i] = fields[i].get(element);
            } catch (IllegalAccessException e) {
                logger.error("获取{}的属性值失败！,错误信息{}",element,e.getMessage());
            }
        }
        return params;
    }

    /**
     * 根据对象获取sql语句的参数
     * @param element 值对象
     * @param fields 值对象包含的Field
     * @return sql 的参数
     */
    private String  getSqlColumnsAndValues(T element, Field[] fields) {

        StringBuilder sqlColumn = new StringBuilder();
        sqlColumn.append("(");

        StringBuilder sqlColumnValue = new StringBuilder();
        sqlColumnValue.append(" values(");
        String columnPrefix = new StringBuilder("column.").append(element.getClass().getName()).append(".").toString();
        for (int i = 0; i < fields.length; i++) { // 添加参数占位符?
            fields[i].setAccessible(true);
            String fieldName = fields[i].getName();
            String columnName = propertiesConfig.getProperty(columnPrefix + fieldName);
            sqlColumn.append(columnName).append(",");
            sqlColumnValue.append("?,");
        }
        sqlColumn.deleteCharAt(sqlColumn.length()-1);
        sqlColumn.append(")");

        sqlColumnValue.deleteCharAt(sqlColumnValue.length()-1);
        sqlColumnValue.append(")");

        StringBuilder sql = new StringBuilder().append(sqlColumn).append(sqlColumnValue);
        return sql.toString();
    }

    /**
     * 插入对象的sql语句
     * @param tableName 表名称
     * @param length 字段长度
     * @return 插入记录的sql语句
     */
    private String getInsertSql(String tableName, String columnsAndValues) {
        StringBuilder sql = new StringBuilder();
        sql.append("insert into ").append(tableName).append(columnsAndValues);
        return sql.toString();
    }

    /**
     * 根据值对象的注解获取其对应的表名称
     * @param clazz 值对象的字节码
     * @return 表名称
     */
    private String getTableName(Class<T> clazz) {
        boolean existTableAnno = clazz.isAnnotationPresent(Table.class);
        if (!existTableAnno) {
            throw new RuntimeException(clazz + " 没有Table注解.");
        }
        Table tableAnno = (Table)clazz.getAnnotation(Table.class);
        logger.info("当前类名为：{},当前表名为：{}",clazz.getName(),tableAnno.name());
        return tableAnno.name();
    }

}
