package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class MoveFirstStrategy implements DequeuingStrategy {

	public MoveFirstStrategy(){
		
	}
	@Override
	public List<Vehicle> dequeue(List<Vehicle> vehicleList) {
		List<Vehicle> list = new ArrayList<Vehicle>(1);
		if(vehicleList.size() != 0)
			list.add(vehicleList.get(0));
		return list;
	}

}
