package sc.ustc.dbutils;

import java.sql.ResultSet;

public interface ResultSetHandler<T> {
    public T handle(ResultSet rs) throws Exception;
}
