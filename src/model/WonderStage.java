package model;

public class WonderStage {
	public int costClay,costOre,costStone,costWood,costGlass,costLoom,costPapyrus;
	public int numCoin,numShield,numVictory;
	public SpecialResource special;

	public static WonderStage rhodesA1=new WonderStage(0,0,0,2,0,0,0,0,0,3,SpecialResource.NONE);
	public static WonderStage rhodesA2=new WonderStage(3,0,0,0,0,0,0,0,2,0,SpecialResource.NONE);
	public static WonderStage rhodesA3=new WonderStage(0,4,0,0,0,0,0,0,0,7,SpecialResource.NONE);
	public static WonderStage alexandriaA1=new WonderStage(0,0,2,0,0,0,0,0,0,3,SpecialResource.NONE);
	public static WonderStage alexandriaA2=new WonderStage(0,2,0,0,0,0,0,0,0,0,SpecialResource.RAW_MATERIALS);
	public static WonderStage alexandriaA3=new WonderStage(0,0,0,0,2,0,0,0,0,7,SpecialResource.NONE);
	public static WonderStage ephesosA1=new WonderStage(0,2,0,0,0,0,0,0,0,3,SpecialResource.NONE);
	public static WonderStage ephesosA2=new WonderStage(0,0,0,2,0,0,0,9,0,0,SpecialResource.NONE);
	public static WonderStage ephesosA3=new WonderStage(0,0,0,0,0,0,2,0,0,7,SpecialResource.NONE);
	public static WonderStage babylonA1=new WonderStage(2,0,0,0,0,0,0,0,0,3,SpecialResource.NONE);
	public static WonderStage babylonA2=new WonderStage(0,0,0,3,0,0,0,0,0,0,SpecialResource.SCIENCE);
	public static WonderStage babylonA3=new WonderStage(4,0,0,0,0,0,0,0,0,7,SpecialResource.NONE);
	public static WonderStage olympiaA1=new WonderStage(0,0,0,2,0,0,0,0,0,3,SpecialResource.NONE);
	public static WonderStage olympiaA2=new WonderStage(0,0,2,0,0,0,0,0,0,0,SpecialResource.FREE_BUILD);
	public static WonderStage olympiaA3=new WonderStage(0,2,0,0,0,0,0,0,0,7,SpecialResource.NONE);
	public static WonderStage halikarnassosA1=new WonderStage(2,0,0,0,0,0,0,0,0,3,SpecialResource.NONE);
	public static WonderStage halikarnassosA2=new WonderStage(0,3,0,0,0,0,0,0,0,0,SpecialResource.PLAY_FROM_DISCARD);
	public static WonderStage halikarnassosA3=new WonderStage(0,0,0,0,0,2,0,0,0,7,SpecialResource.NONE);
	public static WonderStage gizahA1=new WonderStage(0,2,0,0,0,0,0,0,0,3,SpecialResource.NONE);
	public static WonderStage gizahA2=new WonderStage(0,0,0,3,0,0,0,0,0,5,SpecialResource.NONE);
	public static WonderStage gizahA3=new WonderStage(0,4,0,0,0,0,0,0,0,7,SpecialResource.NONE);
	public static WonderStage rhodesB1=new WonderStage(0,0,3,0,0,0,0,3,1,3,SpecialResource.NONE);
	public static WonderStage rhodesB2=new WonderStage(0,4,0,0,0,0,0,4,1,4,SpecialResource.NONE);
	public static WonderStage alexandriaB1=new WonderStage(2,0,0,0,0,0,0,0,0,0,SpecialResource.RAW_MATERIALS);
	public static WonderStage alexandriaB2=new WonderStage(0,0,0,2,0,0,0,0,0,0,SpecialResource.MANUFACTURED_GOODS);
	public static WonderStage alexandriaB3=new WonderStage(0,0,3,0,0,0,0,0,0,7,SpecialResource.NONE);
	public static WonderStage ephesosB1=new WonderStage(0,0,2,0,0,0,0,4,0,2,SpecialResource.NONE);
	public static WonderStage ephesosB2=new WonderStage(0,0,0,2,0,0,0,4,0,3,SpecialResource.NONE);
	public static WonderStage ephesosB3=new WonderStage(0,0,0,0,1,1,1,4,0,5,SpecialResource.NONE);
	public static WonderStage babylonB1=new WonderStage(1,0,0,0,0,1,0,0,0,3,SpecialResource.NONE);
	public static WonderStage babylonB2=new WonderStage(0,0,0,2,1,0,0,0,0,0,SpecialResource.PLAY_LAST_CARD);
	public static WonderStage babylonB3=new WonderStage(3,0,0,0,0,0,1,0,0,0,SpecialResource.SCIENCE);
	public static WonderStage olympiaB1=new WonderStage(0,0,0,2,0,0,0,0,0,0,SpecialResource.TRADING);
	public static WonderStage olympiaB2=new WonderStage(0,0,2,0,0,0,0,0,0,5,SpecialResource.NONE);
	public static WonderStage olympiaB3=new WonderStage(0,2,0,0,0,1,0,0,0,0,SpecialResource.GUILD);
	public static WonderStage halikarnassosB1=new WonderStage(0,2,0,0,0,0,0,0,0,2,SpecialResource.PLAY_FROM_DISCARD);
	public static WonderStage halikarnassosB2=new WonderStage(3,0,0,0,0,0,0,0,0,1,SpecialResource.PLAY_FROM_DISCARD);
	public static WonderStage halikarnassosB3=new WonderStage(0,0,0,0,1,1,1,0,0,0,SpecialResource.PLAY_FROM_DISCARD);
	public static WonderStage gizahB1=new WonderStage(0,0,0,2,0,0,0,0,0,3,SpecialResource.NONE);
	public static WonderStage gizahB2=new WonderStage(0,0,3,0,0,0,0,0,0,5,SpecialResource.NONE);
	public static WonderStage gizahB3=new WonderStage(3,0,0,0,0,0,0,0,0,5,SpecialResource.NONE);
	public static WonderStage gizahB4=new WonderStage(0,0,4,0,0,0,1,0,0,7,SpecialResource.NONE);

