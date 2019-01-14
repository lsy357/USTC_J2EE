import org.junit.Test;
import water.ustc.dao.impl.UserDaoImpl;
import water.ustc.pojo.UserBean;

import java.util.List;

public class UserDao {

    private water.ustc.dao.UserDao userDao = new UserDaoImpl();

    @Test
    public void queryAllTest() {
        try {
            List<UserBean> userBeans = (List<UserBean>) userDao.query();
            System.out.println(userBeans);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void findOneTest() {
        UserBean userBean = null;
        try {
            userBean = (UserBean) userDao.findOne(1);
            System.out.println(userBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void insertTest() {
        UserBean user = new UserBean();
        user.setUserId(2);
        user.setUserName("2222");
        user.setUserPassword("2222");
        try {
            userDao.insert(user.getUserId(), user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateTest() {
        UserBean user = new UserBean();
        user.setUserId(2);
        user.setUserName("55555");
        user.setUserPassword("55555");
        try {
            userDao.update(user.getUserId(), user);
            queryAllTest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteTest() {
        try {
            userDao.delete(3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void findByAttrTest() {
        try {
            System.out.println((UserBean) userDao.findUserByName("lsy"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
