package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
	private JMenuBar barra;

	
	public StatusBar(Controller _ctrl) {
		_events = new ArrayList<Event>();
		_time=0;
		_ctrl.addObserver(this );
		initGUI();

	}
	
	public void initGUI() {
		//this.getLayout();
		barra = new JMenuBar();
		this.setLayout(new BorderLayout());//viene por defecto
		this.add(barra, BorderLayout.NORTH);
        //JPanel mainPanel = new JPanel();
		
		JLabel l1= new JLabel(" Time: " + String.valueOf(_time));
		JLabel l2;
			
		barra.add(l1, BorderLayout.WEST);
		barra.add(Box.createRigidArea(new Dimension(200, 0)));
		barra.add(new JSeparator(SwingConstants.VERTICAL));

		if(_time==0 && _events.size()==0) {
			l2= new JLabel(" Welcome!");
			barra.add(l2);
		}
	
		else if(_events.size()!=0&& _events.get(0).getTime()==_time+1 ) {
			l2= new JLabel(" Event added ("+_events.get(0).toString()+")" );
			barra.add(l2);
		}
		barra.add(Box.createRigidArea(new Dimension(1400, 0)));

		
		//this.add(mainPanel);
		

		
		
		

		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//this.setSize(700, 300);
		//this.pack();
	}
	public void setEventsList(List<Event> events) {
		_events = events;
		//fireTableDataChanged();
	}
	public void update(List<Event> events) {
		_events = events;
		repaint();
	}
/*	public static void main(String[] args) {

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
	}*/
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		this._time=time;
		update(events);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		this._time=time;
		update(events);

	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		this._time=time;

		update(events);

	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		this._time=time;
		update(events);

	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

}
