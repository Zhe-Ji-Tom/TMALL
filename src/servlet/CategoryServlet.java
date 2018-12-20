/**
 * @author Tom
 * 2018.12.20
 * category类的servlet
 */
package servlet;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Category;
import util.ImageUtil;
import util.Page;

public class CategoryServlet extends BaseBackServlet{
	
	//增加一个category项目
	public String add(HttpServletRequest request,HttpServletResponse response,Page page) {
		Map<String,String> params=new HashMap<>();
		InputStream is=super.parseUpload(request,params);
		
		String name=params.get("name");
		Category c=new Category();
		c.setName(name);
		categoryDAO.add(c);
		
		File imageFolder=new File(request.getSession().getServletContext().getRealPath("img/category"));
		File file=new File(imageFolder,c.getId()+".jpg");
		
		try {
			if(null!=is && 0!=is.available()) {
				try(FileOutputStream fos=new FileOutputStream(file)){
					byte b[]=new byte[1024*1024];
					int length=0;
					while(-1!=(length=is.read(b))) {
						fos.write(b, 0, length);
					}
					fos.flush();
					//通过如下代码，把文件保存为jpg格式
					BufferedImage img=ImageUtil.change2jpg(file);
					ImageIO.write(img, "jpg", file);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		return "@admin_category_list";
	}
	
	//删除一个category项目
	public String delete(HttpServletRequest request,HttpServletResponse response,Page page) {
		int id=Integer.parseInt(request.getParameter("id"));
		categoryDAO.delete(id);
		return "@admin_cagegory_list";
	}
	
	//编辑一个category项目
	public String edit(HttpServletRequest request,HttpServletResponse response,Page page) {
		int id=Integer.parseInt(request.getParameter("id"));
		Category c=categoryDAO.get(id);
		request.setAttribute("c", c);
		return "admin/editCategory.jsp";
	}
	
	//更新一个category项目
	public String update(HttpServletRequest request,HttpServletResponse response,Page page) {
		Map<String,String> params=new HashMap<>();
		InputStream is=super.parseUpload(request, params);
		
		System.out.println(params);
		String name=params.get("name");
		int id=Integer.parseInt(params.get("id"));
		
		Category c=new Category();
		c.setId(id);
		c.setName(name);
		categoryDAO.update(c);
		
		File imageFolder=new File(request.getSession().getServletContext().getRealPath("img/category"));
		File file=new File(imageFolder,c.getId()+".jpg");
		file.getParentFile().mkdirs();
		
		try {
			if(null!=is && 0!=is.available()) {
				try(FileOutputStream fos=new FileOutputStream(file)){
					byte b[]=new byte[1024*1024];
					int length=0;
					while(-1!=(length=is.read(b))) {
						fos.write(b, 0, length);
					}
					fos.flush();
					//通过如下代码，把文件保存为jpg格式
					BufferedImage img=ImageUtil.change2jpg(file);
					ImageIO.write(img, "jpg", file);
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		return "@admin_category_list";
	}
	
	//列举category项目
	public String list(HttpServletRequest request,HttpServletResponse response,Page page) {
		List<Category> cs=categoryDAO.list(page.getStart(),page.getCount());
		int total=categoryDAO.getTotal();
		page.setTotal(total);
		
		request.setAttribute("thecs", cs);
		request.setAttribute("page", page);
		
		return "@admin/listCategory.jsp";
	}
}
