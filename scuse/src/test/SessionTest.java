import org.junit.Test;
import sc.ustc.dao.Session;
import sc.ustc.dao.SessionFactory;
import water.ustc.pojo.UserBean;

public class SessionTest {

    @Test
    public void queryAllTest(){
        Session session1 = SessionFactory.getCurrentSession();
        try {
            System.out.println(session1.query(UserBean.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getTest(){
        Session session = SessionFactory.getCurrentSession();
        try {
            System.out.println(session.get(UserBean.class, 2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void loadTest(){
        Session session = SessionFactory.getCurrentSession();
        try {
            UserBean userBean = (UserBean) session.load(UserBean.class, 1);
            System.out.println(userBean);
            userBean.getUserPassword();
            System.out.println(userBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void insertTest(){
        Session session = SessionFactory.getCurrentSession();
        UserBean user = new UserBean();
//        user.setUserId(4);
        user.setUserName("123456");
        user.setUserPassword("123456");
        try {
//            session.update(user);
            session.insert(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteTest(){
        Session session = SessionFactory.getCurrentSession();
        try {
            System.out.println(session.delete(UserBean.class, 4));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
