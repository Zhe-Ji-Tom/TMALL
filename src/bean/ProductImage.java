/**
 * @author Tom
 * 2018.12.14
 * ��ƷͼƬ
 */
package bean;

public class ProductImage {
	private String type;
	private Product product;
	private int id;
	
	public int getId() {
        return id;
    }
     
    public void setId(int id) {
        this.id = id;
    }
 
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }
}
