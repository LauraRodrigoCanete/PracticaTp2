package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;

public class VehiclesTableModel extends AbstractTableModel implements TrafficSimObserver{

	private Controller controller;
	private List<Vehicle> _vehicles;
	private String[] _colNames = { "Id", "Location", "Itinerary", "CO2 Class", "Max. Speed", "Speed", "Total CO2", "Distance" };

	public VehiclesTableModel(Controller controller) {
		_vehicles=new ArrayList<>();
		this.controller = controller;
		controller.addObserver(this);
	}
	
	public void setVehiclesList(List<Vehicle> vehicles) {
		_vehicles = vehicles;
		fireTableDataChanged();	
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
		return _vehicles == null ? 0 : _vehicles.size();//no puedo hacer null.size()
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		switch (columnIndex) {//cada case es una columna
		case 0:
			s = _vehicles.get(rowIndex).getId();
			break;
		case 1:
			if(_vehicles.get(rowIndex).getStatus() == VehicleStatus.ARRIVED)
				s = "Arrived";
			else if(_vehicles.get(rowIndex).getStatus() == VehicleStatus.PENDING)
				s = "Pending";
			else if(_vehicles.get(rowIndex).getStatus() == VehicleStatus.WAITING)
				s = "Waiting:" + _vehicles.get(rowIndex).getWaitingJunction();
			else {
				String aux = "";
				if (_vehicles.get(rowIndex).getRoad() != null) 
					aux = _vehicles.get(rowIndex).getRoad().getId() + ":";
				aux += _vehicles.get(rowIndex).getLocation();
				s = aux;
			}
			break;
		case 2:
			s = _vehicles.get(rowIndex).getItinerary();
			break;
		case 3:
			s = _vehicles.get(rowIndex).getContClass();
			break;
		case 4:
			s = _vehicles.get(rowIndex).getMaxSpeed();
			break;
		case 5:
			s = _vehicles.get(rowIndex).getSpeed();
			break;
		case 6:
			s = _vehicles.get(rowIndex).getTotalCO2();
			break;
		case 7:
			s = _vehicles.get(rowIndex).getDistance();
			break;
		}
		
		return s;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		setVehiclesList(map.getVehicles());
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		setVehiclesList(map.getVehicles());
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		setVehiclesList(map.getVehicles());
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		setVehiclesList(map.getVehicles());
		
	}

	@Override
	public void onError(String err) {
	}

	
}
