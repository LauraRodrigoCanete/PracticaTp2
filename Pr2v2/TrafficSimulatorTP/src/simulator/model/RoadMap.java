package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class RoadMap {


	private List<Junction> junctionList;
	private List<Road> roadList;
	private List<Vehicle> vehicleList;
	private Map<String, Junction> junctionMap;
	private Map<String, Road> roadMap;
	private Map<String, Vehicle> vehicleMap;




	public RoadMap(){
		junctionList= new ArrayList<Junction>();
		roadList = new ArrayList<Road>();
		vehicleList= new ArrayList<Vehicle>();
		junctionMap= new HashMap<String,Junction>();
		roadMap= new HashMap<String, Road>();
		vehicleMap= new HashMap<String, Vehicle>();
	}

	void reset(){
		junctionList.clear();
		roadList.clear();
		vehicleList.clear();
		junctionMap.clear();
		roadMap.clear();
		vehicleMap.clear();
	}

	void addJunction(Junction j){
		if(junctionMap.containsKey(j.getId()))
			throw new IllegalArgumentException("Invalid key: "+j.getId());
		else {
			junctionList.add(j);
			junctionMap.put(j.getId(), j);
		}
	}
	void addRoad(Road r) {
		if(roadMap.containsKey(r.getId()))
			throw new IllegalArgumentException("Invalid key: "+r.getId());
		else if(!correctJunctions(r))
			throw new IllegalArgumentException("Incorrect road junctions");
		else {
			roadList.add(r);
			roadMap.put(r.getId(), r);
		}
	}
	private boolean correctJunctions(Road r) {
		return junctionMap.containsValue(r.getSrc()) && junctionMap.containsValue(r.getDest());  
	}

	void addVehicle(Vehicle v){

		if(vehicleMap.containsKey(v.getId()))
			throw new IllegalArgumentException("Invalid key: "+v.getId());
		else if(!correctItinerary(v))
			throw new IllegalArgumentException("Incorrect itinerary");
		else {
			vehicleList.add(v);
			vehicleMap.put(v.getId(), v);
		}
	}
	private boolean correctItinerary(Vehicle v) {
		boolean ok=true;
		for(int i=0;i<v.getItinerary().size()-1;i++) {
			if(v.getItinerary().get(i).roadTo(v.getItinerary().get(i+1))==null)
				ok=false;
		}
		return ok;
	}

	public Vehicle getVehicle(String s){
		return vehicleMap.get(s);	  
	}
	public List<Vehicle> getVehicles(){
		return Collections.unmodifiableList(vehicleList);
	}
	public Road getRoad(String s) {
		return roadMap.get(s);	  
	} 
	public List<Road> getRoads(){
		return Collections.unmodifiableList(roadList);
	} 
	public Junction getJunction(String s) {
		return junctionMap.get(s);	  
	}
	public List<Junction> getJunctions(){
		return Collections.unmodifiableList(junctionList);
	} 
	public JSONObject report() {
		JSONArray jo1 = new JSONArray();
		for(Junction j : this.junctionList) {
			jo1.put(j.report());
		}
		JSONArray jo2 = new JSONArray();
		for(Road r : this.roadList) {
			jo2.put(r.report());
		}

		JSONArray jo3 = new JSONArray();
		for(Vehicle v : vehicleList) {
			jo3.put(v.report());
		}
		JSONObject jo = new JSONObject();
		jo.put("junctions", jo1);
		jo.put("roads", jo2);
		jo.put("vehicles", jo3);

		return jo;  
	}
}
