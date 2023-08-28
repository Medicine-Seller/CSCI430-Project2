import java.util.*;

public class Client {
	String clientName;
	private String clientAddress;
	private int id = 0;
	private static int idCounter = 0;
	private Wishlist wishlist;
	private float balance = 0;
	private List<Invoice> transactions = new LinkedList<Invoice>();

	public Client(String clientName, String clientAddress) {
		this.clientName = clientName;
		this.clientAddress = clientAddress;
		id = idCounter++;
		wishlist = new Wishlist();
	}

	public void setBalance(float b) {
		balance = b;
	}

	public float getBalance() {
		return balance;
	}

	public void addBalance(float b) {
		balance += b;
	}

	public String getName() {
		return clientName;
	}

	public String getAddress() {
		return clientAddress;
	}

	public int getId() {
		return id;
	}

	public void addEntry(Entry entry) {
		wishlist.addEntry(entry);
	}

	public boolean removeEntry(int entryId) {
		return wishlist.removeEntry(entryId);
	}

	public void subEntry(Entry entry) {
		wishlist.subEntry(entry);
	}

	public Iterator<Entry> getWishlist() {
		return wishlist.getEntries();
	}

	public void addInvoice(Invoice invoice) {
		transactions.add(invoice);
	}

	public Iterator<Invoice> getTransactions() {
		return transactions.iterator();
	}

}