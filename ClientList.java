import java.util.*;

public class ClientList {
	private List<Client> clients = new LinkedList<Client>();
	private static ClientList clientList;

	public static ClientList instance() {
		if (clientList == null)
			clientList = new ClientList();

		return clientList;
	}

	public boolean insertClient(Client Client) {
		clients.add(Client);
		return true;
	}

	public Iterator<Client> getMembers() {
		return clients.iterator();
	}

}
