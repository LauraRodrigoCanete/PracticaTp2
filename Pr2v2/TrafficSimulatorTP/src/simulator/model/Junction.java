package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Junction extends SimulatedObject{
	private int x;
	private int y;
	private List<Road> entryRoadsList;//las carreteras que entran al cruce
	private Map<Junction, Road> exitRoadsMap;
	private List<List<Vehicle>> queueList;//las colas de cada carretera entrante
	private Map <Road, List<Vehicle>> queueMap;
	private int trafficLight;
	private int lastSwitch;
	private LightSwitchingStrategy lsStrategy;
	private DequeuingStrategy dqStrategy;

	
	 Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
		 super(id);
		 if(lsStrategy==null || dqStrategy == null || xCoor <0 || yCoor<0)
			  throw new IllegalArgumentException("Invalid parameters for junction");
		this.lsStrategy=lsStrategy;
		this.dqStrategy=dqStrategy;
		x=xCoor;
		y=yCoor;
		entryRoadsList=new ArrayList<Road>();
		exitRoadsMap = new HashMap<Junction, Road>();
		queueList = new ArrayList<>();
		queueMap = new HashMap<>();
		trafficLight=-1;
		lastSwitch=0;
	}

	int getX() {
		return x;
	}

	int getY() {
		return y;
	}

	public void addIncomingRoad(Road r) {
		if(!r.getDest().equals(this))
			  throw new IllegalArgumentException("Invalid road ");
		entryRoadsList.add(r);
		List<Vehicle> queue= new LinkedList<Vehicle>();
		queueList.add(queue);
		queueMap.put(r, queue);		
	}
	
	public void addOutgoingRoad(Road r) {
		if(r.getSrc()!=this || exitRoadsMap.get(r.getDest()) != null)
			  throw new IllegalArgumentException("Invalid arguments ");
		exitRoadsMap.put(r.getDest(), r);
	}
	
	void enter(Vehicle v) {
		queueMap.get(v.getRoad()).add(v);
		//vale con solo añadirlo al mapa y no a la lista porque son el mismo objeto
	}
	
	Road roadTo(Junction j) {
		return exitRoadsMap.get(j);	
	}
	
	@Override
	public void advance(int time) {
		if(trafficLight != -1) {
			List<Vehicle> vehiclesqueue = dqStrategy.dequeue(queueList.get(trafficLight));
	
			while(vehiclesqueue.size() > 0){
				/*eso hace que los objeto vehicle se muevan a la next road y se eliminen de vehiclesqueue pero en la queueList d
				de esa carretera los objetos se van a haber movido to nextRoad (pq ambas listas contienen referencias a los mismos
				objetos pero seguirán ahi, asi que tenemos que eliminarlos de la queueList de la carretera escogida tambien, que es
				la queueList.get(trafficLight)
				*/
				vehiclesqueue.get(0).moveToNextRoad();
				queueList.get(trafficLight).remove(vehiclesqueue.remove(0));
			}
		}
		int aux = lsStrategy.chooseNextGreen(entryRoadsList, queueList, trafficLight, lastSwitch, time);
		if(aux!=trafficLight) {
			lastSwitch=time;
			trafficLight=aux;
		}
	}
	
	@Override
	public JSONObject report() {
		

		JSONObject jo = new JSONObject();

		jo.put("id", this._id);

		if(this.trafficLight==-1)
			jo.put("green", "none");
		else
			jo.put("green", entryRoadsList.get(this.trafficLight).getId());

		JSONArray jo2 = new JSONArray();
		int i=0;
		for(Road list : this.entryRoadsList) {
			JSONObject Qi = new JSONObject();
			Qi.put("road", list.toString());
			JSONArray jo1 = new JSONArray();
			for(Vehicle v : queueList.get(i)) {
				jo1.put(v);
			}
			Qi.put("vehicles", jo1);
			jo2.put(Qi);
			i++;
		}	
		jo.put("queues", jo2);
		return jo;  

	}
	
}
