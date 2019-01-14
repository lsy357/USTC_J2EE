package sc.ustc.utils;

import org.dom4j.Element;
import sc.ustc.exception.MyException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Map;

/**
 * 中心控制器进行调度的工具类
 */
public class ControllerUtil {

    //	指定controller.xml特定位置
    final static private File controllerXML = new File(StoneValueUtil.class.getResource("/controller.xml").getFile());

    public static String getServletPath(HttpServletRequest request) {
        return request.getServletPath();
    }

    /**
     * 根据result响应请求，默认为forward方式
     *
     * @param request
     * @param response
     * @param responseMethod
     * @param servletPath    response的servletPath
     * @return
     * @throws Exception
     */
    public static String doResponse(HttpServletRequest request, HttpServletResponse response, String responseMethod, String servletPath) throws Exception {
        if (responseMethod != null) {
            switch (responseMethod) {
                case "redirect":
                    response.setHeader("refresh", "0;url=" + getDirectURL(request.getContextPath(), servletPath));
                    return "redirect";
                case "forward":
                    request.getRequestDispatcher(getForwardURI(servletPath)).forward(request, response);
                    return "forward";
                case "plain":
                    return "plain";
            }
            throw new MyException("响应方式设置错误");
        }
        request.getRequestDispatcher(getForwardURI(servletPath)).forward(request, response);
        return "forward";
    }

    //    拼接请求URL,规定web-content目录下只能有html页面
    public static String getDirectURL(String contextPath, String servletPath) {
        return contextPath + "/" + getForwardURI(servletPath);
    }

    public static String getForwardURI(String servletPath) {
        if (servletPath.contains("jsp")) {
            return servletPath + ".jsp";
        } else if (servletPath.contains("pages")) {
            return servletPath + ".xml";
        } else if (servletPath.contains("html")) {
            return servletPath + ".html";
        } else {
            return servletPath + ".html";
        }
    }

    /**
     * 解析URL并获取controller.xml中action节点信息
     *
     * @param request
     * @param response
     * @return
     */
    public static Map<String, Object> getHandlerInfo(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String servletPath = getServletPath(request);
        String actionName = servletPath.substring(servletPath.lastIndexOf("/") + 1, servletPath.lastIndexOf("."));
//        Map<Object, Object> map = XMLUtil.getElementAttributeByName(controllerXML, "action", actionName);
        Map<String, Object> map = XMLUtil.parseThirdLevelElememt(controllerXML, "controller", "action", actionName);
        if (map == null) {
            throw new MyException("action映射失败");
        }
        return map;
    }

    /**
     * 获取result映射视图和响应方式
     *
     * @param actionElement
     * @param resultName
     * @return
     * @throws Exception
     */
    public static Map<String, String> getResultMappingInfo(Element actionElement, String resultName) throws Exception {
        Map<String, String> map = XMLUtil.getReslutInfoByName(actionElement, resultName);
        if (map != null) {
            return map;
        }
        throw new MyException("result映射失败");
    }

}
