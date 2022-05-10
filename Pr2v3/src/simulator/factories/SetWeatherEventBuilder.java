package simulator.factories;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event> {

	
	public SetWeatherEventBuilder() {
		super("set_weather");
		
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		List<Pair<String,Weather>> ws = new ArrayList<>();
		int time = data.getInt("time");
		JSONArray ja = data.getJSONArray("info");
		for(int i=0;i<ja.length();i++) {
			String id = ja.getJSONObject(i).getString("road");
			Weather w = Weather.valueOf(ja.getJSONObject(i).getString("weather").toUpperCase());
			ws.add(new Pair<String,Weather>(id, w));
		}
		return new SetWeatherEvent(time, ws);
	}

}
