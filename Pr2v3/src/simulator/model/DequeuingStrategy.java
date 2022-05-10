package simulator.model;

import java.util.List;

public interface DequeuingStrategy {

	//solo puede haber una carretera con 1 semaforo en verde pero de esa carretera pueden salir un numero variable de vehiculos (según estas estrategias)
	public List<Vehicle> dequeue(List<Vehicle> vehicleList);
}
