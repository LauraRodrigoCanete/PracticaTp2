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
		Junction j = _junctions.get(rowIndex);
		switch(columnIndex) {
		case 0:
			s= j.getId();
			break;
		case 1:
			s= j.getTrafficLight();
			if(s.equals(-1))
				s="NONE";
			else
				s= j.getInRoads().get(j.getTrafficLight());
			break;
		case 2:
			 s="";
			for(int i=0; i<j.getInRoads().size();i++) {
				s+=j.getInRoads().get(i).getId()+":";
				if(j.getQueueList().get(i).size()==0) 
					s+="[] ";			
				else {
					for(int k=0;k < j.getQueueList().get(i).size();k++) {
						s+="[";			
						s+=j.getQueueList().get(i).get(k).getId();
						s+="] " ;
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
