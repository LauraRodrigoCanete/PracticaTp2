package simulator.control;


import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.TrafficSimObserver;
import simulator.model.TrafficSimulator;


public class Controller {
	
	private TrafficSimulator trafficSimulator;
	private Factory<Event> eventsFactory;

	
	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory) {
		if(sim==null)
			throw new IllegalArgumentException("Invalid traffic simulator");
		trafficSimulator=sim;
		if(eventsFactory==null )
			throw new IllegalArgumentException("Invalid events factory");
		this.eventsFactory=eventsFactory;
	}
	
	public void addObserver(TrafficSimObserver o) {
		trafficSimulator.addObserver(o);
	}
	
	public void removeObserver(TrafficSimObserver o) {
		trafficSimulator.removeObserver(o);
	}
	
	public void addEvent(Event e) {
		trafficSimulator.addEvent(e);
	}
	
	public void reset() {
		trafficSimulator.reset();
	}
	
	public void loadEvents(InputStream in) {
		//nos dijo que no hacia falta comprobar que eran semanticamente iguales
		
		JSONObject jo = new JSONObject(new JSONTokener(in)); 
		JSONArray ja = jo.getJSONArray("events");
		for(int i=0;i<ja.length();i++) {
			trafficSimulator.addEvent(eventsFactory.createInstance(ja.getJSONObject(i)));
			
		}
	}
	
	
	public void run(int ticks , OutputStream out) {
		PrintStream p = new PrintStream(out);
		p.println("{");
		p.println("  \"states\": [");
		for(int i=0;i<ticks;i++) {
			trafficSimulator.advance();
			p.print(trafficSimulator.report().toString());
			if(i != ticks-1)
				p.println(",");
		}
		p.println("]");
		p.println("}");
	}
}
