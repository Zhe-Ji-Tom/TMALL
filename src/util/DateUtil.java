/**
 * @author Tom
 * 2018.12.14
 * 日期类型转换
 */
package util;

public class DateUtil {
	//Date转为Timestamp
	public static java.sql.Timestamp d2t(java.util.Date d){
		if(d==null)
			return null;
		return new java.sql.Timestamp(d.getTime());
	}
	
	//Timestamp转为Date
	public static java.util.Date t2d(java.sql.Timestamp t){
		if(t==null)
			return null;
		return new java.util.Date(t.getTime());
	}
}
