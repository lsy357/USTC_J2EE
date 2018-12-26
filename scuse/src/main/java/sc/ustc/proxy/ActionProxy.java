package sc.ustc.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.dom4j.Element;
import sc.ustc.interceptor.Interceptor;
import sc.ustc.utils.InterceptorUtil;
import sc.ustc.utils.ControllerUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * 遇到intellij bug更换的代理方式
 */
public class ActionProxy implements MethodInterceptor {

    private Object target;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private List<Map<String, String>> interceptorsInfo;
    private Map<Object, Object> attrMap;

    public ActionProxy(HttpServletRequest request, HttpServletResponse response, List<Map<String, String>> interceptorsInfo, Map<Object, Object> attrMap) {
        this.request = request;
        this.response = response;
        this.interceptorsInfo = interceptorsInfo;
        this.attrMap = attrMap;
    }

    public Object bind(Object target){
        this.target = target;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.target.getClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        List<Interceptor> interceptors = new ArrayList<Interceptor>();
        interceptors = InterceptorUtil.getCurrentInterceptor(interceptorsInfo);

        Stack<Interceptor> stack = new Stack<Interceptor>();

//				执行predo
        for (Interceptor interceptor : interceptors) {
            if (!interceptor.preAction()) {
                ControllerUtil.doResponse(request, response,"redirect", "error");
//                        throw new MyException("拦截请求");
            }
            stack.push(interceptor);
        }

//				action方法执行
        String resultName = (String) methodProxy.invoke(target, objects);
        Map<String, String> rtMappingInfo = ControllerUtil.getResultMappingInfo((Element) attrMap.get("element"), resultName);
        String resultMapping = rtMappingInfo.get("value");
        String responseMethod = rtMappingInfo.get("type");

//				执行afterdo
        while (!stack.isEmpty()) {
            Interceptor interceptor = stack.pop();
//					Void
            interceptor.afterAction((String) attrMap.get("name"), resultName);
        }
        ControllerUtil.doResponse(request, response, responseMethod, resultMapping);

        return null;
    }
}
