package water.ustc.dao;

import sc.ustc.dao.BaseDAO;
import water.ustc.pojo.UserBean;

public abstract class UserDao extends BaseDAO {

    @Override
    public abstract Object query() throws Exception;

    @Override
    public abstract Object findOne(Integer userId) throws Exception;

    @Override
    public abstract boolean insert(Integer userId, UserBean user) throws Exception;

    @Override
    public abstract boolean update(Integer userId, UserBean user) throws Exception;

    @Override
    public abstract boolean delete(Integer userId) throws Exception;

    public abstract UserBean findUserByName(String attrValue) throws Exception;
}
