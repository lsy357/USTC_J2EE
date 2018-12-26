import org.junit.Test;
import sc.ustc.ioc.impl.ClassPathXmlApplicationContext;
import water.ustc.action.LoginAction;
import water.ustc.pojo.UserBean;
import water.ustc.service.UserService;

public class IocTest {

    @Test
    public void iocTest(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext();
        ClassPathXmlApplicationContext applicationContext1 = new ClassPathXmlApplicationContext();
        ClassPathXmlApplicationContext applicationContext2 = new ClassPathXmlApplicationContext();
        UserService userService = (UserService) applicationContext.getBean("userService");
        UserBean user = (UserBean) applicationContext.getBean("user");
        UserBean user1 = (UserBean) applicationContext1.getBean("user");
        UserBean user2 = (UserBean) applicationContext2.getBean("user");
        user.setUserName("lsy");
        user.setUserPassword("123");
        System.out.println(user);
        try {
            System.out.println(user.hashCode());
            System.out.println(user1.hashCode());
            System.out.println(user2.hashCode());
            System.out.println(userService.checkLogin(user));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void loginActionTest(){
    }

}
