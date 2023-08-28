import java.util.*;

public class Waitlist {
	private List<OrderRequest> orders = new LinkedList<OrderRequest>();

	private int getIndexIfClientExist(int clientId) {
		for (int i = 0; i < orders.size(); i++)
			if (orders.get(i).getClientId() == clientId)
				return i;

		return -1;
	}

	public boolean isEmpty() {
		return orders.isEmpty();
	}

	public void add(OrderRequest order) {
		int index = getIndexIfClientExist(order.getClientId());

		if (index == -1) {
			orders.add(order);
			return;
		}

		orders.get(index).add(order.getQuantity());
	}

	public Iterator<OrderRequest> getOrders() {
		return orders.iterator();
	}

	public void remove(OrderRequest order) {
		int index = getIndexIfClientExist(order.getClientId());
		if (index == -1)
			return;

		orders.remove(index);
	}

	public OrderRequest getOrderFromClient(int clientId) {
		int index = getIndexIfClientExist(clientId);

		if (index == -1)
			return null;

		return orders.get(index);
	}

	public void sub(OrderRequest order) {
		int index = getIndexIfClientExist(order.getClientId());

		if (index == -1)
			return;

		orders.get(index).sub(order.getQuantity());

		if (orders.get(index).getQuantity() <= 0)
			orders.remove(index);

		return;

	}
}
