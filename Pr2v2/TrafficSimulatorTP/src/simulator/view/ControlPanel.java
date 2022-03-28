package simulator.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class ControlPanel extends JPanel implements TrafficSimObserver{
	
	private Controller _ctrl;
	private boolean _stopped;
	private JToolBar barra;
	private JButton stop;
	private JButton run;
	private JSpinner ticks;
	
	public ControlPanel(Controller ctrl) {
		_stopped = false;
		_ctrl = ctrl;
		initGUI();
		_ctrl.addObserver(this);
	}
	
	private void initGUI() {
		run = new JButton();
		ticks = new JSpinner(new SpinnerNumberModel(10, 1, 10000, 1));
		stop = new JButton();
		barra = new JToolBar();
		this.setLayout(new BorderLayout());//viene por defecto
		this.add(barra, BorderLayout.NORTH);
		
		
		//boton run
		run.setActionCommand("run");//no sirve de nada, ya está por defecto
		run.setToolTipText("Run the program");
		run.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				_stopped = false;
				enableToolBar(false);
				Integer t = (Integer) ticks.getValue();
				run_sim(t);
			}
		});
		run.setIcon(new ImageIcon("resources/icons/run.png"));
		barra.add(run);
		//barra.addSeparator();
		
		//boton stop
		stop.setActionCommand("stop");
		stop.setToolTipText("Stop the program");
		stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				stop();
			}
		});
		stop.setIcon(new ImageIcon("resources/icons/stop.png"));
		barra.add(stop);
		
		//spinner ticks
		ticks.setToolTipText("Simulation tick to run: 1-10000");
		ticks.setMaximumSize(new Dimension(80, 40));
		ticks.setMinimumSize(new Dimension(80, 40));
		ticks.setPreferredSize(new Dimension(80, 40));
		barra.add( new JLabel("Ticks: "));//se añade todo al toolbar
		barra.add(ticks);
		
		//separacion
		barra.add(Box.createGlue());
		
		//boton salir
		
	}
	
	private void run_sim(int n) {
		if (n > 0 && !_stopped) {
			try {
				_ctrl.run(1);
			} catch (Exception e) {
				// TODO show error message
				_stopped = true;
				return;
			}
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					run_sim(n - 1);
				}
			});
		} else {
			
			enableToolBar(true);//para que puedas pulsar el resto de botones
			_stopped = true;
		}
	}
	
	private void enableToolBar(boolean b) {
		barra.setEnabled(b);
		stop.setEnabled(true);
	}

	private void stop() {
		_stopped = true;
	}

	//en estos metodos necesitamos ir reasignando las listas de vehiculos, junc... q tengamos como atributos
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		
	}
	
	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		
	}
	
	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {

	}
	
	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {

	}
	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {

	}
	
	@Override
	public void onError(String err) {

	}


}
