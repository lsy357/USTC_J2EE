package water.ustc.service;

import water.ustc.pojo.UserBean;

public interface UserService {
    boolean checkLogin(UserBean user) throws Exception;
}
