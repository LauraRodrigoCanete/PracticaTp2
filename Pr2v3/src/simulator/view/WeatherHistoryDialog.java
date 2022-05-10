package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Weather;

public class WeatherHistoryDialog extends JDialog {
	private JComboBox<String> _weathers;
	private DefaultComboBoxModel<String> _weathersModel;
	private Controller controller;
	
	public WeatherHistoryDialog(Frame frame, Controller c) {
		super(frame, true);
		controller = c;
		initGUI();
	}
	
	private void initGUI() {

		setTitle("Roads Weather History");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);

		JLabel helpMsg = new JLabel("<html>Select a weather and press UPDATE to show the roads that have this weather in each tick.</html>");
		helpMsg.setAlignmentX(CENTER_ALIGNMENT);

		mainPanel.add(helpMsg);

		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel comboPanel = new JPanel();
		comboPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(comboPanel);
 
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(buttonsPanel);

		_weathersModel = new DefaultComboBoxModel<>();
		_weathers = new JComboBox<>(_weathersModel);
		comboPanel.add( new JLabel("Weather: "));
		comboPanel.add(_weathers);
		for(Weather w: Weather.values()) {
			_weathersModel.addElement(w.toString());
		}

		JButton cancelButton = new JButton("Close");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				WeatherHistoryDialog.this.setVisible(false);
			}
		});
		buttonsPanel.add(cancelButton);

		WeatherTableModel model = new WeatherTableModel(controller);
		
		JButton okButton = new JButton("Update");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (_weathersModel.getSelectedItem() != null){
					model.fireTableDataChanged();
				}
			}
		});
		buttonsPanel.add(okButton);
		
		JPanel tablesPanel = new JPanel(new BorderLayout());
		tablesPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2), "",
				TitledBorder.LEFT, TitledBorder.TOP));
		JTable table = new JTable(model);
		tablesPanel.add(table);
		tablesPanel.add(new JScrollPane(table));
		
		tablesPanel.setPreferredSize(new Dimension(500, 200));
		mainPanel.add(tablesPanel);

		setPreferredSize(new Dimension(600, 400));
		pack();
		setResizable(false);//no permite al usuario agrandar la ventana
		setVisible(false);//en el constructor no se hace visible todavia
	}

	public void open() {

		setLocation(getParent().getLocation().x + 10, getParent().getLocation().y + 10);

		setVisible(true);//aqui se para para q el usuario interactue, se hace visible aqui, NO EN LA CONSTRUCTORA para que esté refrescada

	}

	public class WeatherTableModel extends AbstractTableModel implements TrafficSimObserver {

		private List<Map<String, List<Road>>> _roadsPerTick;//la fila i-esima es el elemento de la list i-esimo
		private String[] _colNames = { "Ticks", "Roads" };

		public WeatherTableModel(Controller controller) {
			_roadsPerTick = new ArrayList<>();
			controller.addObserver(this);
		}

		@Override
		public boolean isCellEditable(int row, int column) {//no puedes modificar el dato de la casilla
			return false;
		}

		//si no pongo esto no coge el nombre de las columnas
		@Override
		public String getColumnName(int col) {
			return _colNames[col];
		}

		@Override
		// metodo obligatorio
		public int getColumnCount() {
			return _colNames.length;
		}

		@Override
		// metodo obligatorio
		public int getRowCount() {
			return _roadsPerTick == null ? 0 : _roadsPerTick.size();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			String s = "";
			List<Road> roads = _roadsPerTick.get(rowIndex).get(_weathers.getSelectedItem());
			switch (columnIndex) {//cada case es una columna
			case 0:
				int i = rowIndex +1;
				s =  s + i;
				break;
			case 1:
				if(roads == null)
					s = s + "[]";
				else
				    s = s + roads;
				break;
			}
			return s;
		}

		@Override
		public void onAdvanceStart(RoadMap map, List<Event> events, int time) {

		}

		@Override
		public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
			
			Map<String, List<Road>> weathermap = new HashMap<>();
			for(Road r : map.getRoads()) {
				String w = r.getWeather().toString();
				if(weathermap.containsKey(w)){
					weathermap.get(w).add(r);
				}
				else {
					List<Road> l = new ArrayList<Road>();
					l.add(r);
					weathermap.put(w, l);
				}
					
			}
			
			_roadsPerTick.add(weathermap);
			fireTableDataChanged();
		}

		@Override
		public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {

		}

		@Override
		public void onReset(RoadMap map, List<Event> events, int time) {
			_roadsPerTick.clear();
			fireTableDataChanged();
		}

		@Override
		public void onRegister(RoadMap map, List<Event> events, int time) {
			_roadsPerTick.clear();
			fireTableDataChanged();
		}

		@Override
		public void onError(String err) {
		}

	}
	
}
