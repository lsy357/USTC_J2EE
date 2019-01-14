package sc.ustc.dao;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.lang3.StringUtils;
import sc.ustc.dbutils.AbstractResultSetHandler;
import sc.ustc.dbutils.MyBeanHandler;
import sc.ustc.dbutils.MyBeanListHandler;
import sc.ustc.dbutils.MyQueryRunner;
import sc.ustc.pojo.IdBean;
import sc.ustc.pojo.PropertyBean;
import sc.ustc.utils.DBUtil;
import sc.ustc.utils.IntrospectorUtil;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * session查询类，线程安全
 */
public class Session extends Conversation {

    private Map<String, Map<String, Object>> mappers = Conversation.getMappers();
    private QueryRunner queryRunner = DBUtil.getQueryRunner();
    private String sql = "";
    private Map<String, Object> mapper = new HashMap<>(0);
    private List<String> lazyLoadAttrs = Conversation.getLazyLoadAttrs();
    private List<String> eagerLoadAttrs = Conversation.getEagerLoadAttrs();

    public Object query(Class clazz) throws Exception {
        mapper = mappers.get(clazz.getName());
        sql = MessageFormat.format("select * from {0}", mapper.get("table"));
        List<Object> beans = MyQueryRunner.query(sql, new MyBeanListHandler<Object>(clazz));
        System.out.println(sql);
        return beans;
    }

    /**
     * 延迟加载
     *
     * @param clazz
     * @param userId
     * @return
     * @throws Exception
     */
    public Object load(Class clazz, Integer userId) throws Exception {
        mapper = mappers.get(clazz.getName());
        IdBean idBean = (IdBean) mapper.get("id");
        Map<String, Map<String, String>> mapperChart = AbstractResultSetHandler.getMapperChart();
        Map<String, String> mapping = mapperChart.get(clazz.getName());
        StringBuffer columns = new StringBuffer();
        Map<String, String> lazyMapper = new HashMap<>(0);

        BeanInfo beanInfo = Introspector.getBeanInfo(clazz, Object.class);
        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();

        if (mapping != null) {
            for (PropertyDescriptor pd : pds) {
                if (!lazyLoadAttrs.contains(pd.getName())) {
                    columns.append(mapping.get(pd.getName()) + ",");
                } else {
                    lazyMapper.put(pd.getName(), mapping.get(pd.getName()));
                }
            }
        } else {
            /**
             * 重复逻辑判断，找到mapper中和当前属性一致的属性，待优化
             */
            List<PropertyBean> properties = (List<PropertyBean>) mapper.get("properties");
            for (PropertyDescriptor pd : pds) {
                if (!pd.getName().equals(idBean.getName())) {
                    for (PropertyBean property : properties) {
                        if (property.getName().equalsIgnoreCase(pd.getName())) {
                            if (!lazyLoadAttrs.contains(pd.getName())) {
                                columns.append(property.getColumn() + ",");
                            } else {
                                lazyMapper.put(property.getName(), property.getColumn());
                            }
                            break;
                        }
                    }
                } else {
                    if (!lazyLoadAttrs.contains(pd.getName())) {
                        columns.append(idBean.getColumn() + ",");
                    } else {
                        lazyMapper.put(idBean.getName(), idBean.getColumn());
                    }
                }
            }
        }
        if (!StringUtils.isEmpty(columns)) {
            columns.deleteCharAt(columns.length() - 1);
        }
        sql = MessageFormat.format("select {0} from {1} where {2}={3}", columns, mapper.get("table"),
                ((IdBean) mapper.get("id")).getColumn(), userId);
        String lazySql = MessageFormat.format("from {0} where {1}={2}", mapper.get("table"),
                ((IdBean) mapper.get("id")).getColumn(), userId);

        lazyMapper.put("sql", lazySql);
        Object bean = MyQueryRunner.lazyLoad(sql, lazyMapper, new MyBeanHandler<Object>(clazz));
        System.out.println(sql);
        return bean;
    }

    /**
     * 立即加载
     *
     * @param clazz
     * @param userId
     * @return
     * @throws Exception
     */
    public Object get(Class clazz, Integer userId) throws Exception {
        mapper = mappers.get(clazz.getName());
        sql = MessageFormat.format("select * from {0} where {1}={2}",
                mapper.get("table"), ((IdBean) mapper.get("id")).getColumn(), userId);
        System.out.println(sql);
        Object bean = MyQueryRunner.query(sql, new MyBeanHandler<Object>(clazz));
        return bean;
    }

