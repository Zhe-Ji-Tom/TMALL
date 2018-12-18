/**
 * @author Tom
 * 2018.12.18
 * 完成对review表的增删改查
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

import bean.Product;
import bean.Review;
import bean.User;
import util.DBUtil;
import util.DateUtil;

public class ReviewDAO {
	
	//统计数据总数
	public int getTotal() {
		int total=0;
		try(Connection c=DBUtil.getConnection();Statement s=c.createStatement();){
			String sql="select count(*) from Review";
			ResultSet rs=s.executeQuery(sql);
			
			while(rs.next()) {
				total=rs.getInt(1);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return total;
	}
	
	//统计同一产品类别的数量
	public int getTotal(int pid) {
		int total=0;
		try(Connection c=DBUtil.getConnection();Statement s=c.createStatement();){
			String sql="select count(*) from Review where pid="+pid;
			ResultSet rs=s.executeQuery(sql);
			
			while(rs.next()) {
				total=rs.getInt("1");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return total;
	}
	
	//更新
	public void update(Review bean) {
		String sql="update Review set content=?,uid=?,pid=?,createDate=? where id=?";
		try(Connection c=DBUtil.getConnection();PreparedStatement ps=c.prepareStatement(sql);){
			
			ps.setString(1, bean.getContent());
			ps.setInt(2, bean.getUser().getId());
			ps.setInt(3, bean.getProduct().getId());
			ps.setTimestamp(4, DateUtil.d2t(bean.getCreateDate()));
			ps.setInt(5, bean.getId());
			ps.execute();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	//删除
	public void delete(int id) {
		try(Connection c=DBUtil.getConnection();Statement s=c.createStatement();){
			
			String sql="delete from Review where id="+id;
			s.executeQuery(sql);
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	//获取某条数据
	public Review get(int id) {
		Review bean=new Review();
		try(Connection c=DBUtil.getConnection();Statement s=c.createStatement();){
		
			String sql="select * from Review where id="+id;
			ResultSet rs=s.executeQuery(sql);
			
			if(rs.next()) {
				int pid=rs.getInt("pid");
				int uid=rs.getInt("uid");
				Date createDate=DateUtil.t2d(rs.getTimestamp("createDate"));
				String content=rs.getString("content");
				Product product=new ProductDAO().get(pid);
				User user=new UserDAO().get(uid);
				
				bean.setContent(content);
				bean.setCreateDate(createDate);
				bean.setId(uid);
				bean.setProduct(product);
				bean.setUser(user);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	//统计同一产品评论数量
	public int getCount(int pid) {
		String sql="select count(*) from Review where pid=?";
		try(Connection c=DBUtil.getConnection();PreparedStatement ps=c.prepareStatement(sql);){
			
			ps.setInt(1, pid);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next()) {
				return rs.getInt(1);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	//列举部分数据
	public List<Review> list(int pid){
		return list(pid,0,Short.MAX_VALUE);
	}
	public List<Review> list(int pid,int start,int count){
		List<Review> beans=new ArrayList<Review>();
		String sql="select * from Review where pid=? order by id desc limit ?,?";
		
		try(Connection c=DBUtil.getConnection();PreparedStatement ps=c.prepareStatement(sql);){
			
			ps.setInt(1, pid);
			ps.setInt(2, start);
			ps.setInt(3, count);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next()) {
				Review bean=new Review();
				int id=rs.getInt("id");
				int uid=rs.getInt("uid");
				Date createDate=DateUtil.t2d(rs.getTimestamp("createDate"));
				String content=rs.getString("contect");
				Product product=new ProductDAO().get(pid);
				User user=new UserDAO().get(uid);
				
				bean.setContent(content);
				bean.setCreateDate(createDate);
				bean.setId(uid);
				bean.setProduct(product);
				bean.setUser(user);
				beans.add(bean);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return beans;
	}
	
	//检测评论是否存在
	public boolean isExist(String content,int pid) {
		String sql="select * from Review where content=? and pid=?";
		
		try(Connection c=DBUtil.getConnection();PreparedStatement ps=c.prepareStatement(sql);){
			ps.setString(1, content);
			ps.setInt(2, pid);
			ResultSet rs=ps.executeQuery();
			
			if(rs.next()) {
				return true;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
