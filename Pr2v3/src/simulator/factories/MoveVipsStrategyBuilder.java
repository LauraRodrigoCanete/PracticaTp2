package simulator.factories;

import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.MoveVipsStrategy;

public class MoveVipsStrategyBuilder extends Builder<DequeuingStrategy>{

	public MoveVipsStrategyBuilder() {
		super("vip_dqs");
	}

	@Override
	protected DequeuingStrategy createTheInstance(JSONObject data) {
		int limit = 1;
		if(data.has("limit")) limit = data.getInt("limit");
		
		if(!data.has("viptag"))
			throw new IllegalArgumentException("JSON has to contain viptag");
		String viptag = data.getString("viptag");
		return new MoveVipsStrategy(limit, viptag);
	}
	
}
