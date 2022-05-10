package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.Weather;

public abstract class NewRoadEventBuilder extends Builder<Event> {

	protected int time, length, co2limit, maxspeed;
	protected String id, j1, j2;
	protected Weather w;
	
	NewRoadEventBuilder(String type) {
		super(type);
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		time=data.getInt("time");
		id=data.getString("id");
		j1= data.getString("src");
		j2= data.getString("dest");
		length= data.getInt("length");
		co2limit=data.getInt("co2limit");
		maxspeed=data.getInt("maxspeed");
		w= Weather.valueOf(data.getString("weather").toUpperCase());
		return createTheRoad();
	}
	
	abstract Event createTheRoad();

}
