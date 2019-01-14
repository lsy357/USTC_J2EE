package sc.ustc.dbutils;

import sc.ustc.dao.Conversation;
import sc.ustc.utils.CommonUtil;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyBeanListHandler<T> extends AbstractResultSetHandler<List<T>> {
    private Class<T> classType;

    public MyBeanListHandler(Class<T> classType) {
        // TODO Auto-generated constructor stub
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
    public List<T> handle(ResultSet rs) throws Exception {
        // TODO Auto-generated method stub
        List<T> list = new ArrayList<T>();
        BeanInfo beanInfo = Introspector.getBeanInfo(classType, Object.class);
        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
        while (rs.next()) {
            T obj = this.classType.newInstance();
            for (PropertyDescriptor pd : pds) {
                pd.getWriteMethod().invoke(obj, rs.getObject(CommonUtil.getColumnName(pd.getName(), classType, getMapper(), mapperChart)));
            }
            list.add(obj);
        }
        return list != null ? list : null;
    }

    @Override
    protected List<T> lazyHandle(ResultSet rs, Map<String, String> lazyMapper) throws Exception {
        return null;
    }

}
