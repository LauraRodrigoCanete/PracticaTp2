package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class SetWeatherEvent extends Event{
	
	private  List<Pair<String,Weather>> ws;
	
	public SetWeatherEvent(int time, List<Pair<String,Weather>> ws) {
		super(time);
		if(ws==null)
			throw new IllegalArgumentException("Invalid parameters");
		this.ws=ws;
	} 


	@Override
	void execute(RoadMap map) {
		for(int i=0;i<ws.size();i++) {
			Road r=map.getRoad(ws.get(i).getFirst());
			if(r==null)
				throw new IllegalArgumentException("Invalid road");
			r.setWeather(ws.get(i).getSecond());
		}
	}

}
