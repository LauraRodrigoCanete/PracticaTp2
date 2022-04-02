package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class JunctionsTableModel extends AbstractTableModel implements TrafficSimObserver {
	
	private List<Junction> _junctions;
	private Controller controller;
	private String[] _colNames = { "Id", "Green", "Queues"};
	
	public JunctionsTableModel(Controller controller) {
		_junctions=new ArrayList<>();	
		this.controller = controller;
		controller.addObserver(this);
	}
	
	public void setJunctionsList(List<Junction> junctions) {
		_junctions = junctions;
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
		return _junctions == null ? 0 : _junctions.size();//no puedo hacer null.size()
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
			s=_junctions.get(rowIndex).getId();
			break;
		case 1:
			s=_junctions.get(rowIndex).getTrafficLight();
			if(s.equals(-1))
				s="NONE";
			break;
		case 2:
			String a="";
			for(int i=0; i<this._junctions.size();i++) {
				a+=this._junctions.get(i).getId()+":";
				if(_junctions.get(i).getQueueList().size()==0) 
					a+="[]";			
				else {
					for(int j=0;j<_junctions.get(i).getQueueList().size();j++) {
						a+="[";
						a+=_junctions.get(i).getQueueList().get(j).toString();
						a+="]";
					}
				}			
			}
			break;
		}
		return s;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		setJunctionsList(map.getJunctions());
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		setJunctionsList(map.getJunctions());
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		setJunctionsList(map.getJunctions());
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		setJunctionsList(map.getJunctions());
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		setJunctionsList(map.getJunctions());
	}

	@Override
	public void onError(String err) {
	}


	

}
