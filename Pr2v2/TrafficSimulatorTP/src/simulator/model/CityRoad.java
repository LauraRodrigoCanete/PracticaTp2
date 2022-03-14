package simulator.model;

public class CityRoad extends Road{

	CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	void reduceTotalContamination() {
		int x = 2;
		switch(weather) {
			case WINDY:
				x= 10;
				break;
			case STORM:
				x= 10;
				break;
		}
		
		totalCO2 = Math.max(totalCO2-x, 0);
		
	}

	@Override
	void updateSpeedLimit() {
		//la velocidad limite no cambia
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		return (((11-v.getContClass())*speedLimit)/11);
	}

}
