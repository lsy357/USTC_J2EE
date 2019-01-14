package sc.ustc.dbutils;

import org.apache.commons.collections.map.LazyMap;
import sc.ustc.dao.Conversation;
import sc.ustc.pojo.IdBean;
import sc.ustc.pojo.PropertyBean;
import sc.ustc.proxy.LazyObjProxy;
import sc.ustc.utils.CommonUtil;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理结果集，在第一次查询某张表时将该表的mapping存储在mapperChart中，以后查询时直接取；
 * 会有多张表，将每张表的mapping以map的value形式存储，map的key为类名
 *
 * @param <T>
 */
public class MyBeanHandler<T> extends AbstractResultSetHandler<T> {
    private Class<T> classType;

    public MyBeanHandler(Class<T> classType) {
        this.classType = classType;
    }

    /**
     * 返回对应xml映射信息
     *
     * @return
     */
    private Map<String, Object> getMapper() {
        return Conversation.getMappers().get(this.classType.getName());
    }

    @Override
    public T handle(ResultSet rs) throws Exception {
        // TODO Auto-generated method stub
        if (rs.next()) {
            T obj = this.classType.newInstance();
            BeanInfo beanInfo = Introspector.getBeanInfo(this.classType, Object.class);
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor pd : pds) {
                pd.getWriteMethod().invoke(obj, rs.getObject(CommonUtil.getColumnName(pd.getName(), classType, getMapper(), mapperChart)));
            }
            return obj;
        }
        return null;
    }


    @Override
    protected T lazyHandle(ResultSet rs, Map<String, String> lazyMapper) throws Exception {
        if (rs.next()) {
            BeanInfo beanInfo = Introspector.getBeanInfo(this.classType, Object.class);
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();

            LazyObjProxy lazyObjProxy = new LazyObjProxy(lazyMapper);
            T proxy = (T) lazyObjProxy.bind(classType);
            for (PropertyDescriptor pd : pds) {
                if (!Conversation.getLazyLoadAttrs().contains(pd.getName())) {
                    pd.getWriteMethod().invoke(proxy, rs.getObject(CommonUtil.getColumnName(pd.getName(),
                            classType, getMapper(), mapperChart)));
                }

            }
            return proxy;
        }
        return null;
    }

}
