/**
 * @author Tom
 * 2018.12.19
 * ��ɶ�product�����ɾ�Ĳ�
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.naming.PartialResultException;

import java.util.ArrayList;
import java.util.Date;

import bean.Category;
import bean.Product;
import bean.ProductImage;
import util.DateUtil;
import util.DBUtil;

public class ProductDAO {
	
	//ͳ����������
	public int getTotal(int cid) {
		int total=0;
		try(Connection c=DBUtil.getConnection();Statement s=c.createStatement();){
			
			String sql="select count(*) from product where cid="+cid;
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
	public void add(Product bean) {
		String sql="insert into product values(null,?,?,?,?,?,?,?)";
		try(Connection c=DBUtil.getConnection();PreparedStatement ps=c.prepareStatement(sql);){
			
			ps.setString(1, bean.getName());
			ps.setString(2, bean.getSubTitle());
			ps.setFloat(3, bean.getOrignalPrice());
			ps.setFloat(4, bean.getPromotePrice());
			ps.setInt(5, bean.getStock());
			ps.setInt(6, bean.getCategory().getId());
			ps.setTimestamp(7, DateUtil.d2t(bean.getCreateDate()));
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
	public void update(Product bean) {
		String sql="update product set name=?,subTitel=?,orignalPrice=?,promotePrice=?,stock=?,"
				+ "cid=?,createDate=? where id=?";
		try(Connection c=DBUtil.getConnection();PreparedStatement ps=c.prepareStatement(sql);){
			
			ps.setString(1, bean.getName());
			ps.setString(2, bean.getSubTitle());
			ps.setFloat(3, bean.getOrignalPrice());
			ps.setFloat(4, bean.getPromotePrice());
			ps.setInt(5, bean.getStock());
			ps.setInt(6, bean.getCategory().getId());
			ps.setTimestamp(7, DateUtil.d2t(bean.getCreateDate()));
			ps.setInt(8, bean.getId());
			ps.execute();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	//ɾ��
	public void delete(int id) {
		try(Connection c=DBUtil.getConnection();Statement s=c.createStatement();){
			
			String sql="delete from product where id="+id;
			s.execute(sql);
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	//��ȡĳһ������
	public Product get(int id) {
		Product bean=new Product();
		try(Connection c=DBUtil.getConnection();Statement s=c.createStatement();){
			
			String sql="select * from product where id="+id;
			ResultSet rs=s.executeQuery(sql);
			
			if(rs.next()) {
				String name=rs.getString("name");
				String subTitle=rs.getString("subTitle");
				float orignalPrice=rs.getFloat("orignalPrice");
				float promotePrice=rs.getFloat("promotePrice");
				int stock=rs.getInt("stock");
				int cid=rs.getInt("cid");
				Date createDate=DateUtil.t2d(rs.getTimestamp("createDate"));
				Category category=new CategoryDAO().get(cid);
				
				bean.setName(name);
				bean.setSubTitle(subTitle);
				bean.setOrignalPrice(orignalPrice);
				bean.setPromotePrice(promotePrice);
				bean.setStock(stock);
				bean.setCategory(category);
				bean.setCreateDate(createDate);
				bean.setId(id);
				setFirstProductImage(bean);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	//�о�ͬһ�����ȫ������
	public List<Product> list(int cid){
		return list(cid,0,Short.MAX_VALUE);
	}
	//�о�ͬһ����Ĳ�������
	public List<Product> list(int cid,int start,int count){
		List<Product> beans=new ArrayList<Product>();
		Category category=new CategoryDAO().get(cid);
		String sql="select * from product where cid=? order by id desc limit ?,?";
		try(Connection c=DBUtil.getConnection();PreparedStatement ps=c.prepareStatement(sql);){
			
			ps.setInt(1, cid);
			ps.setInt(2, start);
			ps.setInt(3, count);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next()) {
				Product bean=new Product();
				int id=rs.getInt(1);
				String name=rs.getString("name");
				String subTitle=rs.getString("subTitle");
				float orignalPrice=rs.getFloat("origanlPrice");
				float promotePrice=rs.getFloat("promotePrice");
				int stock=rs.getInt("stock");
				Date createDate=DateUtil.t2d(rs.getTimestamp("createDate"));
				
				bean.setName(name);
				bean.setSubTitle(subTitle);
				bean.setOrignalPrice(orignalPrice);
				bean.setPromotePrice(promotePrice);
				bean.setStock(stock);
				bean.setCreateDate(createDate);
				bean.setId(id);
				bean.setCategory(category);
				setFirstProductImage(bean);
				beans.add(bean);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return beans;
	}
	
	//�о�ȫ������
	public List<Product> list(){
		return list(0,Short.MAX_VALUE);
	}
	//�оٲ�������
	public List<Product> list(int start, int count){
		List<Product> beans=new ArrayList<Product>();
		String sql="select * from product limit ?,?";
		try(Connection c=DBUtil.getConnection();PreparedStatement ps=c.prepareStatement(sql);){
			ps.setInt(1, start);
			ps.setInt(2, count);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next()) {
				Product bean=new Product();
				int id=rs.getInt(1);
				int cid=rs.getInt("cid");
				String name=rs.getString("name");
				String subTitle=rs.getString("subTitle");
				float orignalPrice=rs.getFloat("orignalPrice");
				float promotePrice=rs.getFloat("promotePrice");
				int stock=rs.getInt("stock");
				Date createDate=DateUtil.t2d(rs.getTimestamp("createDate"));
				Category category=new CategoryDAO().get(cid);
				
				bean.setName(name);
				bean.setSubTitle(subTitle);
				bean.setOrignalPrice(orignalPrice);
				bean.setPromotePrice(promotePrice);
				bean.setStock(stock);
				bean.setCreateDate(createDate);
				bean.setId(cid);
				bean.setCategory(category);
				beans.add(bean);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return beans;
	}
	
	//���
	public void fill(List<Category> cs) {
		for(Category c:cs)
			fill(c);
	}
	public void fill(Category c) {
		List<Product> ps=this.list(c.getId());
		c.setProducts(ps);
	}
	
	//�������
	public void fillByRow(List<Category> cs) {
		int productNumberEachRow=8;
		for(Category c:cs) {
			List<Product> products=c.getProducts();
			List<List<Product>> productByRow=new ArrayList<>();
			for(int i=0;i<products.size();i+=productNumberEachRow) {
				int size=i+productNumberEachRow;
				size=size>products.size()?products.size():size;
				List<Product> productsOfEachRow=products.subList(i, size);
				productByRow.add(productsOfEachRow);
			}
			c.setProductsByRow(productByRow);
		}
	}
	
	//������Ʒ��һ��ͼƬ
	public void setFirstProductImage(Product p) {
		List<ProductImage> pis=new ProductImageDAO().list(p, ProductImageDAO.type_single);
		if(!pis.isEmpty())
			p.setFirstProductImage(pis.get(0));
	}
	
	//����ĳһ��Ʒ�����ۺ���������
	public void setSaleAndReviewNumber(Product p) {
		int saleCount=new OrderItemDAO().getSaleCount(p.getId());
		p.setSaleCount(saleCount);
		int reviewCount=new ReviewDAO().getCount(p.getId());
		p.setReviewCount(reviewCount);
	}
	
	//����һ����Ʒ�����ۺ���������
	public void setSaleAndReviewNumber(List<Product> products) {
		for(Product p:products) {
			setSaleAndReviewNumber(p);
		}
	}
	
	//���ؼ���������Ʒ
	public List<Product> search(String keyword,int start,int count){
		List<Product> beans=new ArrayList<Product>();
		if(keyword==null||keyword.trim().length()==0)
			return beans;
		String sql="select * from Product where name like ? limit ?,?";
		try(Connection c=DBUtil.getConnection();PreparedStatement ps=c.prepareStatement(sql);){
			
			ps.setString(1, "%"+keyword.trim()+"%");
			ps.setInt(2, start);
			ps.setInt(3, count);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next()) {
				Product bean=new Product();
				int id=rs.getInt(1);
				int cid=rs.getInt("cid");
				String name=rs.getString("name");
				String subTitle=rs.getString("subTitle");
				float orignalPrice=rs.getFloat("orignalPrice");
				float promotePrice=rs.getFloat("promotePrice");
				int stock=rs.getInt("stock");
				Date createDate=DateUtil.t2d(rs.getTimestamp("createDate"));
				Category category=new CategoryDAO().get(cid);
				
				bean.setName(name);
				bean.setSubTitle(subTitle);
				bean.setOrignalPrice(orignalPrice);
				bean.setPromotePrice(promotePrice);
				bean.setStock(stock);
				bean.setCreateDate(createDate);
				bean.setId(id);
				bean.setCategory(category);
				setFirstProductImage(bean);
				beans.add(bean);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return beans;
	}
}
