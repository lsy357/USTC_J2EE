package sc.ustc.utils;

import java.util.*;

import sc.ustc.interceptor.Interceptor;
import sc.ustc.interceptor.impl.PrivacyInterceptor;

public class InterceptorUtil {

    public static Map<String, Interceptor> interceptors = new HashMap<String, Interceptor>(0);
    protected static Set<?> sysInterfacesClass = ReflectionUtil.getInterfaceImpls("sc.ustc.interceptor.impl", Interceptor.class);
    protected static List<Interceptor> sysInterceptors = new ArrayList<Interceptor>(0);

    static {
        for (Object sysInterfaceClass : sysInterfacesClass){
            try {
                sysInterceptors.add((Interceptor)((Class)sysInterfaceClass).newInstance());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("系统拦截器初始化失败");
            }
        }
        initSysInterceptors();
//        实例所有已定义的拦截器
        for (String interceptorClassName : StoneValueUtil.getInterceptorAttr("class")) {
            interceptors.put(interceptorClassName, (Interceptor)ReflectionUtil.getInstance(interceptorClassName));
        }
    }

    public static void initSysInterceptors(){
        PrivacyInterceptor.setPublicURLs("/loginUI.sc", "/login.sc", "/welcome.html");
    }

//    用即取
    public static Interceptor getInterceptor(String className){
        return interceptors.get(className);
    }

	/**
	 * 返回所给信息的拦截器
	 * @param interceptorsInfo
	 * @return
	 */
	public static List<Interceptor> getCurrentInterceptor(List<Map<String, String>> interceptorsInfo){
		List<sc.ustc.interceptor.Interceptor> currentInterceptors = new ArrayList<sc.ustc.interceptor.Interceptor>();
//		放入系统拦截器
        for (Interceptor sysInterceptor : sysInterceptors){
            currentInterceptors.add((Interceptor) sysInterceptor);
        }
//		放入定义的拦截器
		for (Map<String, String> map : interceptorsInfo) {
            currentInterceptors.add(getInterceptor(map.get("class")));
		}
		return currentInterceptors;
	}

}
