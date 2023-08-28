import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WarehouseContext {
	private int currentState;
	private static WarehouseContext context;
	private int currentUserType;
	private String currentClientId;
	public static final int IsClient = 0;
	public static final int IsClerk = 1;
	public static final int IsManager = 2;
	private static JFrame warehouseFrame;
	private WarehouseState[] states;
	private int[][] nextState;

	public void setUserType(int code) {
		currentUserType = code;
	}

	public static JFrame getFrame() {
		return warehouseFrame;
	}

	public void setClientId(String id) {
		currentClientId = id;
	}

	public int getUserType() {
		return currentUserType;
	}

	public String getClientId() {
		return currentClientId;
	}

	private WarehouseContext() {
		states = new WarehouseState[6];
		states[0] = ClientMenuState.getInstance();
		states[1] = ClerkState.getInstance();
		states[2] = ManagerMenuState.getInstance();
		states[3] = LoginState.instance();
		states[4] = CartState.getInstance();
		states[5] = QueryState.getInstance();
		nextState = new int[6][4];

		// Transitions for client
		nextState[0][0] = 3;
		nextState[0][1] = 1;
		nextState[0][2] = 4;
		nextState[0][3] = -2;

		// Transitions for clerk
		nextState[1][0] = 3;
		nextState[1][1] = 2;
		nextState[1][2] = 0;
		nextState[1][3] = 5;

		// Transitions for manager
		nextState[2][0] = 3;
		nextState[2][1] = 1;
		nextState[2][2] = -2;
		nextState[2][3] = -2;

		// Transitions for login
		nextState[3][0] = 0;
		nextState[3][1] = 1;
		nextState[3][2] = 2;
		nextState[3][3] = -2;

		//Transitions for cart state
		nextState[4][0] = 0;
		nextState[4][1] = -2;
		nextState[4][2] = -2;
		nextState[4][3] = -2;

		//Transitions for query state
		nextState[5][0] = 1;
		nextState[5][1] = -2;
		nextState[5][2] = -2;
		nextState[5][3] = -2;
		
		currentState = 3;
	}

	public void changeState(int transition) {
		currentState = nextState[currentState][transition];
		if (currentState == -2) {
			System.out.println("Transition error!");
			System.exit(0);
		}

		if (currentState == -1)
			System.exit(0);

		states[currentState].run();
	}

	public static WarehouseContext getInstance() {
		if (context == null) {
			context = new WarehouseContext();
		}

		return context;
	}

	public void process() {
		Warehouse warehouse = Warehouse.instance();

		warehouse.addClient("Bob", "438 Eigth St. South");
		warehouse.addClient("George", "916 Ave. West");
		warehouse.addClient("Fiona", "699 Lincoln Street");
		warehouse.addClient("Charles", "712 Kang St. North");
		warehouse.addClient("Xiang", "980 Saint Street");
		warehouse.addClient("Abdul", "125 North Ave.");

		warehouse.addProduct("Cheese", 2.00f, 10);
		warehouse.addProduct("Milk", 2.50f, 25);
		warehouse.addProduct("Ramen", 0.45f, 10);
		warehouse.addProduct("Shampoo", 1.25f, 10);
		warehouse.addProduct("Toothpaste", 1.00f, 32);
		warehouse.addProduct("Magazine", 0.05f, 241);
		warehouse.addProduct("Paper", 0.025f, 412);
		warehouse.addProduct("Wood", 0.85f, 421);
		warehouse.addProduct("Cereal", 1.85f, 251);
		warehouse.addProduct("Soft Drinks", 1.45f, 521);
		
		warehouse.addProductToClientWishlist(0, "Cheese", 15);
		warehouse.addProductToClientWishlist(0, "Milk", 7);
		warehouse.addProductToClientWishlist(0, "Ramen", 10);
		
		warehouse.addProductToClientWishlist(1, "Wood", 115);
		warehouse.addProductToClientWishlist(1, "Milk", 72);
		warehouse.addProductToClientWishlist(1, "Shampoo", 10);
		warehouse.addProductToClientWishlist(1, "Ramen", 10);
		
		warehouse.addProductToClientWishlist(2, "Cereal", 3);
		warehouse.addProductToClientWishlist(2, "Toothpaste", 21);
		warehouse.addProductToClientWishlist(2, "Cheese", 8);
		warehouse.addProductToClientWishlist(2, "Milk", 5);

		warehouse.addBalanceToClientId(0, 100);
		warehouse.addBalanceToClientId(1, 1000);
		warehouse.addBalanceToClientId(2, 250);
		warehouse.addBalanceToClientId(3, -100);
		warehouse.addBalanceToClientId(4, -45);
		warehouse.addBalanceToClientId(5, -586);

		warehouseFrame = new JFrame("Warehouse GUI");
		warehouseFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		warehouseFrame.setLocation(100, 100);
		warehouseFrame.setResizable(false);
		warehouseFrame.setSize(512, 512);
		warehouseFrame.setVisible(true);

		states[currentState].run();
	}

	public static void main(String[] args) {
		WarehouseContext.getInstance().process();
	}

}
