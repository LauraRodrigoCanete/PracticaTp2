package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;
import simulator.model.Event;
import simulator.model.DequeuingStrategy;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;

public class NewJunctionEventBuilder extends Builder<Event>{
	
	private Factory<LightSwitchingStrategy> lsFactory;
	private Factory<DequeuingStrategy> dqFactory;
	
	public NewJunctionEventBuilder(Factory<LightSwitchingStrategy> lsFactory, Factory<DequeuingStrategy> dqFactory) {
		super("new_junction");
		this.lsFactory = lsFactory;
		this.dqFactory = dqFactory;
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		JSONArray coor = data.getJSONArray("coor");
		LightSwitchingStrategy lsStrategy = lsFactory.createInstance(data.getJSONObject("ls_strategy"));
		DequeuingStrategy dqStrategy = dqFactory.createInstance(data.getJSONObject("dq_strategy"));
		return new NewJunctionEvent(data.getInt("time"), data.getString("id"), lsStrategy, dqStrategy, coor.getInt(0), coor.getInt(1));
	}
}
