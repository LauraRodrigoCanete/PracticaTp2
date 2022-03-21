package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class SetContClassEvent extends Event {
	
	private  List<Pair<String,Integer>> cs;

	public SetContClassEvent(int time, List<Pair<String,Integer>> cs) {
		super(time);
		if(cs==null)
			  throw new IllegalArgumentException("Invalid parameters");
		this.cs=cs;
	} 
	
	@Override
	void execute(RoadMap map) {
		for(int i=0;i<cs.size();i++) {
			Vehicle v=map.getVehicle(cs.get(i).getFirst());
			if(v==null)
				  throw new IllegalArgumentException("Invalid vehicle");
			v.setContClass(cs.get(i).getSecond());
		}
	}
	
	@Override
	public String toString() {
		return "New SetContClassEvent";
	}
}
