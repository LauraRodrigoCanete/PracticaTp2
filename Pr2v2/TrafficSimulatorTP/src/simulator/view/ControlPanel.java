package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.SetContClassEvent;
import simulator.model.SetWeatherEvent;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.Weather;

public class ControlPanel extends JPanel implements TrafficSimObserver{
	
	private Controller _ctrl;
	private boolean _stopped;
	private JToolBar barra;
	private JButton stop;
	private JButton run;
	private JButton contClass;
	private JButton weather;
	private JSpinner ticks;
	private JButton exit;
	private JButton file;
	private JFileChooser fc;
	
	//estas cosas habra q actualizarlas en los metodos de observador
	private List<Road> _roads;
	private List<Vehicle> _vehicles;
	private List<Event> _events;
	private int _time;
	private static final String SPACE = "                                  ";

	public ControlPanel(Controller ctrl) {
		_roads = new ArrayList<>();
		_vehicles = new ArrayList<>();
		_events = new ArrayList<>();;
		_time = 0;
		_stopped = false;
		this._ctrl = ctrl;
		initGUI();
		_ctrl.addObserver(this);
	}
	
	private void initGUI() {
		contClass = new JButton();
		weather = new JButton();
		run = new JButton();
		ticks = new JSpinner(new SpinnerNumberModel(10, 1, 10000, 1));
		stop = new JButton();
		exit = new JButton();
		file = new JButton();
		barra = new JToolBar();
		this.setLayout(new BorderLayout());
		this.add(barra, BorderLayout.NORTH);
		
		
		//boton file
		
		file.setActionCommand("file");//no sirve de nada, ya está por defecto
		file.setToolTipText("Select a file to load events");
		file.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fc = new JFileChooser();
				fc.setCurrentDirectory(new File("resources/examples/"));
				fc.setMultiSelectionEnabled(false);//solo queremos q seleccione un archivo
				fc.setFileFilter(new FileNameExtensionFilter("Archivos json", "json"));
				int ret = fc.showOpenDialog(thisPanel());
				if (ret == JFileChooser.APPROVE_OPTION) {
					FileInputStream in = null;
					try {
						in = (new FileInputStream(fc.getSelectedFile()));
					} catch (FileNotFoundException e) {
						JOptionPane.showMessageDialog(thisPanel(), "No se ha encontrado el archivo.");
						enableToolBar(true);
					}
					try {
					_ctrl.reset();
					_ctrl.loadEvents(in);
					} catch(Exception e) {
						JOptionPane.showMessageDialog(thisPanel(), "Error sintactico.");
						enableToolBar(true);
					}
					
					JOptionPane.showMessageDialog(thisPanel(), "Se ha seleccionado abrir el archivo: " + fc.getSelectedFile());
				} else if (ret == JFileChooser.CANCEL_OPTION){
					JOptionPane.showMessageDialog(thisPanel(), "Se ha pulsado cancelar.");
				} else {//ha habido un error
					JOptionPane.showMessageDialog(thisPanel(), "Ha habido un error eligiendo el archivo.");
				}
			}
		});
		file.setIcon(new ImageIcon("resources/icons/open.png"));
		barra.add(file);
		barra.addSeparator();
		
		//boton contClass
		contClass.setActionCommand("contClass");
		contClass.setToolTipText("Change contamination class");
		contClass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				ChangeContDialog dialog = new ChangeContDialog(getPreviousFrame());

				//sin esta linea no es visible, se hace visible en el open
				int status = dialog.open(_vehicles);
				if (status == 0) {
					System.out.println("Canceled");
				} else {
					List<Pair<String,Integer>> pairs = new ArrayList<Pair<String,Integer>>();
					pairs.add(dialog.getVehicleandClass());
					_ctrl.addEvent(new SetContClassEvent(_time + dialog.getTicks(), pairs));
				}
			}
		});
		contClass.setIcon(new ImageIcon("resources/icons/co2class.png"));
		barra.add(contClass);



		//boton weather
		weather.setActionCommand("weather");
		weather.setToolTipText("Change the weather conditions");
		weather.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				ChangeWeatherDialog dialog = new ChangeWeatherDialog(getPreviousFrame());

				//sin esta linea no es visible, se hace visible en el open
				int status = dialog.open(_roads);
				if (status == 0) {
					System.out.println("Canceled");
				} else {
					List<Pair<String,Weather>> pairs = new ArrayList<Pair<String,Weather>>();
					pairs.add(dialog.getRoadandWeather());
					_ctrl.addEvent(new SetWeatherEvent(_time + dialog.getTicks(), pairs));
				}
			}
		});
		weather.setIcon(new ImageIcon("resources/icons/weather.png"));
		barra.add(weather);
		barra.addSeparator();
		
		//boton run
		run.setActionCommand("run");
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
		exit.setActionCommand("exit");
		exit.setToolTipText("exit the program");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				int n = JOptionPane.showConfirmDialog (null, "Are you sure you want to quit?","Quit",JOptionPane.YES_NO_OPTION);				 
		        if(n == 0)
			        System.exit(0);			        
			}
		});
		exit.setIcon(new ImageIcon("resources/icons/exit.png"));
		barra.add(exit);
		
		
	}
	
	private void run_sim(int n) {
		if (n > 0 && !_stopped) {
			try {
				_ctrl.run(1);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(thisPanel(), "Ha habido un error en el run");
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
		//poner boton por boton pq toda la toolbar de golpe no funciona bien
		run.setEnabled(b);
		exit.setEnabled(b);
		weather.setEnabled(b);
		contClass.setEnabled(b);
		stop.setEnabled(true);

	}

	private void stop() {
		_stopped = true;
	}

	/*
	 * lo tengo q hacer fuera para q sea un metodo 
	 * de la clase JPanel pq dentro de la clase anonima el this cambia y 
	 * no me deja usar el metodo
	 */
	private JFrame getPreviousFrame() {
		return (JFrame) SwingUtilities.getWindowAncestor(this);
	}
	
	private JPanel thisPanel() {
		return this;
	}
	
	private void update(List<Road> roads, List<Vehicle> vehicles, List<Event> events, int time) {
		_roads = roads;
		_vehicles = vehicles;
		_events = events;
		_time = time;
	}
	
	//en estos metodos necesitamos ir reasignando las listas de vehiculos, junc... q tengamos como atributos
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		update(map.getRoads(), map.getVehicles(), events, time);
	}
	
	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map.getRoads(), map.getVehicles(), events, time);
	}
	
	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map.getRoads(), map.getVehicles(), events, time);
	}
	
	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map.getRoads(), map.getVehicles(), events, time);
	}
	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map.getRoads(), map.getVehicles(), events, time);
	}
	
	@Override
	public void onError(String err) {

	}


}
