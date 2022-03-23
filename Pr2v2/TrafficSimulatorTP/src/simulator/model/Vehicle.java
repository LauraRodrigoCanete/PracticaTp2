package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.lang.Math;

import org.json.JSONObject;

public class Vehicle extends SimulatedObject{
	
	private int location;//distancia desde el comienzo de la carretera actual
	private int speed;
	private int maxSpeed;
	private int contClass;//contamination class
	private int distance;//distancia total recorrida
	private VehicleStatus status;
	private int CO2;//contamination
	private List<Junction> itinerary;
	private int prevJunction;//indice del cruce en el que estamos/acabamos de pasar
	private Road road;
	
	Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) {
		super(id);
		if(maxSpeed <= 0 || contClass < 0 || contClass > 10 || itinerary.size() < 2 )
			throw new IllegalArgumentException("there is a problem in the construction of a vehicle"); 

		this.itinerary = Collections.unmodifiableList(new ArrayList<>(itinerary));
		this.road = null;
		this.location = 0;
		this.maxSpeed = maxSpeed;
		this.contClass = contClass;
		this.speed = 0;
		status = VehicleStatus.PENDING;
		prevJunction = 0;
	}
	
	public int getLocation() {
		return location;
	}

	public int getSpeed() {
		return speed;
	}

	public int getMaxSpeed() {
		return maxSpeed;
	}

	public int getContClass() {
		return contClass;
	}

	public VehicleStatus getStatus() {
		return status;
	}

	public int getTotalCO2() {
		return CO2;
	}
	
	public int getDistance() {
		return distance;
	}

	public List<Junction> getItinerary() {
		return Collections.unmodifiableList(itinerary);
	}

	public Road getRoad() {
		return road;
	}
	
	void setSpeed(int s) {
		if(status == VehicleStatus.TRAVELING) {
		if(s < 0)
			throw new IllegalArgumentException("speed is less than 0"); 

		this.speed = Math.min(s, maxSpeed);
		}
	}

	void setContClass(int c) {
		if(c < 0 || c > 10)
			throw new IllegalArgumentException("contamination level is not in its range"); 
		this.contClass = c;
	}

	void moveToNextRoad() {
		if(status != VehicleStatus.PENDING && status != VehicleStatus.WAITING)
			throw new IllegalArgumentException("status is not pending or waiting so cannot move to next road"); 
		
		if(this.status != VehicleStatus.PENDING)
			road.exit(this);
		
		if(prevJunction < itinerary.size()-1) {
			location = 0;
			road = itinerary.get(prevJunction).roadTo(itinerary.get(prevJunction+1));
			road.enter(this);
			status = VehicleStatus.TRAVELING;
			prevJunction++;
		}
		else {
			status = VehicleStatus.ARRIVED;
			speed = 0;
			road = null;
		}
		
	}
	
	@Override
	void advance(int time) {
		if(this.status == VehicleStatus.TRAVELING) {
			int oldLocation = this.location;
			this.location = Math.min(this.location + speed, road.getLength());
			this.distance = this.distance - oldLocation + this.location;
			int c = contClass * (location - oldLocation);
			CO2 += c;
			road.addContamination(c);
			if(location == road.getLength()) {
				road.getDest().enter(this);
				status = VehicleStatus.WAITING;
				speed = 0;
			}
		}
	}

	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("id", _id);
		jo.put("speed", speed);
		jo.put("distance", distance);
		jo.put("co2", CO2);
		jo.put("class", contClass);
		jo.put("status", status.toString());
		
		if(this.status!=(VehicleStatus.PENDING) && 
				this.status!=(VehicleStatus.ARRIVED)){
			jo.put("road", road.getId());
			jo.put("location", location);
		}
		
		return jo;
	}
	
	
	
}
