
public class OrderRequest {
	private int clientId;
	private int quantity;
	private Product product = null;

	public OrderRequest(int id, int q) {

		clientId = id;
		quantity = q;
	}

	public OrderRequest(int id, int q, Product product) {
		this.product = product;
		clientId = id;
		quantity = q;
	}

	public void add(int q) {
		quantity += q;
	}

	public void sub(int q) {
		quantity -= q;
	}

	public int getClientId() {
		return clientId;
	}

	public int getQuantity() {
		return quantity;
	}

	public Product getProduct() {
		return product;
	}
}
