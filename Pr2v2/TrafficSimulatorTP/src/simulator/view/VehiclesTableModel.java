package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class VehiclesTableModel extends AbstractTableModel implements TrafficSimObserver{

	private List<Vehicles> _events;
	private String[] _colNames = { "#", "Time", "Priority" };

	public VehiclesTableModel() {
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

	@Override
	public boolean isCellEditable(int row, int column) {//no puedes modificar el dato de la casilla
		return false;
	}

	//si no pongo esto no coge el nombre de las columnas
	//this is for the column header
	@Override
	public String getColumnName(int col) {
		return _colNames[col];
	}

	@Override
	// método obligatorio, probad a quitarlo, no compila
	//(es abstracto y hay q sobreescribir)
	// this is for the number of columns
	public int getColumnCount() {
		return _colNames.length;
	}

	@Override
	// método obligatorio
	//
	// the number of row, like those in the events list
	public int getRowCount() {
		return _events == null ? 0 : _events.size();//no puedo hacer null.size()
	}

	@Override
	// método obligatorio
	// asi es como se va a cargar la tabla desde el ArrayList
	// el indice del arrayList es el número de fila pq en este ejemplo
	// quiero enumerarlos.
	//
	// returns the value of a particular cell 
	public Object getValueAt(int rowIndex, int columnIndex) {//no le llamaremos es para ver como se visualiza cada casilla (fila,col)
		//quiero cada evento en una fila (el de la pr es igual q este)
		Object s = null;
		switch (columnIndex) {//cada case es una columna
		case 0:
			s = rowIndex;
			break;
		case 1:
			s = _events.get(rowIndex).getTime();
			break;
		case 2:
			s = _events.get(rowIndex).getPriority();
			break;
		}
		return s;
	}
}
