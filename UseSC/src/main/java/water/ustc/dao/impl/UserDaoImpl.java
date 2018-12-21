package water.ustc.dao.impl;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import sc.ustc.utils.DBUtil;
import water.ustc.dao.UserDao;
import water.ustc.pojo.UserBean;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;

public class UserDaoImpl extends UserDao {

    private QueryRunner queryRunner = DBUtil.getQueryRunner();
    String sql = "";

    @Override
    public Object query() throws Exception {
        sql = "select * from user";
        List<UserBean> users = queryRunner.query(sql, new BeanListHandler<UserBean>(UserBean.class));
        return users;
    }

    @Override
    public Object findOne(Integer userId) throws Exception {
        sql = "select * from user where userid=?";
        UserBean userBean = queryRunner.query(sql, new BeanHandler<UserBean>(UserBean.class), userId);
        return userBean;
    }

    @Override
    public boolean insert(Integer userId, UserBean user) throws Exception {
        try {
            sql = "insert into user(userid, username, userpassword) value(?, ?, ?)";
            queryRunner.update(sql, user.getUserId(), user.getUserName(), user.getUserPassword());
            return true;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Integer userId, UserBean user) throws Exception {
        try {
            sql = MessageFormat.format("update user set userid={0}, username={1}, userpassword={2} where userid={0}",
                    user.getUserId(), user.getUserName(), user.getUserPassword());
            queryRunner.update(sql);
            return true;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Integer userId) throws Exception {
        try {
            sql = "delete from user where userid=?";
            queryRunner.update(sql, userId);
            return true;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public UserBean findUserByName(String attrValue) throws Exception {
        String sql = "select * from user where username=?";
        UserBean userBean = queryRunner.query(sql, new BeanHandler<UserBean>(UserBean.class), attrValue);
        return userBean;
    }
}
