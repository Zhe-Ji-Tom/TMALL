/**
 * @author Tom
 * 2018.12.18
 * 完成对order表的增删改查
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import bean.Order;
import bean.User;
import util.DateUtil;
import util.DBUtil;

public class OrderDAO {
	public static final String waitPay="waitPay";
	public static final String waitDelivery="waitDelivery";
	public static final String waitConfirm="waitConfirm";
	public static final String waitReview="waitReview";
	public static final String finish="finish";
	public static final String delete="delete";
	
	//统计数据总数
	public int getTotal() {
		int total=0;
		try(Connection c=DBUtil.getConnection();Statement s=c.createStatement();){
			
			String sql="select count(*) from oder_";
			ResultSet rs=s.executeQuery(sql);
			
			while(rs.next()) {
				total=rs.getInt(1);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return total;
	}
	
	//增加
	public void add(Order bean) {
		String sql="insert into order_ values(null,?,?,?,?,?,?,?,?,?,?,?,?)";
		try(Connection c=DBUtil.getConnection();PreparedStatement ps=c.prepareStatement(sql);){
			
			ps.setString(1, bean.getOrderCode());
			ps.setString(2, bean.getAddress());
			ps.setString(3, bean.getPost());
			ps.setString(4, bean.getReceiver());
			ps.setString(5, bean.getMobile());
			ps.setString(6, bean.getUserMessage());
			
			ps.setTimestamp(7, DateUtil.d2t(bean.getCreateDate()));
			ps.setTimestamp(8, DateUtil.d2t(bean.getPayDate()));
			ps.setTimestamp(9, DateUtil.d2t(bean.getDeliveryDate()));
			ps.setTimestamp(10, DateUtil.d2t(bean.getConfirmDate()));
			ps.setInt(11, bean.getUser().getId());
			ps.setString(12, bean.getStatus());
			
			ps.execute();
			ResultSet rs=ps.getGeneratedKeys();
			
			if(rs.next()) {
				int id=rs.getInt(1);
				bean.setId(id);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	//更新
	public void update(Order bean) {
		String sql="update order_ set address=?,post=?"
	}
}
