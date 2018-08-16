package com.winning.fba.trainning.mybatis.chapter1.orm;

import com.winning.fba.trainning.mybatis.chapter1.orm.config.PropertiesConfig;
import com.winning.fba.trainning.mybatis.chapter1.orm.config.PropertiesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
        return  JdbcUtils.excuteUpdate(sql, params);
    }
    public List<T> queryToList(String sql,Object[] params,Class<T> clazz){
        List<T> result = new ArrayList<T>();
        List<HashMap<String,Object>> list = JdbcUtils.executeQuery(sql,params);
        String columnPrefix = new StringBuilder("column.").append(clazz.getName()).append(".").toString();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (HashMap<String, Object> rowData :list) {
                T object =  clazz.newInstance();
                logger.info("查询每行数据:{}",rowData);
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    String fieldName = fields[i].getName();
                    String columnName = propertiesConfig.getProperty(columnPrefix + fieldName);
                    if(columnName == null) {
                        continue;
                    }
                    logger.debug("设置数据库列:{}到java对象中",columnName);
                    fields[i].set(object,rowData.get(columnName));
                }
                result.add(object);
            }
        }catch (Exception ex) {
            logger.error("查询数据时出现异常",ex);
        }

        logger.info("一共查询到{}条数据",result.size());
        logger.info("查询到的数据集:{}",result);
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
        String className =  clazz.getName(); //获取类的完整类名
        StringBuilder stringBuilder = new StringBuilder("table.").append(className);

        String tableName = propertiesConfig.getProperty(stringBuilder.toString());
        logger.info("当前类名为：{},当前表名为：{}",className,tableName);
        return tableName;
    }


    /*---------------------------------更新对象到数据库---------------------------------*/
    /**
     * 更新一个对象
     * @param pojo 待更新的对象
     * @return 若成功更新则返回1，否则返回0
     */
/*    public int update(T pojo) {
        if (pojo == null) {
            throw new IllegalArgumentException("插入的元素为空.");
        }
        Class clazz = pojo.getClass();
        Field[] fields = clazz.getDeclaredFields();
        if (fields == null || fields.length == 0)
            throw new RuntimeException(pojo + "没有属性.");
        Object[] params = new Object[fields.length];
        String sql = getUpdateSqlAndParams(pojo, params);
        System.out.println("update sql = " + sql);
        System.out.println("params = " + Arrays.toString(params));
        return JdbcUtils.excuteUpdate(sql, params);
    }

    *//**
     * 获取更新记录的sql语句和参数
     * @param element 对象
     * @param params 参数数组
     * @return update sql 和 sql语句的参数
     *//*
    private String getUpdateSqlAndParams(T element, Object[] params) {
        Class clazz = element.getClass();
        String tableName = getTableName(clazz);
        Field[] fields = clazz.getDeclaredFields();

        StringBuilder updateSql = new StringBuilder();
        updateSql.append("update ").append(tableName).append(" set ");
        String idName = "";
        int index = 0; // 记录参数的位置
        for (int i = 0; i < fields.length; i ++){
            fields[i].setAccessible(true);
            // 找到id对应的列名和值
            if (fields[i].isAnnotationPresent(Id.class)){
                idName = fields[0].getAnnotation(Id.class).name();
                try {
                    params[params.length-1] = fields[i].get(element);  // id作为update sql 的最后一个参数
                    if (params[params.length-1] == null)
                        throw new RuntimeException(element + "没有Id属性!");
                } catch (IllegalAccessException e) {
                    System.out.println(e.getMessage());
                    System.out.println("获取" + element + "的属性值失败！");
                }
            }
            boolean isPresent = fields[i].isAnnotationPresent(Column.class);
            if (isPresent) {
                Column column = fields[i].getAnnotation(Column.class);
                String columnName = column.name();
                updateSql.append(" ").append(columnName).append( " = ? ,");
                // update sql 的参数
                try {
                    params[index++] = fields[i].get(element);  // 添加参数到数组，并更新下标
                } catch (IllegalAccessException e) {
                    System.out.println(e.getMessage());
                    System.out.println("获取" + element + "的属性值失败！");
                }
            }
        }
        updateSql.deleteCharAt(updateSql.length()-1);
        updateSql.append("where ").append(idName).append(" = ?");
        return updateSql.toString();
    }*/
}
