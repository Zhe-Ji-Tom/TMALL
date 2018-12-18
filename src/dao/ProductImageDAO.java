/**
 * @author Tom
 * 2018.12.18
 * 完成productimage表的增删改查
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
import bean.ProductImage;
import util.DBUtil;
import util.DateUtil;

public class ProductImageDAO {
	
	public static final String type_single="type_single";
	public static final String type_detail="type_detail";
	
	//统计数据总数
	public int getTotal() {
		int total=0;
		try(Connection c=DBUtil.getConnection();Statement s=c.createStatement();){
			
			String sql="select count(*) from ProductImage";
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
	public void add(ProductImage bean) {
		String sql="insert into ProductImage values(null,?,?)";
		
		try(Connection c=DBUtil.getConnection();PreparedStatement ps=c.prepareStatement(sql);){
			
			ps.setInt(1, bean.getProduct().getId());
			ps.setString(2, bean.getType());
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
	public void update(ProductImage bean) {
		
	}
	
	//删除
	public void delete(int id) {
		try(Connection c=DBUtil.getConnection();Statement s=c.createStatement();){
			
			String sql="delete from ProductImage where id="+id;
			s.execute(sql);
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	//通过id获取一条数据
	public ProductImage get(int id) {
		ProductImage bean=new ProductImage();
		try(Connection c=DBUtil.getConnection();Statement s=c.createStatement();){
			
			String sql="select * from ProductImage where id="+id;
			ResultSet rs=s.executeQuery(sql);
			
			if(rs.next()) {
				int pid=rs.getInt("pid");
				String type=rs.getString("type");
				Product product=new ProductDAO().get(pid);
				bean.setId(id);
				bean.setType(type);
				bean.setProduct(product);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	//列举部分数据
	public List<ProductImage> list(Product p, String type){
		return list(p,type,0,Short.MAX_VALUE);
	}
	public List<ProductImage> list(Product p,String type,int start,int count){
		List<ProductImage> beans=new ArrayList<ProductImage>();
		String sql="select * from ProductImage where pid=? and type=? order by id desc limit?,?";
		
		try(Connection c=DBUtil.getConnection();PreparedStatement ps=c.prepareStatement(sql);){
			
			ps.setInt(1, p.getId());
			ps.setString(2, type);
			ps.setInt(3, start);
			ps.setInt(4, count);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next()) {
				ProductImage bean=new ProductImage();
				int id=rs.getInt(1);
				bean.setId(id);
				bean.setProduct(p);
				bean.setType(type);
				beans.add(bean);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return beans;
	}
}
