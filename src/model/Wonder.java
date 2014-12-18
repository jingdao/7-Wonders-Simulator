
package model;

public class Wonder {
	public String name;
	public WonderStage[] stagesA,stagesB;
	public ResourceType startingResource;

	public static Wonder Rhodes = new Wonder("RHODES",ResourceType.ORE,WonderStage.rhodesA,WonderStage.rhodesB);
	public static Wonder Alexandria = new Wonder("ALEXANDRIA",ResourceType.GLASS,WonderStage.alexandriaA,WonderStage.alexandriaB);
	public static Wonder Ephesos  = new Wonder("EPHESOS",ResourceType.PAPYRUS,WonderStage.ephesosA,WonderStage.ephesosB);
	public static Wonder Babylon = new Wonder("BABYLON",ResourceType.CLAY,WonderStage.babylonA,WonderStage.babylonB);
	public static Wonder Olympia = new Wonder("OLYMPIA",ResourceType.WOOD,WonderStage.olympiaA,WonderStage.olympiaB);
	public static Wonder Halikarnassos = new Wonder("HALIKARNASSOS",ResourceType.LOOM,WonderStage.halikarnassosA,WonderStage.halikarnassosB);
	public static Wonder Gizah = new Wonder("GIZAH",ResourceType.STONE,WonderStage.gizahA,WonderStage.gizahB);
	public static Wonder[] wonders = {Rhodes,Alexandria,Ephesos,Babylon,Olympia,Halikarnassos,Gizah};

	public Wonder(String name,ResourceType startingResource,WonderStage[] stagesA,WonderStage[] stagesB) {
		this.name=name;
		this.startingResource=startingResource;
		this.stagesA=stagesA;
		this.stagesB=stagesB;
	}
}
