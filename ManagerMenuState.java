import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ManagerMenuState extends WarehouseState implements ActionListener {
	private static Warehouse warehouse;
	private static ManagerMenuState instance;
	private static JFrame frame;
	private AbstractButton 	button_logout, 
							button_addProduct, 
							button_receiveShipment, 
							button_becomeClerk;

	private ManagerMenuState() {
		warehouse = Warehouse.instance();

		button_logout = new JButton("Logout");
		button_addProduct = new JButton("Add Product");
		button_receiveShipment = new JButton("Receive Shipment");
		button_becomeClerk = new JButton("Become Clerk");

		button_logout.addActionListener(this);
		button_addProduct.addActionListener(this);
		button_receiveShipment.addActionListener(this);
		button_becomeClerk.addActionListener(this);
	}

	public void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(this.button_logout))
			logout();
		else if (event.getSource().equals(this.button_addProduct))
			addProduct();
		else if (event.getSource().equals(this.button_receiveShipment))
			receiveShipment();
		else if (event.getSource().equals(this.button_becomeClerk))
			clerkMenu();
	}

	public void run() {
		frame = WarehouseContext.getFrame();
		frame.getContentPane().removeAll();
		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(this.button_logout);
		frame.getContentPane().add(this.button_addProduct);
		frame.getContentPane().add(this.button_receiveShipment);
		frame.getContentPane().add(this.button_becomeClerk);
		frame.setVisible(true);
		frame.paint(frame.getGraphics());
		frame.toFront();
		frame.requestFocus();
	}

	public static ManagerMenuState getInstance() {
		if (instance == null)
			instance = new ManagerMenuState();

		return instance;
	}

	public void addProduct() {
		String inputProductName = JOptionPane.showInputDialog("Product Name:");
		String inputProductPrice = JOptionPane.showInputDialog("Product Price $:");
		String inputProductQuantity = JOptionPane.showInputDialog("Product Quantity:");

		if (inputProductName.isEmpty() || inputProductPrice.isEmpty() || inputProductQuantity.isEmpty()) {
			JOptionPane.showMessageDialog(frame, "Invalid input!");
			return;
		}

		warehouse.addProduct(inputProductName, Float.parseFloat(inputProductPrice),Integer.parseInt(inputProductQuantity));
		JOptionPane.showMessageDialog(frame, "Product Added!");
	}

	public void receiveShipment() {
		warehouse.addProduct("Avocado", 1.50f, 1000);
		warehouse.addProduct("Cherry", 0.70f, 530);
		warehouse.addProduct("Beef", 4.50f, 1050);
		warehouse.addProduct("Orange", 0.50f, 1100);
		warehouse.addProduct("Steak", 4.50f, 421);
		warehouse.addProduct("Banana", 0.60f, 520);
		warehouse.addProduct("Cotton", 0.20f, 2100);
		warehouse.addProduct("Silk", 0.40f, 241);
		JOptionPane.showMessageDialog(frame, "Shipment Added!");
	}

	public void clerkMenu() {
		WarehouseContext warehouseContext = WarehouseContext.getInstance();
		warehouseContext.setUserType(WarehouseContext.IsManager);
		warehouseContext.changeState(1);
	}

	public void logout() {
		WarehouseContext.getInstance().changeState(0);
	}

}