/**
 * @author Tom
 * 2018.12.18
 * ��ɶ�propertyvalue������ɾ�Ĳ�
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

import bean.Product;
import bean.Property;
import bean.PropertyValue;
import util.DBUtil;

public class PropertyValueDAO {
	
	//ͳ����������
	public int getTotal() {
		int total=0;
		
		try(Connection c=DBUtil.getConnection();Statement s=c.createStatement();){
			
			String sql="select count(*) from PropertyValue";
			ResultSet rs=s.executeQuery(sql);
			
			while(rs.next()) {
				total=rs.getInt(1);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return total;
	}
	
	//����
	public void add(PropertyValue bean) {
		String sql="insert into PropertyValue values(null,?,?,?)";
		
		try(Connection c=DBUtil.getConnection();PreparedStatement ps=c.prepareStatement(sql);){
			
			ps.setInt(1, bean.getProduct().getId());
			ps.setInt(2, bean.getProperty().getId());
			ps.setString(3, bean.getValue());
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
	
	//����
	public void update(PropertyValue bean) {
		String sql="update PropertyValue set pid=?,ptid=?,value=? where id=?";
		
		try(Connection c=DBUtil.getConnection();PreparedStatement ps=c.prepareStatement(sql);){
			
			ps.setInt(1, bean.getProduct().getId());
			ps.setInt(2, bean.getProperty().getId());
			ps.setString(3, bean.getValue());
			ps.setInt(4, bean.getId());
			ps.execute();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	//ɾ��
	public void delete(int id) {
		try(Connection c=DBUtil.getConnection();Statement s=c.createStatement();){
			
			String sql="delete from PropertyValue where id="+id;
			s.execute(sql);
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	//ͨ��id��ȡĳ������
	public PropertyValue get(int id) {
		PropertyValue bean=new PropertyValue();
		try(Connection c=DBUtil.getConnection();Statement s=c.createStatement();){
			
			String sql="select * from PropertyValue where id="+id;
			
			ResultSet rs=s.executeQuery(sql);
			
			if(rs.next()) {
				int pid=rs.getInt("pid");
				int ptid=rs.getInt("ptid");
				String value=rs.getString("value");
				Product product=new ProductDAO().get(pid);
				Property property=new PropertyDAO().get(ptid);
				
				bean.setId(id);
				bean.setProduct(product);
				bean.setProperty(property);
				bean.setValue(value);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	//ͨ��ptid��pid��ȡĳ������
	public PropertyValue get(int ptid,int pid) {
		PropertyValue bean=new PropertyValue();
		
		try(Connection c=DBUtil.getConnection();Statement s=c.createStatement();){
			
			String sql="select * from PropertyValue where ptid="+ptid+" and pid="+pid;
			ResultSet rs=s.executeQuery(sql);
			
			if(rs.next()) {
				bean=new PropertyValue();
				
				int id=rs.getInt("id");
				String value=rs.getString("value");
				Product product=new ProductDAO().get(pid);
				Property property=new PropertyDAO().get(ptid);
				
				bean.setId(id);
				bean.setProduct(product);
				bean.setProperty(property);
				bean.setValue(value);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	//�оٲ�������
	public List<PropertyValue> list(){
		return list(0,Short.MAX_VALUE);
	}
	public List<PropertyValue> list(int start,int count){
		List<PropertyValue> beans=new ArrayList<PropertyValue>();
		
		String sql="select * from PropertyValue order by id desc limit ?,?";
		
		try(Connection c=DBUtil.getConnection();PreparedStatement ps=c.prepareStatement(sql);){
		
			ps.setInt(1, start);
			ps.setInt(2, count);
			
			ResultSet rs=ps.executeQuery();
			
			while(rs.next()) {
				PropertyValue bean=new PropertyValue();
				
				int id=rs.getInt("id");
				int pid=rs.getInt("pid");
				int ptid=rs.getInt("ptid");
				String value=rs.getString("value");
				
				Product product=new ProductDAO().get(pid);
				Property property=new PropertyDAO().get(ptid);
				
				bean.setId(id);
				bean.setProduct(product);
				bean.setProperty(property);
				bean.setValue(value);
				beans.add(bean);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return beans;
	}
}