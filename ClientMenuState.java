import java.util.*;
import javax.swing.AbstractButton;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ClientMenuState extends WarehouseState implements ActionListener {
	private static Warehouse warehouse;
	private static ClientMenuState instance;
	private JFrame frame;
	private JTextArea textArea;
	private AbstractButton 	button_logout, 
							button_clientInfo, 
							button_listProducts, 
							button_showTransactions, 
							button_cart,
							button_showWaitlist, 
							button_order;

	private ClientMenuState() {
		warehouse = Warehouse.instance();
		textArea = new JTextArea();

		button_logout = new JButton("Logout");
		button_clientInfo = new JButton("My Info");
		button_listProducts = new JButton("Show Products");
		button_showWaitlist = new JButton("My waitlist");
		button_showTransactions = new JButton("Show my transactions");
		button_cart = new JButton("Go to cart");
		button_order = new JButton("Order");

		button_logout.addActionListener(this);
		button_clientInfo.addActionListener(this);
		button_listProducts.addActionListener(this);
		button_showWaitlist.addActionListener(this);
		button_showTransactions.addActionListener(this);
		button_cart.addActionListener(this);
		button_order.addActionListener(this);
	}

	public void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(this.button_logout))
			logout();
		else if (event.getSource().equals(this.button_clientInfo))
			showClientDetails();
		else if (event.getSource().equals(this.button_listProducts))
			showProducts();
		else if (event.getSource().equals(this.button_showWaitlist))
			showClientWaitlist();
		else if (event.getSource().equals(this.button_showTransactions))
			showClientTransactions();
		else if (event.getSource().equals(this.button_cart))
			updateShoppingCart();
		else if (event.getSource().equals(this.button_order))
			placeOrder();
	}

	public void run() {
		textArea.setText("");
		frame = WarehouseContext.getFrame();
		frame.getContentPane().removeAll();
		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(this.button_logout);
		frame.getContentPane().add(this.button_clientInfo);
		frame.getContentPane().add(this.button_listProducts);
		frame.getContentPane().add(this.button_showWaitlist);
		frame.getContentPane().add(this.button_showTransactions);
		frame.getContentPane().add(this.button_cart);
		frame.getContentPane().add(this.button_order);
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

	public static ClientMenuState getInstance() {
		if (instance == null)
			instance = new ClientMenuState();

		return instance;
	}

	public void updateShoppingCart() {
		WarehouseContext.getInstance().changeState(2);
	}

	public void showClientDetails() {
		int clientId = Integer.parseInt(WarehouseContext.getInstance().getClientId());
		Client client = warehouse.getClient(clientId);

		String clientInfoString = "My information:" + "\nID:	" + client.getId() + "\nName:	" + client.getName()
				+ "\nAddress:	" + client.getAddress() + "\nBalance:	$" + client.getBalance();

		updateText(clientInfoString);
	}

	public void showProducts() {
		Iterator<Product> iterProd = ProductList.getInstance().getProducts();

		String productList = "Products in warehouse:";
		while (iterProd.hasNext()) {
			Product p = iterProd.next();
			productList += "\nID:" + p.getId() + "	" + p.getName() + "	Price: $" + p.getPrice();
		}

		updateText(productList);
	}

	public void showClientTransactions() {
		int clientId = Integer.parseInt(WarehouseContext.getInstance().getClientId());
		Client client = warehouse.getClient(clientId);

		Iterator<Invoice> iterTrans = client.getTransactions();

		String transactions = "My transactions:";
		while (iterTrans.hasNext()) {
			Invoice v = iterTrans.next();

			transactions += "\n-$" + v.getTotal();
		}

		updateText(transactions);
	}

	public void placeOrder() {
		int clientId = Integer.parseInt(WarehouseContext.getInstance().getClientId());

		Iterator<Entry> iterWish = warehouse.getClient(clientId).getWishlist();
		if (!iterWish.hasNext()) {
			JOptionPane.showMessageDialog(frame, "Nothing in cart!");
			return;
		}

		Invoice invoice = new Invoice(clientId);

		boolean exit = false;
		while (iterWish.hasNext() && !exit) {
			Entry e = iterWish.next();
			switch (JOptionPane.showConfirmDialog(frame, "Purchase cart item - " + e.getProduct().getName() + "?")) {
			case JOptionPane.OK_OPTION:

				switch (JOptionPane.showConfirmDialog(frame, "Purchase " + e.getProduct().getName() + " x "
						+ e.getQuantity() + "?\nPress No to change quantity.")) {
				case JOptionPane.OK_OPTION:
					invoice.addToInvoiceWithQuantity(e.getProduct(), e.getQuantity());
					break;
				case JOptionPane.NO_OPTION:
					int newQuantity = Integer.parseInt(JOptionPane.showInputDialog("Enter new quantity:"));
					invoice.addToInvoiceWithQuantity(e.getProduct(), newQuantity);
					break;
				case JOptionPane.CANCEL_OPTION:
					break;
				}

				break;
			case JOptionPane.NO_OPTION:
				continue;
			case JOptionPane.CANCEL_OPTION:
				exit = true;
				break;
			}
		}

		if (exit)
			return;

		String str = invoice.toString();
		str += "\n\nFinalize order?";
		switch (JOptionPane.showConfirmDialog(frame, str)) {
		case JOptionPane.OK_OPTION:
			if (!warehouse.processInvoice(invoice))
				JOptionPane.showMessageDialog(frame, "Insufficient fund!");
			else
				JOptionPane.showMessageDialog(frame, "Success!");
		case JOptionPane.NO_OPTION:
			break;
		case JOptionPane.CANCEL_OPTION:
			break;
		}
	}

	public void showClientWaitlist() {
		int clientId = Integer.parseInt(WarehouseContext.getInstance().getClientId());

		String clientWaitlist = "Client waitlist:";

		Iterator<Product> iterProd = warehouse.getProducts();
		while (iterProd.hasNext()) {
			Product p = iterProd.next();
			Iterator<OrderRequest> iterOrders = p.getOrderRequests();

			while (iterOrders.hasNext()) {
				OrderRequest o = iterOrders.next();
				if (o.getClientId() == clientId)
					clientWaitlist += "\nPID:" + p.getId() + "	" + p.getName() + "	x " + o.getQuantity();
			}
		}

		updateText(clientWaitlist);
	}

	public void logout() {
		int userLoginContext = WarehouseContext.getInstance().getUserType();
		if (userLoginContext == WarehouseContext.IsClient)
			WarehouseContext.getInstance().changeState(0);
		else
			WarehouseContext.getInstance().changeState(1);
	}

}