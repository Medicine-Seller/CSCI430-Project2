import java.util.*;
import java.util.List;

public class ProductList {
	private List<Product> products = new LinkedList<Product>();
	private static ProductList productList;

	private ProductList() {
	}

	public static ProductList getInstance() {
		if (productList == null)
			productList = new ProductList();

		return productList;
	}

	void addProduct(Product product) {
		products.add(product);
	}

	public Iterator<Product> getProducts() {
		return products.iterator();
	}
}
