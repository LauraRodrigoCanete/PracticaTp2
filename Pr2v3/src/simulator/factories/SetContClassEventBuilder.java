package simulator.factories;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetContClassEvent;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class SetContClassEventBuilder extends Builder<Event> {

	public SetContClassEventBuilder() {
		super("set_cont_class");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time=data.getInt("time");
		List<Pair<String,Integer>> cs = new ArrayList<>();
		JSONArray ja=data.getJSONArray("info");
		for(int i=0;i<ja.length();i++) {
			String id = ja.getJSONObject(i).getString("vehicle");
			Integer c = ja.getJSONObject(i).getInt("class");
			cs.add(new Pair<String,Integer>(id, c));
		}
		return new SetContClassEvent(time, cs);
	}

}
