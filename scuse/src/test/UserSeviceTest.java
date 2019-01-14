import org.junit.Test;
import water.ustc.pojo.UserBean;
import water.ustc.service.UserService;
import water.ustc.service.impl.UserServiceImpl;

public class UserSeviceTest {

    UserService userService = new UserServiceImpl();

    @Test
    public void checkTest() {
        UserBean userBean = new UserBean();
        userBean.setUserName("lsy");
        userBean.setUserPassword("123");
        try {
            System.out.println(userService.checkLogin(userBean));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
