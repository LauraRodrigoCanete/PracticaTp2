package extra.dialog;

import javax.swing.*;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class DialogWindowExample extends JFrame {
	//Jdialog: para cambiar el tiempo en la carretera y la cont en los vehiculos

	public DialogWindowExample() {
		super("Custom Dialog Example");
		initGUI();
	}

	private void initGUI() {

		JPanel mainPanel = new JPanel();
		this.setContentPane(mainPanel);

		mainPanel.add(new JLabel("Click "));
		JButton here = new JButton("HERE");
		here.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				select_food();
			}
		});
		mainPanel.add(here);
		mainPanel.add(new JLabel(" to select your food"));

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);

	}

	protected void select_food() {

		// if you're in a JPanel class, you can use the following
		//
		// (Frame) SwingUtilities.getWindowAncestor(this)
		//
		// in order to get the parent JFrame. Then pass it to the constructor
		// of MyDialogWindow instead of 'this'
		//

		MyDialogWindow dialog = new MyDialogWindow(this);//hay q crearlo

		List<Dish> dishes = new ArrayList<Dish>();
		for (int i = 0; i < 10; i++) {
			dishes.add(new Dish("Yum Yum " + i));
		}

		//sin esta linea no es visible, se hace visible en el open
		int status = dialog.open(dishes);//hay q abrirlo con los datos, en la pr cada vez q hago el open se abrirá con los datos q haya luego siempre tiene q estar actualizado para no sacar datos antiguos

		if (status == 0) {
			System.out.println("Canceled");
		} else {
			System.out.println("Your favorite dish is: " + dialog.getDish());
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new DialogWindowExample();
			}
		});
	}
}