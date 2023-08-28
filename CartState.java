import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CartState extends WarehouseState implements ActionListener {
	private static CartState cartState;
	private static Warehouse warehouse;
	private JFrame frame;
	private JTextArea textArea;
	private AbstractButton 	button_return, 
							button_viewCart, 
							button_addProduct, 
							button_removeProduct,
							button_changeQuantity;

	private CartState() {
		warehouse = Warehouse.instance();
		textArea = new JTextArea();

		button_return = new JButton("Return");
		button_viewCart = new JButton("View Cart");
		button_addProduct = new JButton("Add Product");
		button_removeProduct = new JButton("Remove Product");
		button_changeQuantity = new JButton("Change Quantity");

		button_return.addActionListener(this);
		button_viewCart.addActionListener(this);
		button_addProduct.addActionListener(this);
		button_removeProduct.addActionListener(this);
		button_changeQuantity.addActionListener(this);
	}

	public void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(this.button_return))
			exit();
		else if (event.getSource().equals(this.button_viewCart))
			viewCart();
		else if (event.getSource().equals(this.button_addProduct))
			addProduct();
		else if (event.getSource().equals(this.button_removeProduct))
			removeProduct();
		else if (event.getSource().equals(this.button_changeQuantity))
			changeQuantity();
	}

	public void run() {
		textArea.setText("");
		frame = WarehouseContext.getFrame();
		frame.getContentPane().removeAll();
		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(this.button_return);
		frame.getContentPane().add(this.button_viewCart);
		frame.getContentPane().add(this.button_addProduct);
		frame.getContentPane().add(this.button_removeProduct);
		frame.getContentPane().add(this.button_changeQuantity);
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

	public static CartState getInstance() {
		if (cartState == null)
			cartState = new CartState();

		return cartState;
	}

	public void exit() {
		WarehouseContext.getInstance().changeState(0);
	}

	public void changeQuantity() {
		int clientId = Integer.parseInt(WarehouseContext.getInstance().getClientId());
		Client client = Warehouse.instance().getClient(clientId);

		int productId = Integer.parseInt(JOptionPane.showInputDialog("Product ID:"));
		int newQuantity = Integer.parseInt(JOptionPane.showInputDialog("New Quantity:"));

		Iterator<Entry> iterEntry = client.getWishlist();

		boolean success = false;
		while (iterEntry.hasNext()) {
			Entry e = iterEntry.next();
			if (e.getProduct().getId() == productId) {
				e.setQuantity(newQuantity);
				success = true;
			}
		}

		if (success)
			JOptionPane.showMessageDialog(frame, "Product #" + productId + " updated!");
		else
			JOptionPane.showMessageDialog(frame, "Can not find product in cart!");
	}

	public void removeProduct() {
		int clientId = Integer.parseInt(WarehouseContext.getInstance().getClientId());
		Client client = Warehouse.instance().getClient(clientId);

		int productId = Integer.parseInt(JOptionPane.showInputDialog("Product ID:"));
		boolean success = client.removeEntry(productId);

		if (success)
			JOptionPane.showMessageDialog(frame, "Product #" + productId + " updated!");
		else
			JOptionPane.showMessageDialog(frame, "Can not find product in cart!");
	}

	public void addProduct() {
		int clientId = Integer.parseInt(WarehouseContext.getInstance().getClientId());
		Client client = Warehouse.instance().getClient(clientId);

		int productId = Integer.parseInt(JOptionPane.showInputDialog("Product ID:"));
		int quantity = Integer.parseInt(JOptionPane.showInputDialog("Quantity:"));

		Product p = Warehouse.instance().getProduct(productId);
		if (p != null) {
			Entry newEntry = new Entry(p, quantity);
			client.addEntry(newEntry);
			JOptionPane.showMessageDialog(frame, "Product #" + productId + " x " + quantity + " added!");
		} 
		else {
			JOptionPane.showMessageDialog(frame, "No such product exist!");
		}
	}

	public void viewCart() {
		int clientId = Integer.parseInt(WarehouseContext.getInstance().getClientId());
		Client client = warehouse.getClient(clientId);

		String cartProducts = "Products in cart:";

		Iterator<Entry> iterWish = client.getWishlist();
		while (iterWish.hasNext()) {
			Entry e = iterWish.next();
			cartProducts += "\nPID:" + e.getProduct().getId() + "	" + e.getProduct().getName() + "	x "
					+ e.getQuantity();
		}

		updateText(cartProducts);
	}

}
