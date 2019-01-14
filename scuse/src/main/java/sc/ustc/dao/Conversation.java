package sc.ustc.dao;

import sc.ustc.pojo.EntityMapper;
import sc.ustc.pojo.IdBean;
import sc.ustc.pojo.PropertyBean;

import java.util.*;

public class Conversation {

    //    懒了，直接按照一个映射文件写
    private static List<String> lazyLoadAttrs = new ArrayList<>(0);
    private static List<String> eagerLoadAttrs = new ArrayList<>(0);

    private static Map<String, Map<String, Object>> mappers = new HashMap<>(0);

    static {
        parseMappers();
    }

    public static Map<String, Map<String, Object>> getMappers() {
        return mappers;
    }

    public static List<String> getLazyLoadAttrs() {
        return lazyLoadAttrs;
    }

    public static List<String> getEagerLoadAttrs() {
        return eagerLoadAttrs;
    }

    /**
     * 将orm对象信息存储到map中，以类名为key，全部orm又存储到一个map中
     * 单个orm的mapping信息：
     * k - table, v - 表名
     * k - id, idBean主键mapping
     * k - properties, v - List<PropertyBean>全部属性mapping
     *
     * @return
     */
    protected static void parseMappers() {

        String table = "";
        String className = "";
        IdBean idBean = null;
        List<PropertyBean> properties = null;

        List<EntityMapper> entityMapperList = ORMConfiguration.getEntityMapperList();
        EntityMapper entityMapper = entityMapperList.get(0);
        Map<String, Object> mapper = new HashMap<String, Object>(0);

        className = entityMapper.getClassName();
        table = entityMapper.getTable();
        idBean = entityMapper.getId();
        properties = entityMapper.getProperties();

        for (PropertyBean property : properties) {
            if ("true".equals(property.getLazy())) {
                lazyLoadAttrs.add(property.getName());
            }
        }

        mapper.put("table", table);
        mapper.put("id", idBean);
        mapper.put("properties", properties);
        mappers.put(className, mapper);
    }

}
