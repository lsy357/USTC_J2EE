package sc.ustc.dao;

import org.dom4j.Element;
import sc.ustc.pojo.EntityMapper;
import sc.ustc.pojo.IdBean;
import sc.ustc.pojo.PropertyBean;
import sc.ustc.utils.IntrospectorUtil;
import sc.ustc.utils.XMLUtil;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ORMConfiguration {

    private final static List<File> mappingConfigs = new ArrayList<File>(0);
    private static List<EntityMapper> entityMapperList = new ArrayList<EntityMapper>(0);

    static {
        List<String> resources = JDBCConfiguration.getMappingResources();
        for (int i = 0; i < resources.size(); i++){
            mappingConfigs.add(new File(ORMConfiguration.class.getResource(resources.get(i)).getFile()));
        }
        try {
            parseEntityMapper();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取解析的EntityMapper对象
     * @return
     */
    public static List<EntityMapper> getEntityMapperList(){
        return entityMapperList;
    }

    /**
     * 将xml的ormMapping封装为EntityMapper对象
     */
    private static void parseEntityMapper() throws Exception {
        for (File mappingConfig : mappingConfigs){

//            class节点
            Element classElement = XMLUtil.getRootElement(mappingConfig).element("class");

//            封装entity
            EntityMapper entityMapper = new EntityMapper();
            String packageName = classElement.attributeValue("package");
            String className = classElement.element("name").attributeValue("value");
            entityMapper.setClassName(packageName + "." +className);
            entityMapper.setTable(classElement.element("table").attributeValue("value"));
            entityMapper.setId(parseId(classElement));
            entityMapper.setProperties(parseProperties(mappingConfig));
            entityMapperList.add(entityMapper);
        }
    }

    /**
     * 解析id节点
     * @param classElement
     * @return
     */
    protected static IdBean parseId(Element classElement){
        Map<String, String> id = new HashMap<String, String>(0);
        Element idElement = classElement.element("id");
        return new IdBean(idElement.attributeValue("name"), idElement.attributeValue("column"));
    }

    /**
     * 解析全部property节点
     * @param mappingConfig
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    protected static List<PropertyBean> parseProperties(File mappingConfig) throws InvocationTargetException, IllegalAccessException {
        IntrospectorUtil introspectorUtil = new IntrospectorUtil();
        List<Element> propertyElements = XMLUtil.getThirdLevelElements(mappingConfig, "class", "property");
        List<PropertyBean> propertyBeans = new ArrayList<PropertyBean>(0);
        for (Element propertyElement : propertyElements){
            Map<String, Object> map = new HashMap<String, Object>(0);
            PropertyBean propertyBean = new PropertyBean();
            map = XMLUtil.getElementsAttrs(propertyElement);
            introspectorUtil.callSetter(propertyBean, map);
            propertyBeans.add(propertyBean);
        }
        return propertyBeans;
    }

}
