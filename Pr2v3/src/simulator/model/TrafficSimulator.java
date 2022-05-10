package simulator.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

import simulator.misc.SortedArrayList;

public class TrafficSimulator implements Observable<TrafficSimObserver>{

	private List<TrafficSimObserver> observers;
	private RoadMap map;
	private List<Event> events;//ordenada por el tiempo de los eventos
	private List<Event> eventsRO;
	private int time;
	
	public TrafficSimulator() {
		super();
		map = new RoadMap();
		time = 0;

		//usa el orden natural de los eventos (que implementan comparable), está hecho así en la clase de sorted
		events = new SortedArrayList<Event>();
		eventsRO = Collections.unmodifiableList(events);
		
		observers = new ArrayList<TrafficSimObserver>();
	}

	public void addObserver(TrafficSimObserver o) {
		observers.add(o);
		o.onRegister(map, eventsRO, time);
	}
	
	public void removeObserver(TrafficSimObserver o) {
		observers.remove(o);
	}
	
	public void reset() {
		map.reset();
		events.clear();
		time = 0;
		for(TrafficSimObserver t: observers) {
			t.onReset(map, eventsRO, time);
		}
	}
	
	public void addEvent(Event e) {
		if(e.getTime() <= this.time) {
			for(TrafficSimObserver t: observers) {
				t.onError("the event's time must be larger than the current time");
			}
			throw new IllegalArgumentException ("the event's time must be larger than the current time");
		}
		events.add(e);
		for(TrafficSimObserver t: observers) {
			t.onEventAdded(map, eventsRO, e, time);
		}
	}
	
	public void advance() {
		
		time++;
		for(TrafficSimObserver t: observers) {
			t.onAdvanceStart(map, eventsRO, time);
		}
		
		try {	
			while(events.size() > 0 && events.get(0).getTime() == time)//tiene que ser un while
				events.remove(0).execute(map);
			
			List<Junction> lj = map.getJunctions();
			for(Junction j : lj) {
				j.advance(time);
			}
			
			List<Road> lr = map.getRoads();
			for(Road r : lr) {
				r.advance(time);//el advance de carreteras dice a los vehiculos que avancen
			}
			for(TrafficSimObserver t: observers) {
				t.onAdvanceEnd(map, eventsRO, time);
			}
		}
		catch(Exception i) {
			for(TrafficSimObserver t: observers) {
				t.onError(i.getMessage());
			}
			throw i;
		}
		
	}
	
	public JSONObject report() {
		JSONObject jo1 = new JSONObject();
		jo1.put("time", time);
		
		JSONObject jo2 = map.report();
		jo1.put("state", jo2);
		return jo1;
		
	}
}
