package sc.ustc.ioc.impl;

import org.apache.commons.beanutils.BeanUtils;
import org.dom4j.Element;
import sc.ustc.ioc.BeanFactory;
import sc.ustc.utils.XMLUtil;

import java.io.File;
import java.lang.reflect.Method;
import java.util.*;

public class ClassPathXmlApplicationContext implements BeanFactory {

    private String path;
    private File iocConfig;
    private static Map<String, Object> container = new HashMap<String, Object>();

    public ClassPathXmlApplicationContext(String path) {
        this.path = path;
    }

    private void initContainer(){
        iocConfig = new File(ClassPathXmlApplicationContext.class.getResource(path).getFile());

        List<Element> beanElements = XMLUtil.getSubElementOfRoot(iocConfig, "bean");
        Stack<Map> stack = new Stack<>();

        try {
//            遍历每个bean节点
            for (int i = 0; i < beanElements.size(); i++) {
                Element element = (Element) beanElements.get(i);

                String id = element.attributeValue("id");
                String clazz = element.attributeValue("class");

                System.out.println(id + " : " + clazz);
                Object obj = Class.forName(clazz).newInstance();
                container.put(id, obj);

//                注入依赖，使用了setter注入，没有实现constructor注入
                if (element.element("property") != null) {
                    for (Element propertyElement : (List<Element>) element.elements("property")) {
                        String name = propertyElement.attributeValue("name");
//                        String methodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);

                        String injectedDependance = "";
                        if (propertyElement.attributeValue("bean-ref") != null) {
                            injectedDependance = propertyElement.attributeValue("bean-ref");
                            Object propertyObj = container.get(injectedDependance);
                            if (propertyObj == null) {
                                Map<String, Object> map = new HashMap<>(0);
                                map.put("obj", obj);
//                                map.put("setter", methodName);
                                map.put("attr", name);
                                map.put("ref", injectedDependance);
                                stack.push(map);
                                break;
                            }
                            injectBeanDependance(obj, name, injectedDependance);
                        } else {
                            injectedDependance = propertyElement.attributeValue("value");
                            BeanUtils.setProperty(obj, name, injectedDependance);
                        }
                    }
                }
            }
            while (!stack.empty()) {
                Map injectionMap = stack.pop();
                injectBeanDependance(injectionMap.get("obj"), (String) injectionMap.get("attr"), (String) injectionMap.get("ref"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void injectBeanDependance(Object obj, String name, String injectedDependance) {
        try {
            Object propertyObj = container.get(injectedDependance);
            BeanUtils.setProperty(obj, name, propertyObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getBean(String id) {
        if (container.isEmpty()){
            initContainer();
        }
        return container.get(id);
    }
}

