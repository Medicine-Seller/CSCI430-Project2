import java.util.*;

public class Invoice {
	private float total = 0;
	private int clientId = 0;
	private List<Entry> entries = new LinkedList<Entry>();
	private List<OrderRequest> orderRequests = new LinkedList<OrderRequest>();

	public Invoice(int clientId) {
	}

	public void addEntry(Entry entry) {
		total += entry.getProduct().getPrice() * entry.getQuantity();
		entries.add(entry);
	}

	public void addOrderRequest(OrderRequest order) {
		orderRequests.add(order);
	}

	public Iterator<Entry> getEntries() {
		return entries.iterator();
	}

	public Iterator<OrderRequest> getOrderRequest() {
		return orderRequests.iterator();
	}

	public int getClientId() {
		return clientId;
	}

	public float getTotal() {
		return total;
	}

	public String toString() {
		String str = "Client ID# " + getClientId();

		Iterator<Entry> iterEnt = getEntries();
		while (iterEnt.hasNext()) {
			Entry e = iterEnt.next();
			str += "\n	" + e.getProduct().getName() + " x " + e.getQuantity() + " = $"
					+ e.getProduct().getPrice() * e.getQuantity();
		}

		Iterator<OrderRequest> iterOrder = getOrderRequest();
		while (iterOrder.hasNext()) {
			OrderRequest e = iterOrder.next();
			str += "\n(Waitlisted) " + e.getProduct().getName() + " x " + e.getQuantity();
		}

		str += "\n\nTotal cost: $" + getTotal();
		str += "\nClient balance: $" + Warehouse.instance().getClient(getClientId()).getBalance();
		return str;
	}

	public void addToInvoiceWithQuantity(Product product, int quantity) {

		if (product.getQuantity() - quantity < 0) {
			int q = quantity - product.getQuantity();
			quantity -= q;
			OrderRequest order = new OrderRequest(clientId, q, product);
			addOrderRequest(order);
		}

		Entry entry = new Entry(product, quantity);
		addEntry(entry);
	}

}
