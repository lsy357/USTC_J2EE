import org.junit.Test;
import sc.ustc.dao.Conversation;
import sc.ustc.dao.ORMConfiguration;
import sc.ustc.interceptor.Interceptor;
import sc.ustc.utils.InterceptorUtil;
import sc.ustc.utils.ReflectionUtil;
import sc.ustc.utils.StoneValueUtil;
import sc.ustc.utils.XMLUtil;

import java.util.*;

public class ClassCastTest {

    @Test
    public void castTest(){
        List<Interceptor> currentInterceptors = new ArrayList<Interceptor>();
        Set<?> sysInterfaces = ReflectionUtil.getInterfaceImpls("sc.ustc.interceptor.impl", Interceptor.class);
        for (Object sysInterceptor : sysInterfaces){
            currentInterceptors.add((Interceptor) sysInterceptor);
            System.out.println(sysInterceptor.getClass().getName());
        }
    }

    @Test
    public void setterTest(){
        System.out.println(ORMConfiguration.getEntityMapperList());
        System.out.println(Conversation.getMappers());
        System.out.println(Conversation.getLazyLoadAttrs());
        System.out.println(XMLUtil.getPublicUrls(StoneValueUtil.controllerXML));
    }
}
