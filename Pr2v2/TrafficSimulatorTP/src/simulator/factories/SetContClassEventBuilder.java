package simulator.factories;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetContClassEvent;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class SetContClassEventBuilder extends Builder<Event> {
	private int time;
	private  List<Pair<String,Integer>> cs;

	public SetContClassEventBuilder() {
		super("set_cont_class");
		cs = new ArrayList<>();
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		time=data.getInt("time");
		JSONArray ja=data.getJSONArray("info");
		for(int i=0;i<ja.length();i++) {
			String id = ja.getJSONObject(i).getString("vehicle");
			Integer c = ja.getJSONObject(i).getInt("class");
			cs.add(new Pair<String,Integer>(id, c));
		}
		return new SetContClassEvent(time, cs);
	}

}
