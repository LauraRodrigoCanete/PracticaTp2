package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class MoveAllStrategy implements DequeuingStrategy {

	public MoveAllStrategy() {}
	
	@Override
	public List<Vehicle> dequeue(List<Vehicle> vehicleList) {
		List<Vehicle> list = new ArrayList<Vehicle>();
		for(int i=0;i<vehicleList.size();i++)
			list.add(vehicleList.get(i));//es una lista distinta pero le estas pasando la referencia al mismo objeto de la primera
		return list;
	}

}
