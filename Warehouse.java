import java.util.*;

public class Warehouse {
	private static Warehouse warehouse;
	private static ClientList clientList;
	private static ProductList productList;
	private float balance = 100000;

	private Warehouse() {
		productList = ProductList.getInstance();
		clientList = ClientList.instance();
	}

	public static Warehouse instance() {
		if (warehouse == null)
			return warehouse = new Warehouse();
		else
			return warehouse;
	}

	public Client getClient(int clientId) {
		Iterator<Client> iterClient = clientList.getMembers();

		while (iterClient.hasNext()) {
			Client c = iterClient.next();

			if (c.getId() == clientId)
				return c;
		}

		return null;
	}

	public Product getProduct(int productId) {
		Iterator<Product> iterProduct = productList.getProducts();

		while (iterProduct.hasNext()) {
			Product p = iterProduct.next();

			if (p.getId() == productId)
				return p;
		}

		return null;
	}

	public void addBalance(float n) {
		balance += n;
	}

	public void removeEntryFromClient(Client client, int productId) {
		client.removeEntry(productId);
	}

	public Iterator<Client> getClients() {
		return clientList.getMembers();
	}

	public Iterator<Product> getProducts() {
		return productList.getProducts();
	}

	public Product getProduct(String productName) {
		Iterator<Product> iterProd = productList.getProducts();

		while (iterProd.hasNext()) {
			Product p = iterProd.next();
			if (p.getName() == productName)
				return p;
		}

		return null;
	}

	public Client addClient(String name, String address) {
		Client c = new Client(name, address);
		clientList.insertClient(c);
		return null;
	}

	public void addProduct(String name, float d, int quantity) {
		Product product = new Product(name, d, quantity);
		productList.addProduct(product);
	}

	public void addProductQuantity(String name, int quantity) {
		Product product = getProduct(name);
		product.addQuantity(quantity);
	}

	public void addProductToClientWishlist(int clientId, String productName, int quantity) {
		Client client = getClient(clientId);
		if (client == null)
			return;

		Product product = getProduct(productName);
		if (product == null)
			return;

		Entry entry = new Entry(product, quantity);
		client.addEntry(entry);
	}

	public void addBalanceToClientId(int clientId, float b) {
		Client client = getClient(clientId);
		client.addBalance(b);
	}

	public boolean processInvoice(Invoice invoice) {
		Client client = warehouse.getClient(invoice.getClientId());

		if (client == null)
			return false;

		Iterator<Entry> iterEnt = invoice.getEntries();
		while (iterEnt.hasNext()) {
			Entry e = iterEnt.next();
			client.subEntry(e);

			e.getProduct().addQuantity(-e.getQuantity());

			Product product = e.getProduct();
			Iterator<OrderRequest> iterOrder = product.getOrderRequests();

			while (iterOrder.hasNext()) {
				OrderRequest o = iterOrder.next();

				if (o.getClientId() == client.getId()) {
					OrderRequest order = new OrderRequest(client.getId(), e.getQuantity());
					product.subOrderRequest(order);
				}
			}
		}

		Iterator<OrderRequest> iterOrder = invoice.getOrderRequest();
		while (iterOrder.hasNext()) {
			OrderRequest o = iterOrder.next();
			Product p = o.getProduct();
			if (p != null)
				p.addOrderRequest(o);

			Entry e = new Entry(p, o.getQuantity());
			client.subEntry(e);
		}

		client.addBalance(-invoice.getTotal());
		client.addInvoice(invoice);

		return true;
	}

}
