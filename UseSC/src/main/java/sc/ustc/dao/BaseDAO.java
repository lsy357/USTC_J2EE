package sc.ustc.dao;

import lombok.Setter;
import water.ustc.pojo.UserBean;

@Setter
public abstract class BaseDAO {

    public abstract Object query() throws Exception;
    public abstract Object findOne(Integer userId) throws Exception;
    public abstract boolean insert(Integer userId, UserBean user) throws Exception;
    public abstract boolean update(Integer userId, UserBean user) throws Exception;
    public abstract boolean delete(Integer userId) throws Exception;

}
