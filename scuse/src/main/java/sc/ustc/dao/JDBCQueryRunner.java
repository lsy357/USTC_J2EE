package sc.ustc.dao;

import sc.ustc.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class JDBCQueryRunner {

    private static Connection conn;
    private static PreparedStatement ps;
    private static ResultSet rs;
    static{
        conn = DBUtil.openDBConnection();
    }
    public static List<Object> query(String sql, String... columns){
        try {
            List<Object> list = new ArrayList<>(0);
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
                for (int i = 0; i < columns.length; i++){
                    list.add(rs.getObject(i + 1));
                }
            }
            return list;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally{
            DBUtil.closeDBConnection(conn, ps, rs);
        }
        return null;
    }

}
