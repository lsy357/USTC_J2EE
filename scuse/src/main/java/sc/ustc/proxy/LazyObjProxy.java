package sc.ustc.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import sc.ustc.dao.Conversation;
import sc.ustc.dao.JDBCQueryRunner;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 懒加载--如果属性字段在懒加载属性数组中，则增强该字段get方法，使用时查询字段并赋值
 */
public class LazyObjProxy implements MethodInterceptor {

    private Class clazz;
    private Map<String, String> lazyMapper;

    public LazyObjProxy(Map<String, String> lazyMapper) {
        this.lazyMapper = lazyMapper;
    }

    public Object bind(Class clazz){
        this.clazz = clazz;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Object obj = null;
        String methodName = method.getName();
//        获取对应属性字段
        String fieldName = (new StringBuilder()).append(Character.toLowerCase(methodName.substring(3).charAt(0))).append(methodName.substring(3).substring(1)).toString();
        if (methodName.startsWith("get")){
            if (Conversation.getLazyLoadAttrs().contains(fieldName)){
//            查询赋值
                Object value = JDBCQueryRunner.query("select " + lazyMapper.get(fieldName) + " " + lazyMapper.get("sql"), lazyMapper.get(fieldName)).get(0);
                System.out.println("select " + lazyMapper.get(fieldName) + " " + lazyMapper.get("sql"));
                Method setter = clazz.getDeclaredMethod("set" + methodName.substring(3), String.class);
                setter.invoke(o, value);
                obj = methodProxy.invokeSuper(o, objects);
            }else {
                return methodProxy.invokeSuper(o, objects);
            }
        }else {
            obj = methodProxy.invokeSuper(o, objects);
        }
        return obj;
    }
}
