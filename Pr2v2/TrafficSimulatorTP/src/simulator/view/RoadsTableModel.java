package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import extra.jtable.EventEx;
import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class RoadsTableModel extends AbstractTableModel implements TrafficSimObserver{
	private static final long serialVersionUID = 1L;//no lo damos, es para serializacion

	private Controller controller;
	private List<Road> _roads;

	private String[] _colNames = { "Id", "Length", "Weather",
	        "Max. Speed", "Speed Limit", "Total CO2", "CO2 Limit" };

	public RoadsTableModel(Controller controller) {
		_roads=null;	
		this.controller = controller;
		controller.addObserver(this);
	}

	
	public void setRoadsList(List<Road> roads) {
		_roads = roads;
		fireTableDataChanged();
	}
	
	public boolean isCellEditable(int row, int column) {//no puedes modificar el dato de la casilla
		return false;
	}
	
	
	public String getColumnName(int col) {
		return _colNames[col];
	}

	@Override
	public int getRowCount() {
		return _roads == null ? 0 : _roads.size();//no puedo hacer null.size()

	}

	@Override
	public int getColumnCount() {
		return _colNames.length;
	}
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		switch(columnIndex) {
		case 0:
			s=_roads.get(rowIndex).getId();
			break;
		case 1:
			s=_roads.get(rowIndex).getLength();
			break;
		case 2:
			s=_roads.get(rowIndex).getWeather();
			break;
		case 3:
			s=_roads.get(rowIndex).getMaxSpeed();
			break;
		case 4:
			s=_roads.get(rowIndex).getSpeedLimit();
			break;
		case 5:
			s=_roads.get(rowIndex).getTotalCO2();
			break;
		case 6:
			s=_roads.get(rowIndex).getContLimit();
			break;	
		}
		return s;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		setRoadsList(map.getRoads());
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		setRoadsList(map.getRoads());
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		setRoadsList(map.getRoads());
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		setRoadsList(map.getRoads());
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(String err) {
	}

}
