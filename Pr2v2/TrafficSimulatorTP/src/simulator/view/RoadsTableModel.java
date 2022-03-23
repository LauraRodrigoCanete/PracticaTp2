package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import extra.jtable.EventEx;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class RoadsTableModel extends AbstractTableModel implements TrafficSimObserver{
	private static final long serialVersionUID = 1L;//no lo damos, es para serializacion

	private List<EventEx> _events;

	private String[] _colNames = { "Id", "Length", "Weather",
	        "Max. Speed", "Speed Limit", "Total CO2", "CO2 Limit" };

	public RoadsTableModel() {
		_events=null;	
	}

	public void update() {
		// observar que si no refresco la tabla no se carga
		// La tabla es la represantación visual de una estructura de datos,
		// en este caso de un ArrayList, hay que notificar los cambios.
		
		// We need to notify changes, otherwise the table does not refresh.
		fireTableDataChanged();//si cambio los datos del modelo al jtable hay q actualizarla, si no no se ven		
	}//hay varios metodos para actualizar

	public void setEventsList(List<EventEx> events) {
		_events = events;
		update();
	}
	
	public boolean isCellEditable(int row, int column) {//no puedes modificar el dato de la casilla
		return false;
	}
	
	
	public String getColumnName(int col) {
		return _colNames[col];
	}

	@Override
	public int getRowCount() {
		return _events == null ? 0 : _events.size();//no puedo hacer null.size()

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
			s=_events.get(rowIndex).toString().valueOf("id");
			break;
		case 1:
			s=_events.get(rowIndex).toString().valueOf("length");
			break;
		case 2:
			s=_events.get(rowIndex).toString().valueOf("weather");
			break;
		case 3:
			s=_events.get(rowIndex).toString().valueOf("MaxSpeed");
			break;
		case 4:
			s=_events.get(rowIndex).toString().valueOf("SpeedLimit");
			break;
		case 5:
			s=_events.get(rowIndex).toString().valueOf("totalCO2");
			break;
		case 6:
			s=_events.get(rowIndex).toString().valueOf("CO2Limit");
			break;	
		}
		return s;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

}
