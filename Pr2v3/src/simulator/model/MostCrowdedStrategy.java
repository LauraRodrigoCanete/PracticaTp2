package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy {

	private int timeSlot;

	public MostCrowdedStrategy(int timeSlot) {
		this.timeSlot=timeSlot;
	}
	
	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime, int currTime) {
		if(roads.size()==0)
			return-1;
		if(currGreen==-1)
			return maxQs(qs);
		if(currTime-lastSwitchingTime<timeSlot)
			return currGreen;
		return busquedaCircular(qs, (currGreen+1)%roads.size());
	}
	
	private int maxQs(List<List<Vehicle>> qs) {
		int index=0;
		int size=qs.get(0).size();
		for(int i=1;i<qs.size();i++) {
			if(qs.get(i).size()>size) {
				size=qs.get(i).size();
				index=i;
			}
		}
		return index;
	}
	
	private int busquedaCircular(List<List<Vehicle>> qs, int index) {
		
		int max= qs.get(index).size();
		int maxIndex=index;
		int i=(index+1)%qs.size();
		
		while(i!=index) {
			int s= qs.get(i).size();
			if(s>max) {
				max=s;
				maxIndex=i;
			}	
			i=(i+1) % qs.size();		
		}
		return maxIndex;
	}
}
