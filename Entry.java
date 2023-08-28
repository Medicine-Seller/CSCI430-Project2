
public class Entry {
	private int quantity;
	private Product product;

	public Entry(Product p, int q) {
		product = p;
		quantity = q;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int q) {
		quantity = q;
	}

	public Product getProduct() {
		return product;
	}

	public void add(int q) {
		quantity += q;
	}

	public void sub(int q) {
		quantity -= q;
	}
}
