package sc.ustc.interceptor.impl;

import sc.ustc.container.ApplicationContext;
import sc.ustc.interceptor.Interceptor;

import java.util.ArrayList;
import java.util.List;


public class PrivacyInterceptor implements Interceptor {

    protected static List<String> publicURLs = new ArrayList<String>(0);

    /**
     * 设置公共资源，要求设置为servletPath，在preAction调用前设置
     *
     * @param list
     * @return
     */
    public static void setPublicURLs(List<String> list) {
        publicURLs.addAll(list);
    }

    @Override
    public boolean preAction() {
        System.out.println("PrivacyInterceptor afterActionWithNoParams");
        String servletPath = ApplicationContext.getRequest().getServletPath();
        if (publicURLs.contains(servletPath)) {
            return true;
        } else {
            if (ApplicationContext.getRequest().getSession().getAttribute("user") != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void afterAction() throws Exception {
        System.out.println("PrivacyInterceptor afterActionWithNoParams");
    }
}
