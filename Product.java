import java.util.*;

public class Product {
	private int id;
	private String name;
	private float price;
	private int quantity = 0;
	private static int idCounter = 0;
	private Waitlist waitlist = new Waitlist();

	Product(String name, float price, int quantity) {
		this.id = idCounter++;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public float getPrice() {
		return price;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void addQuantity(int quantity) {
		this.quantity += quantity;
	}

	public int getQuantity() {
		return quantity;
	}

	public Iterator<OrderRequest> getOrderRequests() {
		return waitlist.getOrders();
	}

	public void addOrderRequest(OrderRequest order) {
		waitlist.add(order);
	}

	public void subOrderRequest(OrderRequest order) {
		waitlist.sub(order);
	}

}
