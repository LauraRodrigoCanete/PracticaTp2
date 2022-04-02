package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;
import simulator.model.Weather;

public class MapByRoadComponent extends JComponent implements TrafficSimObserver{

	private static final int _JRADIUS = 10;
	private static final Color _BG_COLOR = Color.WHITE;
	private static final Color _JUNCTION_COLOR = Color.BLUE;
	private static final Color _JUNCTION_LABEL_COLOR = new Color(200, 100, 0);
	private static final Color _GREEN_LIGHT_COLOR = Color.GREEN;
	private static final Color _RED_LIGHT_COLOR = Color.RED;
	
	private RoadMap _map;

	private List<Image> _contImages;
	private Map<Weather, Image> _weatherImages;
	private Image _car;
	
	public MapByRoadComponent(Controller ctrl) {
		setPreferredSize (new Dimension (300, 200));//cambiamos
		initGUI();
		ctrl.addObserver(this);
	}
	
	private void initGUI() {
		_car = loadImage("car.png");
		
		_contImages = new ArrayList<Image>();
		for(int i = 0; i < 6; i++) {
			_contImages.add(loadImage("cont_" + i + ".png"));
		}
		
		_weatherImages = new HashMap<Weather, Image>();
		_weatherImages.put(Weather.CLOUDY, loadImage("cloud.png"));
		_weatherImages.put(Weather.RAINY, loadImage("rain.png"));
		_weatherImages.put(Weather.STORM, loadImage("storm.png"));
		_weatherImages.put(Weather.SUNNY, loadImage("sun.png"));
		_weatherImages.put(Weather.WINDY, loadImage("wind.png"));
	}
	
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//para tener mas definicion
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// clear with a background color
		g.setColor(_BG_COLOR);//no hace falta, por defecto sale blanco
		g.clearRect(0, 0, getWidth(), getHeight());

		if (_map == null || _map.getJunctions().size() == 0) {
			g.setColor(Color.red);
			g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
		} else {
			updatePrefferedSize();//no hace falta pq las coordenadas no se van a salir de la pantalla, esto es para redimensionarlo entonces
			drawMap(g);
		}
	}
	
	private void drawMap(Graphics g) {
		//para cada carretera: junctions, vehiculos, weather, contclass
		
		for (int i = 0; i < _map.getRoads().size(); i++) {
			Road r = _map.getRoads().get(i); //para escribir menos
			int x1 = 50;
			int x2 = getWidth()-100;
			int y = (i+1)*50;
			
			//dibujamos la carretera:
			g.setColor(Color.BLACK);
			g.drawLine(x1, y, x2, y);
			
			//dibujamos el cruce origen:
			g.setColor(_JUNCTION_COLOR);
			g.fillOval(x1 - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);
			g.setColor(_JUNCTION_LABEL_COLOR);
			g.drawString(r.getSrc().getId(), x1-2, y - 8);
		
			//dibujamos el cruce destino segun el semaforo:
			Color juncColor = _RED_LIGHT_COLOR;
			int light = r.getDest().getTrafficLight();
			if (light != -1 && r.equals(r.getDest().getInRoads().get(light))) {
				juncColor = _GREEN_LIGHT_COLOR;
			}
			
			g.setColor(juncColor);
			g.fillOval(x2 - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);
			g.setColor(_JUNCTION_LABEL_COLOR);
			g.drawString(r.getSrc().getId(), x2-2, y - 8);
		
			//dibujamos los vehiculos
			for(Vehicle v: r.getVehicles()) {
				int A = v.getLocation();
				int B = r.getLength();
				int x = x1 + (int) ((x2 - x1) * ((double) A / (double) B));
				g.drawImage(_car, x, y -10, 16, 16, this);
				
				int vLabelColor = (int) (25.0 * (10.0 - (double) v.getContClass()));
				g.setColor(new Color(0, vLabelColor, 0));
				g.drawString(v.getId(), x, y - 8);
			}
			
			//dibujamos el id de las carreteras
			g.setColor(Color.BLACK);
			g.drawString(r.getId(), x1 - 30, y);
			
			//weather
			g.drawImage(_weatherImages.get(r.getWeather()), x2 + 3, y-15, 32, 32, this);
			
			//contamination
			int c = (int) Math.floor(Math.min((double) r.getTotalCO2()/(1.0 + (double) r.getContLimit()),1.0) / 0.19);
			g.drawImage(_contImages.get(c), x2 + 37, y-15, 32, 32, this);
		}

	}

	
	private void updatePrefferedSize() {
		int maxW = 200;
		int maxH = 200;
		for (Junction j : _map.getJunctions()) {
			maxW = Math.max(maxW, j.getX());
			maxH = Math.max(maxH, j.getY());
		}
		maxW += 20;
		maxH += 20;
		setPreferredSize(new Dimension(maxW, maxH));
		setSize(new Dimension(maxW, maxH));
	}

	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(new File("resources/icons/" + img));
		} catch (IOException e) {
		}
		return i;
	}

	public void update(RoadMap map) {
		_map = map;
		repaint();
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onError(String err) {
	}

}
