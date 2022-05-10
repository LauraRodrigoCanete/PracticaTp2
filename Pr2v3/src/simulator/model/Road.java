package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Road extends SimulatedObject{

	private int length;
	private Junction dest;
	private Junction src;
	protected Weather weather;
	protected int contLimit;
    protected int maxSpeed;
	protected int totalCO2;
	protected int speedLimit;
	private List<Vehicle> vehicles;//ordenada por localizacion descendente
	private Comparator<Vehicle> comp;
	
	Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id);
		
		if(maxSpeed <= 0 || contLimit < 0 || length <= 0 || srcJunc == null || destJunc == null || weather == null)
			throw new IllegalArgumentException("there is a problem in the construction of a road"); 

		vehicles = new ArrayList<Vehicle>();
		src = srcJunc;
		dest = destJunc;
		this.maxSpeed = maxSpeed;
		speedLimit = maxSpeed;
		this.contLimit = contLimit;
		this.length = length;
		this.weather = weather;
		this.totalCO2 = 0;
		
		src.addOutgoingRoad(this);
		dest.addIncomingRoad(this);
		
		comp = new Comparator<Vehicle>() {//clase anónima
			public int compare(Vehicle a, Vehicle b) {
				if(a.getLocation() > b.getLocation())
					return -1;
				else if(a.getLocation() == b.getLocation())
					return 0;
				else
					return 1;
			}
		};//ojo ;
	}

	
	
	public Weather getWeather() {
		return weather;
	}

	void setWeather(Weather weather) {
		if(weather == null)
			throw new IllegalArgumentException("cannot change the road weather");
		
		this.weather = weather;
	}

	public int getLength() {
		return length;
	}

	public Junction getDest() {
		return dest;
	}

	public Junction getSrc() {
		return src;
	}

	public int getContLimit() {
		return contLimit;
	}

	public int getMaxSpeed() {
		return maxSpeed;
	}

	public int getTotalCO2() {
		return totalCO2;
	}

	public int getSpeedLimit() {
		return speedLimit;
	}

	public List<Vehicle> getVehicles() {
		return Collections.unmodifiableList(vehicles);
	}
	
	void addContamination(int c) {
		if(c < 0)
			throw new IllegalArgumentException("cannot add contamination in road");
		
		totalCO2 += c;
	}
	
	void enter(Vehicle v) {
		if(v.getLocation() != 0 || v.getSpeed() != 0)
			throw new IllegalArgumentException("cannot add vehicle to road"); 
		
		vehicles.add(v);
	}
	
	void exit(Vehicle v) {
		vehicles.remove(v);
	}
	
	@Override
	void advance(int time) {
		reduceTotalContamination();
		updateSpeedLimit();
		for(Vehicle v : vehicles) {
			v.setSpeed(calculateVehicleSpeed(v));
			v.advance(time);
		}
		vehicles.sort(comp);
	}
	
	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("id", _id);
		jo.put("speedlimit", speedLimit);
		jo.put("weather", weather.toString());
		jo.put("co2", totalCO2);
		
		JSONArray ja = new JSONArray();
		for(Vehicle v : vehicles) {
			ja.put(v.getId());
		}
		jo.put("vehicles", ja);
		return jo;
		
	}
	
	abstract void reduceTotalContamination();
	abstract void updateSpeedLimit();
	abstract int calculateVehicleSpeed(Vehicle v);
}
