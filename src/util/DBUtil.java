/**
 * @author Tom
 * 2018.12.14
 * 连接MySQL
 */
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
	//数据库连接信息
	static String ip="127.0.0.1";
	static int port=3306;
	static String database="tamll";
	static String encoding="UTF-8";
	static String loginName="root";
	static String password="admin";
	
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	//连接数据库
	public static Connection getConnection() throws SQLException{
		String url=String.format("jdbc:mysql://%s:%d/%s?characterEncoding=%s",ip,port,database,encoding);
		return DriverManager.getConnection(url,loginName,password);
	}
	
	public static void main(String[] args) throws SQLException{
		System.out.println(getConnection());
	}
}
