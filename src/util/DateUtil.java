/**
 * @author Tom
 * 2018.12.14
 * ��������ת��
 */
package util;

public class DateUtil {
	//DateתΪTimestamp
	public static java.sql.Timestamp d2t(java.util.Date d){
		if(d==null)
			return null;
		return new java.sql.Timestamp(d.getTime());
	}
	
	//TimestampתΪDate
	public static java.util.Date t2d(java.sql.Timestamp t){
		if(t==null)
			return null;
		return new java.util.Date(t.getTime());
	}
}
