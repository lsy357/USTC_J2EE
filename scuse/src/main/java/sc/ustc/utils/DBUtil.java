package sc.ustc.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.commons.dbutils.QueryRunner;
import sc.ustc.dao.JDBCConfiguration;
import sc.ustc.dbutils.MyQueryRunner;
import sc.ustc.pojo.JDBCConfigMapper;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {

    private static DruidDataSource dataSource = new DruidDataSource();
    protected static ThreadLocal<QueryRunner> tl = new ThreadLocal<QueryRunner>();

    /**
     * 获取连接池
     */
    static {
//        try {
//            Properties prop = new Properties();
//            FileInputStream in = new FileInputStream("D:\\workspace\\UseSC\\src\\main\\resources\\config.properties");
//            prop.load(in);
//            dataSource = DruidDataSourceFactory.createDataSource(prop);
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
        JDBCConfigMapper jdbcProperty = JDBCConfiguration.getJdbcProperty();
        dataSource.setDriverClassName(jdbcProperty.getDriver());
        dataSource.setUrl(jdbcProperty.getUrl());
        dataSource.setUsername(jdbcProperty.getUsername());
        dataSource.setPassword(jdbcProperty.getPassword());
    }

//    之后不使用DBUtil的QueryRunner，而是使用自定义实现的MyQueryRunner
    public static QueryRunner getQueryRunner() {
        return new QueryRunner(dataSource);
    }

    public static Connection openDBConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean closeDBConnection(Connection conn, Statement st, ResultSet rs) {
        try {
            if (conn != null) {
                conn.close();
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

}
