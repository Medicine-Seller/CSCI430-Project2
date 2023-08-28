import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class QueryState extends WarehouseState implements ActionListener {
	private static QueryState queryState;
	private static JFrame frame;
	private JTextArea textArea;
	private AbstractButton 	button_return, 
							button_displayClients, 
							button_outstandingClients, 
							button_inactiveClients;

	private QueryState() {
		textArea = new JTextArea();

		button_return = new JButton("Return");
		button_displayClients = new JButton("Display Clients");
		button_outstandingClients = new JButton("Display Negative Balance Clients");
		button_inactiveClients = new JButton("Display /inactive Clients");

		button_return.addActionListener(this);
		button_displayClients.addActionListener(this);
		button_outstandingClients.addActionListener(this);
		button_inactiveClients.addActionListener(this);
	}

	public void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(this.button_return))
			exit();
		else if (event.getSource().equals(this.button_displayClients))
			displayClients();
		else if (event.getSource().equals(this.button_outstandingClients))
			displayOutstandingClients();
		else if (event.getSource().equals(this.button_inactiveClients))
			displayInactiveClients();
	}

	public void run() {
		textArea.setText("");
		frame = WarehouseContext.getFrame();
		frame.getContentPane().removeAll();
		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(this.button_return);
		frame.getContentPane().add(this.button_displayClients);
		frame.getContentPane().add(this.button_outstandingClients);
		frame.getContentPane().add(this.button_inactiveClients);
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

	public static QueryState getInstance() {
		if (queryState == null)
			queryState = new QueryState();

		return queryState;
	}

	public void exit() {
		WarehouseContext.getInstance().changeState(0);
	}

	public void displayClients() {
		Iterator<Client> iterClient = ClientList.instance().getMembers();
		String clientList = "Clients in warehouse:";

		while (iterClient.hasNext()) {
			Client c = iterClient.next();
			clientList += "\nID:" + c.getId() + "	" + c.getName() + "	Address: " + c.getAddress();
		}

		updateText(clientList);
	}

	public void displayOutstandingClients() {
		Iterator<Client> iterClient = Warehouse.instance().getClients();

		String clientList = "Clients with outstanding balance:";
		while (iterClient.hasNext()) {
			Client c = iterClient.next();
			if (c.getBalance() < 0) {
				clientList += "\nID:" + c.getId() + "	" + c.getName() + "	Balance: $" + c.getBalance();
			}
		}

		updateText(clientList);
	}

	public void displayInactiveClients() {
		Iterator<Client> iterClient = Warehouse.instance().getClients();

		String clientList = "Inactive Clients:";
		while (iterClient.hasNext()) {
			Client c = iterClient.next();

			Iterator<Invoice> iterTrans = c.getTransactions();
			if (!iterTrans.hasNext())
				clientList += "\nID:" + c.getId() + "	" + c.getName();
		}

		updateText(clientList);
	}

}
