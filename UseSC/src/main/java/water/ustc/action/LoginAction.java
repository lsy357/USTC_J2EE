package water.ustc.action;

import sc.ustc.container.ApplicationContext;
import sc.ustc.action.Action;
import water.ustc.pojo.UserBean;
import water.ustc.service.UserService;
import water.ustc.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LoginAction extends Action {

    private UserService userService;
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public String loginUI() throws Exception{
        return "success";
    }
	
	public String login() throws Exception {

//	    待优化
        HttpServletRequest request = ApplicationContext.getRequest();
        String userName = request.getParameter("username");
        String userPassword = request.getParameter("userpassword");
        UserBean user = new UserBean();
        user.setUserName(userName);
        user.setUserPassword(userPassword);

        if (userService.checkLogin(user)){
            HttpSession session = ApplicationContext.getRequest().getSession();
            session.setAttribute("user", user);
            return "success";
        }
        return "failure";
	}

	public String dashboard(){
        return "dashboard";
    }

}
