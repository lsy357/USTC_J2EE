package sc.ustc.utils;

import sc.ustc.pojo.IdBean;
import sc.ustc.pojo.PropertyBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonUtil {

    public static String getColumnName(String varName, Class classType, Map<String, Object> mapper, Map<String, Map<String, String>> mapperChart) {
        Map<String, String> map = mapperChart.get(classType.getName());
        if (map == null) {
//            添加主键对应信息
            map = new HashMap<>(0);
            IdBean id = (IdBean) mapper.get("id");
            map.put(id.getName(), id.getColumn());
//            添加非主键属性对应信息
            List<PropertyBean> properties = (List<PropertyBean>) mapper.get("properties");
            for (PropertyBean property : properties) {
                map.put(property.getName(), property.getColumn());
            }
            mapperChart.put(classType.getName(), map);
        }
        return map.get(varName);
    }

}
