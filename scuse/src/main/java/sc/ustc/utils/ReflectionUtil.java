package sc.ustc.utils;

import java.lang.reflect.Constructor;
import java.util.Set;

import org.reflections.Reflections;

public class ReflectionUtil {

    public static Object getInstance(String className) {
        try {
            Class clazz = Class.forName(className);
            return clazz.newInstance();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException("反射创建对象错误");
        }
    }

    //    实例私有构造方法类
    public static Object getDeclaredInstance(String className) {
        try {
            Class clazz = Class.forName(className);
            Constructor cons = clazz.getDeclaredConstructor(null);
            cons.setAccessible(true);
            return cons.newInstance(null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException("反射创建对象错误");
        }
    }

    /**
     * 目前只支持函数包含最多两个String参数
     * 执行方法只实例化一个对象
     *
     * @param clazz
     * @param instance
     * @param methodName
     * @return
     */
    public static Object runMethod(Class<?> clazz, Object instance, String methodName) {
        try {
            return clazz.getMethod(methodName).invoke(instance);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException("反射执行方法错误");
        }
    }

    /**
     * 执行方法实例化多个对象
     *
     * @param className
     * @param methodName
     * @return
     */
    public static Object runMethod(String className, String methodName) {

        try {
            Class clazz = Class.forName(className);
            Object instance = clazz.newInstance();
            return runMethod(clazz, instance, methodName);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException("反射执行方法错误");
        }
    }

    /**
     * 获取实现特定接口类的字节码
     *
     * @param packName
     * @param clazz
     * @return
     */
    public static Set<?> getInterfaceImpls(String packName, Class<?> clazz) {
        Reflections reflections = new Reflections(packName);
        Set<?> classes = reflections.getSubTypesOf(clazz);
        return classes;
    }
}
