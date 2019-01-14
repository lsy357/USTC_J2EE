package sc.ustc.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sc.ustc.container.ApplicationContext;
import sc.ustc.exception.MyException;
import sc.ustc.proxy.InterceptorProxy;
import sc.ustc.utils.ControllerUtil;
import sc.ustc.utils.StoneValueUtil;
import org.dom4j.Element;

/**
 * 中心控制器
 */
public class SimpleController extends HttpServlet {

    final static private File controllerXML = StoneValueUtil.controllerXML;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        try {
            initContainer(request, response);
            doDispath(request, response);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            if (e instanceof MyException) {
                request.getRequestDispatcher("/error.html").forward(request, response);
                System.out.println(e.getMessage());
            }
        }
    }

    //    域放入容器
    private void initContainer(HttpServletRequest request, HttpServletResponse response) {
        ApplicationContext.setValue("request", request);
        ApplicationContext.setValue("response", response);
        ApplicationContext.setValue("applicationContext", getServletContext());
    }

    private void doDispath(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> attrMap = new HashMap<String, Object>(0);
        attrMap = ControllerUtil.getHandlerInfo(request, response);
//		获取当前action节点
        Element actionElement = (Element) attrMap.get("element");

        List<String> interceptorNames = new ArrayList<String>();
        if (actionElement.elements("interceptor") != null) {
//			解析action对应interceptor子节点的属性
            List<Element> interceptorRefElements = actionElement.elements("interceptor-ref");

//			获取interceptorsInfo
            for (Element element : interceptorRefElements) {
                interceptorNames.add(element.attribute("name").getText());
            }
        } else {
            interceptorNames = null;
        }
//			拦截器执行拦截
        InterceptorProxy proxy = new InterceptorProxy(request, response);
        proxy.doIntercept(interceptorNames, attrMap);
    }

}