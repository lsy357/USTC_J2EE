package water.ustc.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtil {
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static String getTime(Date date){
		return sdf.format(date);
	}
}
