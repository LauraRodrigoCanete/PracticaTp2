package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import extra.jtable.EventEx;
import extra.jtable.EventsTableModel;
import extra.jtable.JTableExamples;
import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.SetWeatherEvent;
import simulator.model.TrafficSimObserver;
import simulator.model.Weather;

public class StatusBar extends JPanel implements TrafficSimObserver {
	private Border _defaultBorder = BorderFactory.createLineBorder(Color.red, 1);

	// this is what we show in the table
	// esto es lo que mostramos en la table

 
	
	private List<Event> _events;
	private int _time;
	
	//BEÑI TE HE AÑADIDO ESTO PARA LO DEL MAIN WINDOW UN KISS
	public StatusBar(Controller _ctrl) {
	}
	
	public StatusBar(List<Event>events) {
		_events=events;
		_time=1;
		initGUI();
	}
	
	public void initGUI() {
		setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel();
        
        //JTable table = new JTable();
		
		JLabel l1= new JLabel("Time: "+String.valueOf(_time));
		JLabel l2= new JLabel("Event Added("+_events.get(0).toString()+")");
		
		
		mainPanel.add(l1, BorderLayout.WEST);
		mainPanel.add(l2, BorderLayout.EAST);

		
		this.add(mainPanel);
		//f.add(p);
		//f.setSize(20, 300);

		
		
		

		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(700, 300);
		//this.pack();
		setVisible(true);
	}
	public void setEventsList(List<Event> events) {
		_events = events;
		//fireTableDataChanged();
	}
	public void update(List<Event> events) {
		_events = events;
		repaint();
	}
	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				List<Event> list=new ArrayList();
				List<Pair<String,Weather>> ws = new ArrayList();
				Pair par=new Pair("Hola", Weather.CLOUDY);
				ws.add(par);
				Event e=new SetWeatherEvent(1, ws);
				list.add(e);
				new StatusBar(list);
			}
		});
	}
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		update(events);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		update(events);

	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		update(events);

	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		update(events);

	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

}
