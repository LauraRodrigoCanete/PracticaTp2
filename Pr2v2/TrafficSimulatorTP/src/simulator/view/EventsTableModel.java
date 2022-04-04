package simulator.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class EventsTableModel extends AbstractTableModel implements TrafficSimObserver {

	private Controller controller;
	private List<Event> _events;//la fila i-esima es el elemento de la list i-esimo
	private String[] _colNames = { "Time", "Desc" };

	public EventsTableModel(Controller controller) {
		_events = new ArrayList<>();
		this.controller = controller;
		controller.addObserver(this);
		Graphics2D g;
		g.setColor(Color.white);
		g.clearRect(0, 0, getWidth(), getHeight());
		//esto no se hace visible, ya hacemos visible el JFrame entero
	}
	
	public void setEventsList(List<Event> events) {
		_events = events;
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
		return _events == null ? 0 : _events.size();//no puedo hacer null.size()
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		switch (columnIndex) {//cada case es una columna
		case 0:
			s = _events.get(rowIndex).getTime();//los ticks son cada cuanto avanzas, el time el numero de ticks totales avanzados
			break;
		case 1:
			s = _events.get(rowIndex).toString();
			break;
		}
		return s;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		setEventsList(events);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		setEventsList(events);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		setEventsList(events);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		setEventsList(events);
	}

	@Override
	public void onError(String err) {
	}

}
