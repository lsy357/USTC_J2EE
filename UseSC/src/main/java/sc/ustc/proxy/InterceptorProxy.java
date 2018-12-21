package sc.ustc.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.dom4j.Element;
import sc.ustc.action.Action;
import sc.ustc.exception.MyException;
import sc.ustc.interceptor.Interceptor;
import sc.ustc.utils.InterceptorUtil;
import sc.ustc.utils.ControllerUtil;
import sc.ustc.utils.ReflectionUtil;
import sc.ustc.utils.StoneValueUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * 拦截类调用interceptor实现拦截
 */
public class InterceptorProxy {

    private HttpServletRequest request;
    private HttpServletResponse response;

    public InterceptorProxy(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public void doIntercept(List<String> interceptorNames, Map<String, Object> attrMap) throws Exception {

        //			interceptorsInfo--当前action节点的全部interceptor属性
        List<Map<String, String>> interceptorsInfo = new ArrayList<Map<String, String>>();
        if (interceptorsValid(interceptorNames)) {

            for (Map<String, String> map : StoneValueUtil.getInterceptorInfos()) {
                if (interceptorNames.contains(map.get("name"))) {
                    interceptorsInfo.add(map);
                }
            }

//            Action actionInstance = (Action) ReflectionUtil.getInstance((String) attrMap.get("class"));
//            ActionProxy actionProxy = new ActionProxy(request, response, interceptorsInfo, attrMap);
//            Object proxy = actionProxy.bind(actionInstance);
//            System.out.println(proxy.getClass().getName());
//            ReflectionUtil.runMethod(proxy.getClass(), proxy, (String) attrMap.get("method"));

            doInterceptProxy(interceptorsInfo, attrMap);

        }

    }

    public void doInterceptProxy(List<Map<String, String>> interceptorsInfo, Map<String, Object> attrMap) {
        //		CGlib动态代理实现
        Action actionInstance = (Action) ReflectionUtil.getInstance((String) attrMap.get("class"));

        Action proxyAction = (Action) Enhancer.create(actionInstance.getClass(), new MethodInterceptor() {

            @Override
            public Object intercept(Object arg0, Method method, Object[] arg2, MethodProxy arg3) throws Throwable {
                // TODO Auto-generated method stub

//				根据信息实例该action所有Interceptor
                List<Interceptor> interceptors = new ArrayList<Interceptor>();
                interceptors = InterceptorUtil.getCurrentInterceptor(interceptorsInfo);

                Stack<Interceptor> stack = new Stack<Interceptor>();

//				执行predo
                for (Interceptor interceptor : interceptors) {
                    if (!interceptor.preAction()) {
                        ControllerUtil.doResponse(request, response,"redirect", "error");
                        throw new MyException("拦截请求");
                    }
                    stack.push(interceptor);
                }

//				action方法执行
                String resultName = (String) method.invoke(actionInstance, null);
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
        });

//        代理对象执行对应方法
        ReflectionUtil.runMethod(proxyAction.getClass(), proxyAction, (String) attrMap.get("method"));

//		JDK动态代理实现
//		ActionSupport actionInstance = (ActionSupport)ReflectionUtil.getInstance((String)attrMap.get("class"));
//
//		ActionSupport proxyAction = (ActionSupport) Proxy.newProxyInstance(actionInstance.getClass().getClassLoader(),
//				actionInstance.getClass().getInterfaces(),
//				new InvocationHandler() {
//
//					@Override
//					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//						// TODO Auto-generated method stub
//
//						String methodName = method.getName();
//
////						根据信息实例该action所有Interceptor
//						List<Interceptor> interceptors = new ArrayList<Interceptor>();
//						interceptors = InterceptorUtil.getCurrentInterceptor(request, response, interceptorsInfo);
//
//						Stack<Interceptor> stack = new Stack<Interceptor>();
//
////						执行predo
//						for (Interceptor interceptor : interceptors) {
//							if (!interceptor.preAction()) {
//								redirect(request, response, "error");
//								throw new MyException("拦截请求");
//							}
//							stack.push(interceptor);
//						}
//
////						action方法执行
//						String resultName = (String) method.invoke(actionInstance, null);
//						String resultMapping = getResultMapping((Element)attrMap.get("element"), resultName);
//
////						执行afterdo
//						while (!stack.isEmpty()) {
//							Interceptor interceptor = stack.pop();
////							Void
//							interceptor.afterAction((String)attrMap.get("name"), resultName);
//						}
//						redirect(request, response, resultMapping);
//
//						return null;
//					}
//				});
//
//		proxyAction.execute();

    }

    /**
     * 判断interceptor是否被声明
     * @param actionInterceptors
     * @return
     */
    public boolean interceptorsValid(List<String> actionInterceptors) {
        for (String string : actionInterceptors) {
            if (!StoneValueUtil.getInterceptorAttr("name").contains(string)) {
                return false;
            }
        }
        return true;
    }

}
