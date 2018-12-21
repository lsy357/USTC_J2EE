package sc.ustc.utils;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 一些web项目启动时确定的变量
 */
public class StoneValueUtil {
	
	final public static File controllerXML = new File(StoneValueUtil.class.getResource("/controller.xml").getFile());
	
	public static List<Map<String, String>> getInterceptorInfos(){
		try {
			return sc.ustc.utils.XMLUtil.getAllRElementAttrs(controllerXML, "interceptor");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据属性名获取全部interceptor的属性值
	 * @param attrName
	 * @return
	 */
	public static List<String> getInterceptorAttr(String attrName){
		try {
			return XMLUtil.getRElementAttrByName(controllerXML, "interceptor", attrName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
