import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginState extends WarehouseState implements ActionListener {
	private static LoginState instance;
	private JFrame frame;
	private AbstractButton 	exitButton, 
							clientButton, 
							clerkButton, 
							managerButton;

	private LoginState() {
		super();
		exitButton = new JButton("Exit");
		clientButton = new JButton("Client");
		clerkButton = new JButton("Clerk");
		managerButton = new JButton("Manager");

		exitButton.addActionListener(this);
		clientButton.addActionListener(this);
		clerkButton.addActionListener(this);
		managerButton.addActionListener(this);
	}

	public void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(this.exitButton))
			System.exit(0);
		else if (event.getSource().equals(this.clientButton))
			client();
		else if (event.getSource().equals(this.clerkButton))
			clerk();
		else if (event.getSource().equals(this.managerButton))
			manager();
	}

	public static LoginState instance() {
		if (instance == null)
			instance = new LoginState();

		return instance;
	}

	private void clerk() {
		WarehouseContext warehouseContext = WarehouseContext.getInstance();
		warehouseContext.setUserType(WarehouseContext.IsClerk);
		warehouseContext.changeState(1);
	}

	private void client() {
		String clientID = JOptionPane.showInputDialog("Enter Client ID:");
		if (clientID.isEmpty() || Warehouse.instance().getClient(Integer.parseInt(clientID)) == null) {
			JOptionPane.showMessageDialog(frame, "Can not find client ID# " + clientID);
			return;
		}

		WarehouseContext warehouseContext = WarehouseContext.getInstance();
		warehouseContext.setClientId(clientID);
		warehouseContext.setUserType(WarehouseContext.IsClient);
		warehouseContext.changeState(0);
	}

	private void manager() {
		WarehouseContext warehouseContext = WarehouseContext.getInstance();
		warehouseContext.setUserType(WarehouseContext.IsManager);
		warehouseContext.changeState(2);
	}

	public void run() {
		frame = WarehouseContext.getFrame();
		frame.getContentPane().removeAll();
		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(this.exitButton);
		frame.getContentPane().add(this.clientButton);
		frame.getContentPane().add(this.clerkButton);
		frame.getContentPane().add(this.managerButton);
		frame.setVisible(true);
		frame.paint(frame.getGraphics());
		frame.toFront();
		frame.requestFocus();
	}
}