	public static WonderStage[] rhodesA={rhodesA1,rhodesA2,rhodesA3};
	public static WonderStage[] alexandriaA={alexandriaA1,alexandriaA2,alexandriaA3};
	public static WonderStage[] ephesosA={ephesosA1,ephesosA2,ephesosA3};
	public static WonderStage[] babylonA={babylonA1,babylonA2,babylonA3};
	public static WonderStage[] olympiaA={olympiaA1,olympiaA2,olympiaA3};
	public static WonderStage[] halikarnassosA={halikarnassosA1,halikarnassosA2,halikarnassosA3};
	public static WonderStage[] gizahA={gizahA1,gizahA2,gizahA3};
	public static WonderStage[] rhodesB={rhodesB1,rhodesB2};
	public static WonderStage[] alexandriaB={alexandriaB1,alexandriaB2,alexandriaB3};
	public static WonderStage[] ephesosB={ephesosB1,ephesosB2,ephesosB3};
	public static WonderStage[] babylonB={babylonB1,babylonB2,babylonB3};
	public static WonderStage[] olympiaB={olympiaB1,olympiaB2,olympiaB3};
	public static WonderStage[] halikarnassosB={halikarnassosB1,halikarnassosB2,halikarnassosB3};
	public static WonderStage[] gizahB={gizahB1,gizahB2,gizahB3,gizahB4};

	public WonderStage(int costClay,int costOre,int costStone,int costWood,int costGlass,int costLoom,int costPapyrus,int numCoin,int numShield,int numVictory,SpecialResource special) {
		this.costClay=costClay;
		this.costOre=costOre;
		this.costStone=costStone;
		this.costWood=costWood;
		this.costGlass=costGlass;
		this.costLoom=costLoom;
		this.costPapyrus=costPapyrus;
		this.numCoin=numCoin;
		this.numShield=numShield;
		this.numVictory=numVictory;
		this.special=special;
	}
}
