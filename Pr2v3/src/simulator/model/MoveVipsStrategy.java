package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class MoveVipsStrategy implements DequeuingStrategy {
	private int limit;
	private String viptag;
	
	public MoveVipsStrategy(int limit, String viptag) {
		this.limit = limit;
		this.viptag = viptag;
	}
	
	@Override
	public List<Vehicle> dequeue(List<Vehicle> vehicleList) {
		List<Vehicle> list = new ArrayList<Vehicle>();
		int i = 0;
		while(list.size() < limit && i < vehicleList.size()) {
			if(vehicleList.get(i).getId().endsWith(viptag)) {
				list.add(vehicleList.get(i));
			}
			i++;
		}
		
		int j = 0; 
		while(list.size() < limit && j < vehicleList.size()) {
			if(!vehicleList.get(j).getId().endsWith(viptag)) {
				list.add(vehicleList.get(j));
			}
			j++;
		}
		
		
		return list;
	}

}
