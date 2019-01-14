package sc.ustc.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class IntrospectorUtil<T> {
    public void callSetter(T object, Map<String, String> map) throws InvocationTargetException, IllegalAccessException {
        Method[] ms = object.getClass().getMethods();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String methodName = "set" + entry.getKey();
            for (Method m : ms) {
                if (methodName.equalsIgnoreCase(m.getName())) {
                    m.invoke(object, entry.getValue());
                    break;
                }
            }
        }
    }

    public Object callGetter(T object, String attrName) throws InvocationTargetException, IllegalAccessException {
        Method[] ms = object.getClass().getMethods();
        String methodName = "get" + attrName;
        for (Method m : ms) {
            if (methodName.equalsIgnoreCase(m.getName())) {
                return m.invoke(object);
            }
        }
        return null;
    }
}
