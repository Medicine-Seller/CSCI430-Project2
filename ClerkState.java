import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ClerkState extends WarehouseState implements ActionListener {
	private static Warehouse warehouse;
	public static ClerkState instance;
	private JTextArea textArea;
	private static JFrame frame;
	private AbstractButton 	button_logout, 
							button_receivePayment, 
							button_showProducts, 
							button_showProductWaitlist,
							button_queryClients, 
							button_becomeClient;

	private ClerkState() {
		warehouse = Warehouse.instance();
		textArea = new JTextArea();

		button_logout = new JButton("Logout");
		button_receivePayment = new JButton("Receive Payment");
		button_showProducts = new JButton("Show Products");
		button_showProductWaitlist = new JButton("Display Product Waitlist");
		button_queryClients = new JButton("Query Clients");
		button_becomeClient = new JButton("Become Client");

		button_logout.addActionListener(this);
		button_receivePayment.addActionListener(this);
		button_showProducts.addActionListener(this);
		button_showProductWaitlist.addActionListener(this);
		button_queryClients.addActionListener(this);
		button_becomeClient.addActionListener(this);
	}

	public void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(this.button_logout))
			logout();
		else if (event.getSource().equals(this.button_receivePayment))
			receivePayment();
		else if (event.getSource().equals(this.button_showProducts))
			displayProducts();
		else if (event.getSource().equals(this.button_showProductWaitlist))
			displayProductWaitlist();
		else if (event.getSource().equals(this.button_queryClients))
			queryClients();
		else if (event.getSource().equals(this.button_becomeClient))
			clientMenu();
	}

	public void run() {
		textArea.setText("");
		frame = WarehouseContext.getFrame();
		frame.getContentPane().removeAll();
		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(this.button_logout);
		frame.getContentPane().add(this.button_receivePayment);
		frame.getContentPane().add(this.button_showProducts);
		frame.getContentPane().add(this.button_showProductWaitlist);
		frame.getContentPane().add(this.button_queryClients);
		frame.getContentPane().add(this.button_becomeClient);
		frame.getContentPane().add(this.textArea);
		frame.setVisible(true);
		frame.paint(frame.getGraphics());
		frame.toFront();
		frame.requestFocus();

		frame.setLayout(null);
		textArea.setLocation(12, 100);
		textArea.setSize(488, 400);
	}

	public void updateText(String str) {
		textArea.setText(str);

		frame.setVisible(true);
	}

	public static ClerkState getInstance() {
		if (instance == null)
			instance = new ClerkState();

		return instance;
	}

	public void queryClients() {
		WarehouseContext.getInstance().changeState(3);
	}

	public void displayProductWaitlist() {
		String inputProductId = JOptionPane.showInputDialog("Enter Product ID:");
		if (inputProductId.isEmpty()) {
			JOptionPane.showMessageDialog(frame, "Invalid input!");
			return;
		}

		Product product = warehouse.getProduct(Integer.parseInt(inputProductId));
		String productWaitlists = "Waitlist for PID " + inputProductId + ":";

		Iterator<OrderRequest> iterOrderRequest = product.getOrderRequests();

		while (iterOrderRequest.hasNext()) {
			OrderRequest o = iterOrderRequest.next();
			productWaitlists += "\n	ClientID: " + o.getClientId() + "	" + product.getName() + "	x "
					+ o.getQuantity();
		}

		updateText(productWaitlists);
	}

	public void displayProducts() {
		Iterator<Product> iterProd = ProductList.getInstance().getProducts();

		String productList = "Products in warehouse:";
		while (iterProd.hasNext()) {
			Product p = iterProd.next();
			productList += "\nPID:" + p.getId() + "	" + p.getName() + "	x " + p.getQuantity() + "	Price: $"
					+ p.getPrice();
		}

		updateText(productList);
	}

	public void addClient() {
		String inputClientName = JOptionPane.showInputDialog("Enter Name:");
		String inputClientAddress = JOptionPane.showInputDialog("Enter Name:");

		warehouse.addClient(inputClientName, inputClientAddress);
		JOptionPane.showMessageDialog(frame, "Client: " + inputClientName + " added!");
	}

	public void receivePayment() {
		String inputClientId = JOptionPane.showInputDialog("Enter Client ID:");
		Client client = Warehouse.instance().getClient(Integer.parseInt(inputClientId));
		if (inputClientId.isEmpty() || client == null) {
			JOptionPane.showMessageDialog(frame, "Can not find client ID# " + inputClientId);
			return;
		}

		String inputAmount = JOptionPane.showInputDialog("Enter Amount $:");
		if (inputAmount.isEmpty()) {
			JOptionPane.showMessageDialog(frame, "Amount is empty!");
			return;
		}

		float amount = Float.parseFloat(inputAmount);
		client.addBalance(-amount);
		Warehouse.instance().addBalance(amount);
		JOptionPane.showMessageDialog(frame, "Success!");
	}

	public void clientMenu() {
		WarehouseContext warehouseContext = WarehouseContext.getInstance();
		String clientID = JOptionPane.showInputDialog("Enter Client ID:");

		if (clientID.isEmpty() || Warehouse.instance().getClient(Integer.parseInt(clientID)) == null) {
			JOptionPane.showMessageDialog(frame, "Can not find client ID# " + clientID);
			return;
		}

		warehouseContext.setClientId(clientID);
		if (warehouseContext.getUserType() != WarehouseContext.IsManager)
			warehouseContext.setUserType(WarehouseContext.IsClerk);

		warehouseContext.changeState(2);
	}

	public void logout() {
		WarehouseContext warehouseContext = WarehouseContext.getInstance();
		int loginType = warehouseContext.getUserType();

		if (loginType == WarehouseContext.IsManager)
			warehouseContext.changeState(1);
		else if (loginType == WarehouseContext.IsClerk)
			warehouseContext.changeState(0);
		else
			warehouseContext.changeState(3);
	}

}
