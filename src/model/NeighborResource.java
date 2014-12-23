package model;


public class NeighborResource {
	public int leftRaw=0,leftManufactured=0,rightRaw=0,rightManufactured=0;
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

}
