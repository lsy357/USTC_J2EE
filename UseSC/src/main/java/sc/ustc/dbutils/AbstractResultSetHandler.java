package sc.ustc.dbutils;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractResultSetHandler<T> {
    /**
     * 已建立的mapping，key为实体类名，value为mapping
     */
    protected static Map<String, Map<String, String>> mapperChart = new HashMap<>(0);

    public static Map<String, Map<String, String>> getMapperChart(){
        return mapperChart;
    }
    protected abstract T handle(ResultSet rs) throws Exception;
    protected abstract T lazyHandle(ResultSet rs, Map<String, String> lazyMapper) throws Exception;
}
