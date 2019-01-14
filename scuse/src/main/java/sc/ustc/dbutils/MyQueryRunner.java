package sc.ustc.dbutils;

import sc.ustc.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

public class MyQueryRunner {
    private static Connection conn;
    private static PreparedStatement ps;
    private static ResultSet rs;

    static {
        conn = DBUtil.openDBConnection();
    }

    public static int update(String sql, Object... params) {
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            return ps.executeUpdate();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            DBUtil.closeDBConnection(conn, ps, null);
        }
        return 0;
    }

    public static <T> T query(String sql, AbstractResultSetHandler<T> rsh, Object... params) {
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            rs = ps.executeQuery();
            return rsh.handle(rs);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            DBUtil.closeDBConnection(conn, ps, rs);
        }
        return null;
    }

    public static <T> T lazyLoad(String sql, Map<String, String> lazyMapper, AbstractResultSetHandler<T> rsh, Object... params) {
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            rs = ps.executeQuery();
            return rsh.lazyHandle(rs, lazyMapper);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            DBUtil.closeDBConnection(conn, ps, rs);
        }
        return null;
    }
}