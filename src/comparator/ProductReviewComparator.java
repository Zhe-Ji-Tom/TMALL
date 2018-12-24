/**
 * @author Tom
 * 2018.12.24
 * �����۶�ķ�ǰ��
 */
package comparator;

import java.util.Comparator;

import bean.Product;

public class ProductReviewComparator implements Comparator<Product>{

	@Override
	public int compare(Product p1,Product p2) {
		return (int)(p2.getReviewCount()-p1.getReviewCount());
	}
}
