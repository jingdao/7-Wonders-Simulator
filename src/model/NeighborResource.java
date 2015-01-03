package model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class NeighborResource {
	public static int nid=0;
	public int leftRaw=0,leftManufactured=0,rightRaw=0,rightManufactured=0;
	public int prerequisite;
	public int id;
	public NeighborResource(int leftRaw,int leftManufactured,int rightRaw,int rightManufactured) {
		this.leftRaw=leftRaw;
		this.leftManufactured=leftManufactured;
		this.rightRaw=rightRaw;
		this.rightManufactured=rightManufactured;
	}

	public NeighborResource(boolean leftOrRight,boolean rawOrManufactured) {
		if (leftOrRight) {
			if (rawOrManufactured) leftRaw=1;
			else leftManufactured=1;
		} else {
			if (rawOrManufactured) rightRaw=1;
			else rightManufactured=1;
		}
		this.id=nid;
	}

	public NeighborResource(boolean leftOrRight,boolean rawOrManufactured,int prerequisite) {
		if (leftOrRight) {
			if (rawOrManufactured) leftRaw=1;
			else leftManufactured=1;
		} else {
			if (rawOrManufactured) rightRaw=1;
			else rightManufactured=1;
		}
		this.prerequisite=prerequisite;
		this.id=nid;
	}

	public NeighborResource(int prerequisite) {
		this.prerequisite=prerequisite;
		this.id=nid;
	}

	public NeighborResource addExtraResource(boolean leftOrRight,boolean rawOrManufactured) {
		if (leftOrRight) {
			if (rawOrManufactured) return new NeighborResource(leftRaw+1,leftManufactured,rightRaw,rightManufactured);
			else return new NeighborResource(leftRaw,leftManufactured+1,rightRaw,rightManufactured);
		} else {
			if (rawOrManufactured) return new NeighborResource(leftRaw,leftManufactured,rightRaw+1,rightManufactured);
			else return new NeighborResource(leftRaw,leftManufactured,rightRaw,rightManufactured+1);
		}
	}

	public static HashSet<Integer> getCost(int resourceCode, int leftTradingCostRaw, int rightTradingCostRaw, int tradingCostManufactured,
							 HashMap<Integer,ArrayList<NeighborResource>> resourceMap,HashSet<Integer> pid) {
//		System.out.println(getStringFromResourceCode(resourceCode)+":"+pid);
		HashSet<Integer> set = new HashSet<Integer>();
		ArrayList<NeighborResource> a = resourceMap.get(resourceCode);
		if (a==null) return null;
		for (NeighborResource n:a) {
			if (pid.contains(n.id)) continue;
			int currentCost=(n.leftRaw*leftTradingCostRaw+n.leftManufactured*tradingCostManufactured)*100+
							(n.rightRaw*rightTradingCostRaw+n.rightManufactured*tradingCostManufactured);
			if (n.prerequisite==0) set.add(currentCost);
			else {
				HashSet<Integer> newId = new HashSet<Integer>(pid);
				newId.add(n.id);
				HashSet<Integer> h = getCost(n.prerequisite,leftTradingCostRaw,rightTradingCostRaw,tradingCostManufactured,resourceMap,newId);
				if (h!=null) for (int i:h) set.add(i+currentCost);
				else set.add(currentCost);
			}
		}
		return set;
	}

	public static String getStringFromResourceCode(int resourceCode){
		String s="";
		for (int i=0;i<7;i++) {
			s+=""+resourceCode%5;
			resourceCode/=5;
		}
		return s;
	} 

	public String toString() {
		return leftRaw+""+leftManufactured+""+rightRaw+""+rightManufactured+NeighborResource.getStringFromResourceCode(prerequisite);
	}
}