    public boolean insert(Object bean) throws Exception {
        sql = sql4bean("insert", bean);
        return queryRunnerUpdateBean(sql);
    }

    public boolean update(Object bean) throws Exception {
        sql = sql4bean("update", bean);
        return queryRunnerUpdateBean(sql);
    }

    public boolean queryRunnerUpdateBean(String sql) {
        try {
            System.out.println(sql);
            MyQueryRunner.update(sql);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean delete(Class clazz, Integer userId) throws Exception {
        mapper = mappers.get(clazz.getName());
        try {
            sql = MessageFormat.format("delete from {0} where {1}={2}",
                    mapper.get("table"), ((IdBean) mapper.get("id")).getColumn(), userId);
            System.out.println(sql);
            MyQueryRunner.update(sql);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取bean各属性名，拼接sql
     *
     * @param sqlType
     * @param bean
     * @return
     * @throws Exception
     */
    private String sql4bean(String sqlType, Object bean) throws Exception {

        mapper = mappers.get(bean.getClass().getName());
        Map<String, Map<String, String>> mapperChart = AbstractResultSetHandler.getMapperChart();
        Map<String, String> mapping = mapperChart.get(bean.getClass().getName());

//        insert语句
        StringBuffer tableInfo = new StringBuffer();
        StringBuffer valueInfo = new StringBuffer();
//        update语句
        StringBuffer updateSqlSet = new StringBuffer();
        String updateSqlConstraint = "";

        BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass(), Object.class);
        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
        IdBean idBean = (IdBean) mapper.get("id");

        if (mapping != null) {
//            表明已经在查询时建立好了mapping
            for (PropertyDescriptor pd : pds) {
                if (!pd.getName().equalsIgnoreCase(idBean.getName())) {
//                    拼接insert语句
                    if (sqlType.equals("insert")) {
                        tableInfo.append(mapping.get(pd.getName()) + ",");
                        valueInfo.append(pd.getValue(pd.getName()).toString() + ",");
                    } else {
//                    拼接update语句
                        updateSqlSet.append(mapping.get(pd.getName()) + "=" + pd.getValue(pd.getName()).toString() + ",");
                    }
                } else {
                    if (sqlType.equals("update")) {
//                    拼接update语句
                        updateSqlConstraint = mapping.get(pd.getName()) + "=" + pd.getValue(pd.getName()).toString();
                    }
                }
            }
        } else {
//            没有查询过，所以没有mapping
            IntrospectorUtil<Object> introspectorUtil = new IntrospectorUtil<>();

//            拼接属性
            List<PropertyBean> properties = (List<PropertyBean>) mapper.get("properties");
            for (PropertyDescriptor pd : pds) {
                if (!pd.getName().equals(idBean.getName())) {
                    for (PropertyBean property : properties) {
                        if (property.getName().equalsIgnoreCase(pd.getName())) {
                            if (sqlType.equals("insert")) {
                                tableInfo.append(property.getColumn() + ",");
                                valueInfo.append(introspectorUtil.callGetter(bean, pd.getName()) + ",");
                            } else {
                                updateSqlSet.append(property.getColumn() + "=" + introspectorUtil.callGetter(bean, pd.getName()) + ",");
                            }
                            break;
                        }
                    }
                } else {
                    if (sqlType.equals("update")) {
                        updateSqlConstraint = idBean.getColumn() + "=" + introspectorUtil.callGetter(bean, pd.getName());
                    }
                }
            }
        }

        if (sqlType.equals("insert")) {
            tableInfo.deleteCharAt(tableInfo.length() - 1);
            valueInfo.deleteCharAt(valueInfo.length() - 1);
            return MessageFormat.format("insert into {0}({1}) value({2})",
                    mapper.get("table"), tableInfo, valueInfo);
        } else {
            updateSqlSet.deleteCharAt(updateSqlSet.length() - 1);
            return MessageFormat.format("update {0} set {1} where {2}",
                    mapper.get("table"), updateSqlSet, updateSqlConstraint);
        }
    }

}
