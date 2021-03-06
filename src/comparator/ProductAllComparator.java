/**
 * @author Tom
 * 2018.12.24
 * 把销量*评价高的放前面
 */
package comparator;

import java.util.Comparator;

import bean.Product;

public class ProductAllComparator implements Comparator<Product>{

	@Override
	public int compare(Product p1,Product p2) {
		return p2.getReviewCount()*p2.getSaleCount()-p1.getReviewCount()*p1.getSaleCount();
	}
}
