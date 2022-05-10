package extra.dialog;

import javax.swing.JPanel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

class MyDialogWindow extends JDialog {

	private static final long serialVersionUID = 1L;

	private int _status;
	private JComboBox<Dish> _dishes;//lo tendremos con vehiculos con carreteras...
	private DefaultComboBoxModel<Dish> _dishesModel;//este modelo refresca automaticamente a diferencia del jtablemodel creado por nosotros

	public MyDialogWindow(Frame parent) {
		//siempre es modal, tiene mas sentido para un jdialog
		super(parent, true);//el segundo argumento indica q la ventana es modal (q hasta q se acepte o se cancele no me deja interactuar con la anterior)
		initGUI();
	}

	private void initGUI() {

		_status = 0;//para q si cierro el cuadro de dialogo sin pulsar a los botones salga tambien el status de 0 por defecto

		setTitle("Food Selector");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);

		JLabel helpMsg = new JLabel("Select your favorite");
		helpMsg.setAlignmentX(CENTER_ALIGNMENT);

		mainPanel.add(helpMsg);

		//para que no salga pegado al "Select your favorite"
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel viewsPanel = new JPanel();
		viewsPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(viewsPanel);

		//creamos el modelo y luego se lo paso al constructor del JComboBox
		// analogo al JTable, pero en este caso el modelo refresca,
		//el modelo tiene datos como addElement(E), 
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(buttonsPanel);

		_dishesModel = new DefaultComboBoxModel<>();
		_dishes = new JComboBox<>(_dishesModel);

		viewsPanel.add(_dishes);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_status = 0;
				MyDialogWindow.this.setVisible(false);//al cancelar hago q ya no sea visible
			}
		});
		buttonsPanel.add(cancelButton);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (_dishesModel.getSelectedItem() != null) {
					_status = 1;
					MyDialogWindow.this.setVisible(false);
				}
			}
		});
		buttonsPanel.add(okButton);

		setPreferredSize(new Dimension(500, 200));
		pack();
		setResizable(false);//no permite al usuario agrandar la ventana
		setVisible(false);//en el constructor no se hace visible todavia
	}

	public int open(List<Dish> dishes) {

		// update the comboxBox model -- if you always use the same no
		// need to update it, you can initialize it in the constructor.
		//
		/*
		 * en este ejemplo los datos q saca el dialog son siempre los mismos platos per oen nuestra practica
		 * los datos cambian si cambio el fichero y hay q hacerlo como en las prox 3 lineas. Hay q ponerlas en nuestro open 
		 * del jdialog:
		 */
		_dishesModel.removeAllElements();
		for (Dish v : dishes)
			_dishesModel.addElement(v);

		// You can chenge this to place the dialog in the middle of the parent window.
		// It can be done using uing getParent().getWidth, this.getWidth(),
		// getParent().getHeight, and this.getHeight(), etc.
		//
		setLocation(getParent().getLocation().x + 10, getParent().getLocation().y + 10);

		setVisible(true);//aqui se para para q el usuario interactue, se hace visible aqui, NO EN LA CONSTRUCTORA para que esté refrescada
		return _status;
	}

	Dish getDish() {
		return (Dish) _dishesModel.getSelectedItem();
	}

}
