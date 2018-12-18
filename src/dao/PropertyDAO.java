/**
 * @author Tom
 * 2018.12.18
 * 完成property表的增删改查
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

import bean.Category;
import bean.Product;
import bean.Property;
import util.DBUtil;
import util.DateUtil;

public class PropertyDAO {
	
	//统计数据总数
	public int getTotal(int cid) {
		int total=0;
		try(Connection c=DBUtil.getConnection();Statement s=c.createStatement();){
			
			String sql="select count(*) from property where cid="+cid;
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
	public void add(Property bean) {
		String sql="insert into property values(null,?,?)";
		try(Connection c=DBUtil.getConnection();PreparedStatement ps=c.prepareStatement(sql);){
			
			ps.setInt(1, bean.getCategory().getId());
			ps.setString(2, bean.getName());
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
	public void update(Property bean) {
		String sql="update property set cid=?,name=? where id=?";
		try(Connection c=DBUtil.getConnection();PreparedStatement ps=c.prepareStatement(sql);){
			
			ps.setInt(1, bean.getCategory().getId());
			ps.setString(2, bean.getName());
			ps.setInt(3, bean.getId());
			ps.execute();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	//通过id获取数据
	public Property get(int id) {
		Property bean=new Property();
		try(Connection c=DBUtil.getConnection();Statement s=c.createStatement();){
			
			String sql="select * from property where id="+id;
			ResultSet rs=s.executeQuery(sql);
			
			if(rs.next()) {
				String name=rs.getString("name");
				int cid=rs.getInt("cid");
				Category category=new CategoryDAO().get(cid);
				bean.setName(name);
				bean.setId(id);
				bean.setCategory(category);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	//列举同一种类的数据
	public List<Property> list(int cid){
		return list(cid,0,Short.MAX_VALUE);
	}
	public List<Property> list(int cid,int start, int count){
		List<Property> beans=new ArrayList<Property>();
		String sql="select * from property where cid=? order by id desc limit?,?";
		
		try(Connection c=DBUtil.getConnection();PreparedStatement ps=c.prepareStatement(sql);){
			
			ps.setInt(1, cid);
			ps.setInt(2, start);
			ps.setInt(3, count);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next()) {
				Property bean=new Property();
				int id=rs.getInt(1);
				String name=rs.getString("name");
				Category category=new CategoryDAO().get(cid);
				bean.setId(id);
				bean.setName(name);
				bean.setCategory(category);
				beans.add(bean);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return beans;
	}
}
