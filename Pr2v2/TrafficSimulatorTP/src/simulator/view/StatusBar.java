package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
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
	

	private List<Event> _events;
	private int _time;
	private JPanel izquierda;
	private JPanel derecha;
	private JLabel izq;
	private JLabel der;
	
	public StatusBar(Controller _ctrl) {
		_events = new ArrayList<Event>();
		_time=0;
		_ctrl.addObserver(this);
		
		izquierda = new JPanel();
		derecha = new JPanel();
		izq= new JLabel(" Time: " + String.valueOf(_time));
		der= new JLabel(" Welcome!");
		initGUI();
	}
	
	public void initGUI() {
		
		izquierda.add(izq);
		derecha.add(der);
		this.setLayout(new FlowLayout(FlowLayout.LEFT));//viene por defecto
		JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
		separator.setPreferredSize(new Dimension(5, 20));		

     	this.add(izquierda);
	    this.add(Box.createRigidArea(new Dimension(100, 20)));
	    this.add(separator);
		this.add(derecha); 	
	}
	public void setEventsList(List<Event> events) {
		_events = events;
	}
	public void update(List<Event> events) {
		_events = events;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		this._time=time;
		der.setText("");		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		this._time=time;
		izq.setText(" Time: " + String.valueOf(_time));
		der.setText("");
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		this._time=time;
		der.setText(e.toString());


	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		this._time=time;
		der.setText("");
		izq.setText(" Time: " + String.valueOf(_time));

	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
	
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		der.setText(err);
	}

}
