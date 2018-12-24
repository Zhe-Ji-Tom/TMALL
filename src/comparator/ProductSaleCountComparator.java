/**
 * @author Tom
 * 2018.12.24
 * �������ߵķ�ǰ��
 */
package comparator;

import java.util.Comparator;

import bean.Product;

public class ProductSaleCountComparator implements Comparator<Product>{

	@Override
	public int compare(Product p1,Product p2) {
		return p2.getSaleCount()-p1.getSaleCount();
	}
}
