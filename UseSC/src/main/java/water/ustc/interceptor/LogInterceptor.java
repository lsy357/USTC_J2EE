package water.ustc.interceptor;

import lombok.Getter;
import lombok.Setter;
import sc.ustc.interceptor.Interceptor;
import sc.ustc.utils.XMLUtil;
import water.ustc.utils.CommonUtil;

import java.util.Date;
import java.util.HashMap;

@Getter @Setter
public class LogInterceptor implements Interceptor {

	private String action = "";
	private String result = "";
	private String sTime;
	private String eTime;

//	private LogInterceptor(){}
//
//	private static class LogInterceptorManager{
//		public static LogInterceptor instance = new LogInterceptor();
//	}
//
//	public static LogInterceptor newInstance(){
//		return LogInterceptorManager.instance;
//	}
	
	public boolean preAction(){
		sTime = CommonUtil.getTime(new Date());
		System.out.println("LogInterceptor preAction");
		return true;
	}
	
	public void afterAction() throws Exception{
		System.out.println("LogInterceptor afterActionWithNoParams");
	}
	
	public void afterAction(String action, String result) throws Exception{
		System.out.println("afterAction");
		
		String eTime = CommonUtil.getTime(new Date());

//		将登陆信息保存在日志
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("name", action);
		map.put("s-time", sTime);
		map.put("e-time", eTime);
		map.put("result", result);
		XMLUtil.map2xml(map);
	}

}
