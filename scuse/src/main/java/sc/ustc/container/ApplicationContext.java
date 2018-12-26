package sc.ustc.container;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class ApplicationContext {
    private static Map<String, Object> valueStack = new HashMap<String, Object>(0);

//    从容器中获取
    public static Object getContext(String key){
        return valueStack.get(key);
    }

//    放置到容器中
    public static void setValue(String key, Object value){
        valueStack.put(key, value);
    }

    public static ServletContext getApplicationContext(){
        return (ServletContext) valueStack.get("applicationContext");
    }

    public static HttpServletRequest getRequest(){
        return (HttpServletRequest) valueStack.get("request");
    }

    public static HttpServletResponse getResponse(){
        return (HttpServletResponse) valueStack.get("response");
    }

    public static Map<String, Object> getValueStack(){
        return (Map<String, Object>) valueStack.get("valueStack");
    }
}
