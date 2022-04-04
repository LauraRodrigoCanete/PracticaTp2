	package simulator.view;

	

	import java.awt.Dimension;
	import java.awt.Frame;
	import java.awt.event.ActionEvent;
	import java.awt.event.ActionListener;
	import java.util.List;

	import javax.swing.Box;
	import javax.swing.BoxLayout;
	import javax.swing.DefaultComboBoxModel;
	import javax.swing.JButton;
	import javax.swing.JComboBox;
	import javax.swing.JDialog;
	import javax.swing.JLabel;
	import javax.swing.JPanel;
	import javax.swing.JSpinner;
	import javax.swing.SpinnerNumberModel;

	import simulator.misc.Pair;
	import simulator.model.Vehicle;


	public class ChangeContDialog extends JDialog{
		private int _status;
		private JComboBox<String> _vehicles;
		private JComboBox<Integer> _co2Class;
		private DefaultComboBoxModel<String> _vehiclesModel;
		private DefaultComboBoxModel<Integer> _co2ClassModel;//este modelo refresca automaticamente a diferencia del jtablemodel creado por nosotros
		private JSpinner _ticks;
		
		public ChangeContDialog(Frame frame) {
			//siempre es modal, tiene mas sentido para un jdialog
			super(frame, true);//el segundo argumento indica q la ventana es modal (q hasta q se acepte o se cancele no me deja interactuar con la anterior)
			initGUI();
		}

		private void initGUI() {

			_status = 0;//para q si cierro el cuadro de dialogo sin pulsar a los botones salga tambien el status de 0 por defecto

			setTitle("Change CO2 Class");
			JPanel mainPanel = new JPanel();
			mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
			setContentPane(mainPanel);

			JLabel helpMsg = new JLabel("Schedule an event to change the CO2 class of a vehicle after a given number of simulation ticks from now");
			helpMsg.setAlignmentX(CENTER_ALIGNMENT);

			mainPanel.add(helpMsg);

			mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

			JPanel viewsPanel = new JPanel();
			viewsPanel.setAlignmentX(CENTER_ALIGNMENT);
			mainPanel.add(viewsPanel);
	 
			mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

			JPanel buttonsPanel = new JPanel();
			buttonsPanel.setAlignmentX(CENTER_ALIGNMENT);
			mainPanel.add(buttonsPanel);

			_vehiclesModel = new DefaultComboBoxModel<>();
			_vehicles = new JComboBox<>(_vehiclesModel);
			viewsPanel.add( new JLabel("Vehicle: "));
			viewsPanel.add(_vehicles);
			
			_co2ClassModel = new DefaultComboBoxModel<>();
			_co2Class = new JComboBox<>(_co2ClassModel);
			viewsPanel.add( new JLabel("CO2 Class: "));
			viewsPanel.add(_co2Class);
			for(int i=0;i<=10;i++) {
				_co2ClassModel.addElement((i));
			}
			
			_ticks = new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));
			_ticks.setMaximumSize(new Dimension(80, 40));
			_ticks.setMinimumSize(new Dimension(80, 40));
			_ticks.setPreferredSize(new Dimension(80, 40));
			viewsPanel.add( new JLabel("Ticks: "));
			viewsPanel.add(_ticks);

			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					_status = 0;
					ChangeContDialog.this.setVisible(false);//al cancelar hago q ya no sea visible
				}
			});
			buttonsPanel.add(cancelButton);

			JButton okButton = new JButton("OK");
			okButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (_vehiclesModel.getSelectedItem() != null && 
							_co2ClassModel.getSelectedItem() != null
							&& _ticks.getValue() != null) {
						_status = 1;
						ChangeContDialog.this.setVisible(false);
					}
				}
			});
			buttonsPanel.add(okButton);

			setPreferredSize(new Dimension(500, 200));
			pack();
			setResizable(false);//no permite al usuario agrandar la ventana
			setVisible(false);//en el constructor no se hace visible todavia
		}

		public int open(List<Vehicle> vehicles) {

			_vehiclesModel.removeAllElements();
			for (Vehicle v : vehicles) {
				_vehiclesModel.addElement(v.getId());
			}
			// You can change this to place the dialog in the middle of the parent window.
			// It can be done using using getParent().getWidth, this.getWidth(),
			// getParent().getHeight, and this.getHeight(), etc.
			//
			setLocation(getParent().getLocation().x + 10, getParent().getLocation().y + 10);

			setVisible(true);//aqui se para para q el usuario interactue, se hace visible aqui, NO EN LA CONSTRUCTORA para que est� refrescada
			return _status;
		}

		public Pair<String, Integer> getVehicleandClass() {
=======
			return new Pair((String) _vehiclesModel.getSelectedItem(),  _co2ClassModel.getSelectedItem());
>>>>>>> 77f2f716f2f88f842454d4d3b462fb98de1cb388
		}
		
		public int getTicks() {
			return (int) _ticks.getValue();
		}
}


