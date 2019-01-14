package sc.ustc.dao;

import org.apache.commons.beanutils.BeanUtils;
import org.dom4j.Element;
import sc.ustc.pojo.JDBCConfigMapper;
import sc.ustc.utils.XMLUtil;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JDBCConfiguration {

    private final static File jdbcConfig = new File(JDBCConfiguration.class.getResource("/persistenceframe.config.xml").getFile());
    private static JDBCConfigMapper jdbcConfigMapper = new JDBCConfigMapper();
    private static List<String> mappingResources = new ArrayList<String>(0);

    static {
        configure();
    }

    public static JDBCConfigMapper getJdbcProperty() {
        return jdbcConfigMapper;
    }

    public static List<String> getMappingResources() {
        return mappingResources;
    }

    /**
     * 将xml的jdbc property封装为JDBCConfigBean对象
     */
    private static void configure() {
        List<Element> propertyElements = XMLUtil.getThirdLevelElements(jdbcConfig, "jdbc", "property");
        try {
            for (Element propertyElement : propertyElements) {
                Map<String, Object> attrs = XMLUtil.getElementsAttrs(propertyElement);
                BeanUtils.setProperty(jdbcConfigMapper, (String) attrs.get("name"), attrs.get("value"));
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        parseMappingResources();
    }

    /**
     * 获取映射文件位置
     */
    private static void parseMappingResources() {
        List<Element> propertyElements = XMLUtil.getSubElementOfRoot(jdbcConfig, "mapping");
        for (Element propertyElement : propertyElements) {
            mappingResources.add(propertyElement.attributeValue("resource"));
        }
    }

}
