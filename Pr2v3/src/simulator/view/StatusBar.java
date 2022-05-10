package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class StatusBar extends JPanel implements TrafficSimObserver {
	

	private int _time;
	private JPanel izquierda;
	private JPanel derecha;
	private JLabel izq;
	private JLabel der;
	
	public StatusBar(Controller _ctrl) {
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
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
		separator.setPreferredSize(new Dimension(5, 20));		

     	this.add(izquierda);
	    this.add(Box.createRigidArea(new Dimension(100, 20)));
	    this.add(separator);
		this.add(derecha); 	
	}


	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		this._time=time;
		der.setText("");		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		this._time=time;
		izq.setText(" Time: " + String.valueOf(_time));
		der.setText("");
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		this._time=time;
		der.setText("Event added: ("+ e.toString()+ ")");

	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		this._time=time;
		der.setText("");
		izq.setText(" Time: " + String.valueOf(_time));

	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onError(String err) {
		der.setText(err);
	}

}
