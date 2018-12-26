package water.ustc.service.impl;

import water.ustc.dao.UserDao;
import water.ustc.pojo.UserBean;
import water.ustc.service.UserService;

public class UserServiceImpl implements UserService {

    public UserDao userDao;
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean checkLogin(UserBean user) throws Exception {
        UserBean userBean = userDao.findUserByName(user.getUserName());
        if (userBean != null){
            if (user.getUserPassword().equals(userBean.getUserPassword())){
                return true;
            }
        }
        return false;
    }
}
