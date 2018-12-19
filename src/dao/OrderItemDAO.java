/**
 * @author Tom
 * 2018.12.19
 * 完成对orderitem表的增删改查
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

import bean.Category;
import bean.Order;
import bean.OrderItem;
import bean.Product;
import bean.User;
import util.DBUtil;

public class OrderItemDAO {
	
	//统计数据总数
	public int getTotal() {
		int total=0;
		try(Connection c=DBUtil.getConnection();Statement s=c.createStatement();){
			String sql="select * from OrderItem";
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
	public void add(OrderItem bean) {
		String sql="insert into OrderItem values(null,?,?,?,?)";
		try(Connection c=DBUtil.getConnection();PreparedStatement ps=c.prepareStatement(sql);){
			
			ps.setInt(1, bean.getProduct().getId());
			
			if(bean.getOrder()==null)
				ps.setInt(2, -1);
			else
				ps.setInt(2, bean.getOrder().getId());
			
			ps.setInt(3, bean.getUser().getId());
			ps.setInt(4, bean.getNumber());
			ps.execute();
			ResultSet rs=ps.getGeneratedKeys();
			
			if(rs.next()) {
				int id=rs.getInt("1");
				bean.setId(id);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	//更新
	public void update(OrderItem bean) {
		String sql="update OrderItem set pid=?,oid=?,uid=?,number=? where id=?";
		try(Connection c=DBUtil.getConnection();PreparedStatement ps=c.prepareStatement(sql);){
			
			ps.setInt(1, bean.getProduct().getId());
			
			if(bean.getProduct()==null)
				ps.setInt(2, -1);
			else
				ps.setInt(2, bean.getOrder().getId());
			
			ps.setInt(3, bean.getUser().getId());
			ps.setInt(4, bean.getNumber());
			ps.setInt(5, bean.getId());
			ps.execute();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	//删除
	public void delete (int id) {
		try(Connection c=DBUtil.getConnection();Statement s=c.createStatement();){
			String sql="delete from OrderItem where id="+id;
			s.execute(sql);
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	//获取某一条数据
	public OrderItem get(int id) {
		OrderItem bean=new OrderItem();
		try(Connection c=DBUtil.getConnection();Statement s=c.createStatement();){
			
			String sql="select * from OrderItem where id="+id;
			ResultSet rs=s.executeQuery(sql);
			
			if(rs.next()) {
				int pid=rs.getInt("pid");
				int oid=rs.getInt("oid");
				int uid=rs.getInt("uid");
				int number=rs.getInt("number");
				Product product=new ProductDAO().get(pid);
				User user=new UserDAO().get(uid);
				
				bean.setId(id);
				bean.setProduct(product);
				bean.setUser(user);
				bean.setNumber(number);
				
				if(oid!=-1) {
					Order order=new OrderDAO().get(oid);
					bean.setOrder(order);
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	//列举同一用户的所有数据
	public List<OrderItem> listByUser(int uid){
		return listByUser(uid,0,Short.MAX_VALUE);
	}
	public List<OrderItem> listByUser(int uid,int start,int count){
		List<OrderItem> beans=new ArrayList<OrderItem>();
		String sql="select * from OrderItem where uid=? and oid=-1 order by id desc limit ?,?";
		try(Connection c=DBUtil.getConnection();PreparedStatement ps=c.prepareStatement(sql);){
			
			ps.setInt(1, uid);
			ps.setInt(2, start);
			ps.setInt(3, count);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next()) {
				OrderItem bean=new OrderItem();
				int id=rs.getInt("1");
				int pid=rs.getInt("pid");
				int oid=rs.getInt("oid");
				int number=rs.getInt("number");
				User user=new UserDAO().get(uid);
				Product product=new ProductDAO().get(pid);
				
				if(oid!=-1) {
					Order order=new OrderDAO().get(oid);
					bean.setOrder(order);
				}
				
				bean.setProduct(product);
				bean.setUser(user);
				bean.setNumber(number);
				bean.setId(id);
				beans.add(bean);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return beans;
	}
	
	//列举同一订单的所有数据
	public List<OrderItem> listByOrder(int oid){
		return listByOrder(oid,0,Short.MAX_VALUE);
	}
	public List<OrderItem> listByOrder(int oid,int start,int count){
		List<OrderItem> beans=new ArrayList<OrderItem>();
		String sql="select * from OrderItem where oid=? order by id desc limit ?,?";
		try(Connection c=DBUtil.getConnection();PreparedStatement ps=c.prepareStatement(sql);){
			
			ps.setInt(1, oid);
			ps.setInt(2, start);
			ps.setInt(3, count);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next()) {
				OrderItem bean=new OrderItem();
				int id=rs.getInt(1);
				int pid=rs.getInt("pid");
				int uid=rs.getInt("uid");
				int number=rs.getInt("number");
				Product product=new ProductDAO().get(pid);
				User user=new UserDAO().get(uid);
				
				if(oid!=-1) {
					Order order=new OrderDAO().get(oid);
					bean.setOrder(order);
				}
				
				bean.setProduct(product);
				bean.setUser(user);
				bean.setNumber(number);
				bean.setId(id);
				beans.add(bean);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return beans;
	}
	
	//统计总价
	public void fill(Order o) {
		List<OrderItem> ois=listByOrder(o.getId());
		float total=0;
		for(OrderItem oi:ois) {
			total+=oi.getNumber()*oi.getProduct().getPromotePrice();
		}
		o.setTotal(total);
		o.setOrderItems(ois);
	}
	
	//列举同一订单的数据
	public List<OrderItem> listByProduct(int pid){
		return listByProduct(pid,0,Short.MAX_VALUE);
	}
	public List<OrderItem> listByProduct(int pid,int start,int count){
		List<OrderItem> beans=new ArrayList<OrderItem>();
		String sql="select * from OrderItem where pid=? order by id desc limit ?,?";
		try(Connection c=DBUtil.getConnection();PreparedStatement ps=c.prepareStatement(sql);){
			
			ps.setInt(1, pid);
			ps.setInt(2, start);
			ps.setInt(3, count);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next()) {
				OrderItem bean=new OrderItem();
				int id=rs.getInt(1);
				int uid=rs.getInt("uid");
				int oid=rs.getInt("oid");
				int number=rs.getInt("number");
				Product product=new ProductDAO().get(pid);
				User user=new UserDAO().get(uid);
				
				if(oid!=-1) {
					Order order=new OrderDAO().get(oid);
					bean.setOrder(order);
				}
				
				bean.setProduct(product);
				bean.setUser(user);
				bean.setNumber(number);
				bean.setId(id);
				beans.add(bean);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return beans;
	}
	
	//统计折扣总额
	public int getSaleCount(int pid) {
		int total=0;
		try(Connection c=DBUtil.getConnection();Statement s=c.createStatement();){
			
			String sql="select sum(number) from OrderItem where pid="+pid;
			ResultSet rs=s.executeQuery(sql);
			
			while(rs.next()) {
				total=rs.getInt(1);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return total;
	}
}
