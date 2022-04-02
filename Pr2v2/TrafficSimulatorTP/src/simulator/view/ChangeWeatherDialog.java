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
import simulator.model.Road;
import simulator.model.Weather;


public class ChangeWeatherDialog extends JDialog{
	private int _status;
	private JComboBox<String> _roads;
	private JComboBox<String> _weathers;
	private DefaultComboBoxModel<String> _roadsModel;
	private DefaultComboBoxModel<String> _weathersModel;//este modelo refresca automaticamente a diferencia del jtablemodel creado por nosotros
	private JSpinner _ticks;
	
	public ChangeWeatherDialog(Frame frame) {
		//siempre es modal, tiene mas sentido para un jdialog
		super(frame, true);//el segundo argumento indica q la ventana es modal (q hasta q se acepte o se cancele no me deja interactuar con la anterior)
		initGUI();
	}

	private void initGUI() {

		_status = 0;//para q si cierro el cuadro de dialogo sin pulsar a los botones salga tambien el status de 0 por defecto

		setTitle("Change Road Weather");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);

		JLabel helpMsg = new JLabel("Schedule an event to change the weather of a road after a given number of simulation ticks from now");
		helpMsg.setAlignmentX(CENTER_ALIGNMENT);

		mainPanel.add(helpMsg);

		//para que no salga pegado al "Select your favorite"
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel viewsPanel = new JPanel();
		viewsPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(viewsPanel);
 
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(buttonsPanel);

		_roadsModel = new DefaultComboBoxModel<>();
		_roads = new JComboBox<>(_roadsModel);
		viewsPanel.add(_roads);
		
		_weathersModel = new DefaultComboBoxModel<>();
		_weathers = new JComboBox<>(_weathersModel);
		viewsPanel.add(_weathers);
		for(Weather w: Weather.values()) {
			_weathersModel.addElement(w.toString());
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
				ChangeWeatherDialog.this.setVisible(false);//al cancelar hago q ya no sea visible
			}
		});
		buttonsPanel.add(cancelButton);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (_roadsModel.getSelectedItem() != null && 
						_weathersModel.getSelectedItem() != null
						&& _ticks.getValue() != null) {
					_status = 1;
					ChangeWeatherDialog.this.setVisible(false);
				}
			}
		});
		buttonsPanel.add(okButton);

		setPreferredSize(new Dimension(500, 200));
		pack();
		setResizable(false);//no permite al usuario agrandar la ventana
		setVisible(false);//en el constructor no se hace visible todavia
	}

	public int open(List<Road> roads) {

		_roadsModel.removeAllElements();
		for (Road r : roads) {
			_roadsModel.addElement(r.getId());
		}
		// You can chenge this to place the dialog in the middle of the parent window.
		// It can be done using uing getParent().getWidth, this.getWidth(),
		// getParent().getHeight, and this.getHeight(), etc.
		//
		setLocation(getParent().getLocation().x + 10, getParent().getLocation().y + 10);

		setVisible(true);//aqui se para para q el usuario interactue, se hace visible aqui, NO EN LA CONSTRUCTORA para que esté refrescada
		return _status;
	}

	public Pair<String, Weather> getRoadandWeather() {
		return new Pair((String) _roadsModel.getSelectedItem(), Weather.valueOf( (String) _weathersModel.getSelectedItem()));
	}
	
	public int getTicks() {
		return (int) _ticks.getValue();
	}
}
