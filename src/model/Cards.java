package model;

import java.util.HashMap;

public class Cards {
	
	public String name;
	public int costClay,costOre,costStone,costWood,costGlass,costLoom,costPapyrus;
	public int costCoin;
	public CardType type;
	public ResourceType rtype;
	public int resourceValue;
	public String dependency;
	
	public static Cards Lumberyard = new Cards("LUMBER YARD",0,0,0,0,0,0,0,0,CardType.BROWN,ResourceType.WOOD,1,null);
	public static Cards Stonepit = new Cards("STONE PIT",0,0,0,0,0,0,0,0,CardType.BROWN,ResourceType.STONE,1,null);
	public static Cards Claypool = new Cards("CLAY POOL",0,0,0,0,0,0,0,0,CardType.BROWN,ResourceType.CLAY,1,null);
	public static Cards Orevein = new Cards("ORE VEIN",0,0,0,0,0,0,0,0,CardType.BROWN,ResourceType.ORE,1,null);
	public static Cards Treefarm = new Cards("TREE FARM",0,0,0,0,0,0,0,1,CardType.BROWN,ResourceType.CLAYWOOD,1,null);
	public static Cards Excavation = new Cards("EXCAVATION",0,0,0,0,0,0,0,1,CardType.BROWN,ResourceType.CLAYSTONE,1,null);
	public static Cards Claypit = new Cards("CLAY PIT",0,0,0,0,0,0,0,1,CardType.BROWN,ResourceType.CLAYORE,1,null);
	public static Cards Timberyard = new Cards("TIMBER YARD",0,0,0,0,0,0,0,1,CardType.BROWN,ResourceType.STONEWOOD,1,null);
	public static Cards Forestcave = new Cards("FOREST CAVE",0,0,0,0,0,0,0,1,CardType.BROWN,ResourceType.OREWOOD,1,null);
	public static Cards Mine = new Cards("MINE",0,0,0,0,0,0,0,1,CardType.BROWN,ResourceType.ORESTONE,1,null);
	public static Cards Loom = new Cards("LOOM",0,0,0,0,0,0,0,0,CardType.GRAY,ResourceType.LOOM,1,null);
	public static Cards Glassworks= new Cards("GLASSWORKS",0,0,0,0,0,0,0,0,CardType.GRAY,ResourceType.GLASS,1,null);
	public static Cards Press = new Cards("PRESS",0,0,0,0,0,0,0,0,CardType.GRAY,ResourceType.PAPYRUS,1,null);
	public static Cards Pawnshop = new Cards("PAWNSHOP",0,0,0,0,0,0,0,0,CardType.BLUE,ResourceType.VICTORY,3,null);
	public static Cards Baths = new Cards("BATHS",0,0,1,0,0,0,0,0,CardType.BLUE,ResourceType.VICTORY,3,null);
	public static Cards Altar = new Cards("ALTAR",0,0,0,0,0,0,0,0,CardType.BLUE,ResourceType.VICTORY,2,null);
	public static Cards Theater = new Cards("THEATER",0,0,0,0,0,0,0,0,CardType.BLUE,ResourceType.VICTORY,2,null);
	public static Cards Tavern = new Cards("TAVERN",0,0,0,0,0,0,0,0,CardType.YELLOW,ResourceType.COIN,5,null);
	public static Cards Easttradingpost = new Cards("EAST TRADING POST",0,0,0,0,0,0,0,0,CardType.YELLOW,ResourceType.COMMERCE,0,null);
	public static Cards Westtradingpost = new Cards("WEST TRADING POST",0,0,0,0,0,0,0,0,CardType.YELLOW,ResourceType.COMMERCE,0,null);
	public static Cards Marketplace = new Cards("MARKETPLACE",0,0,0,0,0,0,0,0,CardType.YELLOW,ResourceType.COMMERCE,0,null);
	public static Cards Apothecary = new Cards("APOTHECARY",0,0,0,0,0,1,0,0,CardType.GREEN,ResourceType.SCIENCE,ScienceType.COMPASS.ordinal(),null);
	public static Cards Workshop = new Cards("WORKSHOP",0,0,0,0,1,0,0,0,CardType.GREEN,ResourceType.SCIENCE,ScienceType.GEAR.ordinal(),null);
	public static Cards Scriptorium = new Cards("SCRIPTORIUM",0,0,0,0,0,0,1,0,CardType.GREEN,ResourceType.SCIENCE,ScienceType.TABLET.ordinal(),null);
	public static Cards Stockade = new Cards("STOCKADE",0,0,0,1,0,0,0,0,CardType.RED,ResourceType.SHIELD,1,null);
	public static Cards Barracks = new Cards("BARRACKS",0,1,0,0,0,0,0,0,CardType.RED,ResourceType.SHIELD,1,null);
	public static Cards Guardtower = new Cards("GUARD TOWER",1,0,0,0,0,0,0,0,CardType.RED,ResourceType.SHIELD,1,null);

	public static Cards Sawmill = new Cards("SAW MILL",0,0,0,0,0,0,0,1,CardType.BROWN,ResourceType.WOOD,2,null);
	public static Cards Quarry = new Cards("QUARRY",0,0,0,0,0,0,0,1,CardType.BROWN,ResourceType.STONE,2,null);
	public static Cards Brickyard = new Cards("BRICKYARD",0,0,0,0,0,0,0,1,CardType.BROWN,ResourceType.CLAY,2,null);
	public static Cards Foundry = new Cards("FOUNDRY",0,0,0,0,0,0,0,1,CardType.BROWN,ResourceType.ORE,2,null);
	public static Cards Aqueduct = new Cards("AQUEDUCT",0,0,3,0,0,0,0,0,CardType.BLUE,ResourceType.VICTORY,5,"BATHS");
	public static Cards Temple = new Cards("TEMPLE",1,0,0,1,1,0,0,0,CardType.BLUE,ResourceType.VICTORY,5,"ALTAR");
	public static Cards Statue = new Cards("STATUE",0,1,0,1,0,0,0,0,CardType.BLUE,ResourceType.VICTORY,5,"THEATER");
	public static Cards Courthouse = new Cards("COURTHOUSE",2,0,0,0,0,1,0,0,CardType.BLUE,ResourceType.VICTORY,5,"SCRIPTORIUM");
	public static Cards Forum = new Cards("FORUM",2,0,0,0,0,0,0,0,CardType.YELLOW,ResourceType.COMMERCE,0,"TRADING POST");
	public static Cards Caravansery = new Cards("CARAVANSERY",0,0,0,2,0,0,0,0,CardType.YELLOW,ResourceType.COMMERCE,0,"MARKETPLACE");
	public static Cards Vineyard = new Cards("VINEYARD",0,0,0,0,0,0,0,0,CardType.YELLOW,ResourceType.COMMERCE,0,null);
	public static Cards Bazar = new Cards("BAZAR",0,0,0,0,0,0,0,0,CardType.YELLOW,ResourceType.COMMERCE,0,null);
	public static Cards Walls = new Cards("WALLS",0,0,3,0,0,0,0,0,CardType.RED,ResourceType.SHIELD,2,null);
	public static Cards Trainingground= new Cards("TRAINING GROUND",0,2,0,1,0,0,0,0,CardType.RED,ResourceType.SHIELD,2,null);
	public static Cards Stables = new Cards("STABLES",1,1,0,1,0,0,0,0,CardType.RED,ResourceType.SHIELD,2,"APOTHECARY");
	public static Cards Archeryrange = new Cards("ARCHERY RANGE",0,1,0,2,0,0,0,0,CardType.RED,ResourceType.SHIELD,2,"WORKSHOP");
	public static Cards Dispensary = new Cards("DISPENSARY",0,2,0,0,1,0,0,0,CardType.GREEN,ResourceType.SCIENCE,ScienceType.COMPASS.ordinal(),"APOTHECARY");
	public static Cards Laboratory = new Cards("LABORATORY",2,0,0,0,0,0,1,0,CardType.GREEN,ResourceType.SCIENCE,ScienceType.GEAR.ordinal(),"WORKSHOP");
	public static Cards Library = new Cards("LIBRARY",0,0,2,0,0,1,0,0,CardType.GREEN,ResourceType.SCIENCE,ScienceType.TABLET.ordinal(),"SCRIPTORIUM");
	public static Cards School = new Cards("SCHOOL",0,0,0,1,0,0,1,0,CardType.GREEN,ResourceType.SCIENCE,ScienceType.TABLET.ordinal(),null);

	public static Cards Pantheon = new Cards("PANTHEON",2,1,0,0,1,1,1,0,CardType.BLUE,ResourceType.VICTORY,7,"PANTHEON");
	public static Cards Gardens = new Cards("GARDENS",2,0,0,1,0,0,0,0,CardType.BLUE,ResourceType.VICTORY,5,"STATUE");
	public static Cards Townhall = new Cards("TOWN HALL",0,1,2,0,1,0,0,0,CardType.BLUE,ResourceType.VICTORY,6,null);
	public static Cards Palace = new Cards("PALACE",1,1,1,1,1,1,1,0,CardType.BLUE,ResourceType.VICTORY,8,null);
	public static Cards Senate = new Cards("SENATE",0,1,1,2,0,0,0,0,CardType.BLUE,ResourceType.VICTORY,6,"LIBRARY");
	public static Cards Haven = new Cards("HAVEN",0,1,0,1,0,1,0,0,CardType.YELLOW,ResourceType.COMMERCE,0,"FORUM");
	public static Cards Lighthouse = new Cards("LIGHTHOUSE",0,0,1,0,1,0,0,0,CardType.YELLOW,ResourceType.COMMERCE,0,"CARAVANSERY");
	public static Cards Chamberofcommerce = new Cards("CHAMBER OF COMMERCE",2,0,0,0,0,0,0,1,CardType.YELLOW,ResourceType.COMMERCE,0,null);
	public static Cards Arena = new Cards("ARENA",0,1,2,0,0,0,0,0,CardType.YELLOW,ResourceType.COMMERCE,0,"DISPENSARY");
	public static Cards Fortifications = new Cards("FORTIFICATIONS",0,3,1,0,0,0,0,0,CardType.RED,ResourceType.SHIELD,3,"WALLS");
	public static Cards Circus = new Cards("CIRCUS",0,1,3,0,0,0,0,0,CardType.RED,ResourceType.SHIELD,3,"TRAINING GROUNDS");
	public static Cards Arsenal = new Cards("ARSENAL",0,1,0,2,0,1,0,0,CardType.RED,ResourceType.SHIELD,3,null);
	public static Cards Siegeworkshop = new Cards("SIEGE WORKSHOP",3,0,0,1,0,0,0,0,CardType.RED,ResourceType.SHIELD,3,"LABORATORY");
	public static Cards Lodge = new Cards("LODGE",2,0,0,0,0,1,1,0,CardType.GREEN,ResourceType.SCIENCE,ScienceType.COMPASS.ordinal(),"DISPENSARY");
	public static Cards Observatory = new Cards("OBSERVATORY",0,2,0,0,1,1,0,0,CardType.GREEN,ResourceType.SCIENCE,ScienceType.GEAR.ordinal(),"LABORATORY");
	public static Cards University = new Cards("UNIVERSITY",0,0,0,2,1,0,1,0,CardType.GREEN,ResourceType.SCIENCE,ScienceType.TABLET.ordinal(),"LIBRARY");
	public static Cards Academy = new Cards("ACADEMY",0,0,3,0,1,0,0,0,CardType.GREEN,ResourceType.SCIENCE,ScienceType.COMPASS.ordinal(),"SCHOOL");
	public static Cards Study = new Cards("STUDY",0,0,0,1,0,1,1,0,CardType.GREEN,ResourceType.SCIENCE,ScienceType.GEAR.ordinal(),"SCHOOL");
	public static Cards Workersguild = new Cards("WORKERS GUILD",1,2,1,1,0,0,0,0,CardType.PURPLE,ResourceType.GUILD,0,null);
	public static Cards Craftsmensguild = new Cards("CRAFTSMENS GUILD",0,2,2,0,0,0,0,0,CardType.PURPLE,ResourceType.GUILD,0,null);
	public static Cards Tradersguild = new Cards("TRADERS GUILD",0,0,0,0,1,1,1,0,CardType.PURPLE,ResourceType.GUILD,0,null);
	public static Cards Philosophersguild = new Cards("PHILOSOPHERS GUILD",3,0,0,0,0,1,1,0,CardType.PURPLE,ResourceType.GUILD,0,null);
	public static Cards Spyguild = new Cards("SPY GUILD",3,0,0,0,1,0,0,0,CardType.PURPLE,ResourceType.GUILD,0,null);
	public static Cards Strategyguild = new Cards("STRATEGY GUILD",0,2,1,0,0,1,0,0,CardType.PURPLE,ResourceType.GUILD,0,null);
	public static Cards Shipownersguild = new Cards("SHIPOWNERS GUILD",0,0,0,3,1,0,1,0,CardType.PURPLE,ResourceType.GUILD,0,null);
	public static Cards Scientistsguild = new Cards("SCIENTISTS GUILD",0,2,0,2,0,0,1,0,CardType.PURPLE,ResourceType.GUILD,0,null);
	public static Cards Magistratesguild = new Cards("MAGISTRATES GUILD",0,0,1,3,0,1,0,0,CardType.PURPLE,ResourceType.GUILD,0,null);
	public static Cards Buildersguild = new Cards("BUILDERS GUILD",2,0,2,0,1,0,0,0,CardType.PURPLE,ResourceType.GUILD,0,null);

	
	public static Cards[] age1group3={Lumberyard,Stonepit,Claypool,Orevein,Claypit,Timberyard,
									Loom,Glassworks,Press,Baths,Altar,Theater,
									Easttradingpost,Westtradingpost,Marketplace,Stockade,Barracks,Guardtower,
									Apothecary,Workshop,Scriptorium};
	public static Cards[] age1group4={Lumberyard,Orevein,Excavation,Pawnshop,Tavern,Guardtower,Scriptorium};
	public static Cards[] age1group5={Stonepit,Claypool,Forestcave,Altar,Tavern,Barracks,Apothecary};
	public static Cards[] age1group6={Treefarm,Mine,Loom,Glassworks,Press,Theater,Marketplace};
	public static Cards[] age1group7={Pawnshop,Baths,Tavern,Easttradingpost,Westtradingpost,Stockade,Workshop};
	public static Cards[] age2group3={Sawmill,Quarry,Brickyard,Foundry,Loom,Glassworks,Press,
										Aqueduct,Temple,Statue,Courthouse,Forum,Caravansery,Vineyard,
										Walls,Stables,Archeryrange,Dispensary,Laboratory,Library,School};
	public static Cards[] age2group4={Sawmill,Quarry,Brickyard,Foundry,Bazar,Trainingground,Dispensary};
	public static Cards[] age2group5={Loom,Glassworks,Press,Courthouse,Caravansery,Stables,Laboratory};
	public static Cards[] age2group6={Temple,Forum,Caravansery,Vineyard,Trainingground,Archeryrange,Library};
	public static Cards[] age2group7={Aqueduct,Statue,Forum,Bazar,Walls,Trainingground,School};
	public static Cards[] age3group3={Pantheon,Gardens,Townhall,Palace,Senate,Haven,Lighthouse,Arena,
										Fortifications,Arsenal,Siegeworkshop,Lodge,Observatory,University,Academy,Study};
	public static Cards[] age3group4={Gardens,Haven,Chamberofcommerce,Circus,Arsenal,University};
	public static Cards[] age3group5={Townhall,Senate,Arena,Circus,Siegeworkshop,Study};
	public static Cards[] age3group6={Pantheon,Townhall,Lighthouse,Chamberofcommerce,Circus,Lodge};
	public static Cards[] age3group7={Palace,Arena,Fortifications,Arsenal,Observatory,Academy};
	public static Cards[] age3guilds={Workersguild,Craftsmensguild,Tradersguild,Philosophersguild,Spyguild,Strategyguild,Shipownersguild,Scientistsguild,Magistratesguild,Buildersguild};

	public static HashMap<String,String[]> dependencyMap;
	
	public static void buildDependencyMap() {
		dependencyMap = new HashMap<String,String[]>();
		String[] s1 = {"COURTHOUSE","LIBRARY"};
		dependencyMap.put("SCRIPTORIUM",s1);
		String[] s2 = {"AQUEDUCT"};
		dependencyMap.put("BATHS",s2);
		String[] s3 = {"TEMPLE"};
		dependencyMap.put("ALTAR",s3);
		String[] s4 = {"STATUE"};
		dependencyMap.put("THEATER",s4);
		String[] s5 = {"PANTHEON"};
		dependencyMap.put("TEMPLE",s5);
		String[] s6 = {"GARDENS"};
		dependencyMap.put("STATUE",s6);
		String[] s7 = {"BATHS"};
		dependencyMap.put("AQUEDUCT",s7);
		String[] s8 = {"FORUM"};
		dependencyMap.put("WEST TRADING POST",s8);
		String[] s9 = {"FORUM"};
		dependencyMap.put("EAST TRADING POST",s9);
		String[] s10 = {"HAVEN"};
		dependencyMap.put("FORUM",s10);
		String[] s11 = {"CARAVANSERY"};
		dependencyMap.put("MARKETPLACE",s11);
		String[] s12 = {"FORTIFICATIONS"};
		dependencyMap.put("WALLS",s12);
		String[] s13 = {"CIRCUS"};
		dependencyMap.put("TRAINING GROUND",s13);
		String[] s14 = {"STABLES","DISPENSARY"};
		dependencyMap.put("APOTHECARY",s14);
		String[] s15 = {"ARENA","LODGE"};
		dependencyMap.put("DISPENSARY",s15);
		String[] s16 = {"ARCHERY RANGE","LABORATORY"};
		dependencyMap.put("WORKSHOP",s16);
		String[] s17 = {"SIEGE WORKSHOP","OBSERVATORY"};
		dependencyMap.put("LABORATORY",s17);
		String[] s18 = {"SENATE","UNIVERSITY"};
		dependencyMap.put("LIBRARY",s18);
		String[] s19 = {"ACADEMY","STUDY"};
		dependencyMap.put("SCHOOL",s19);
	}

	public Cards(String name,int costClay,int costOre,int costStone,int costWood,int costGlass,int costLoom,int costPapyrus,int costCoin,CardType type,ResourceType rtype,int resourceValue,String dependency) {
		this.name=name;
		this.costClay=costClay;
		this.costOre=costOre;
		this.costStone=costStone;
		this.costWood=costWood;
		this.costGlass=costGlass;
		this.costLoom=costLoom;
		this.costPapyrus=costPapyrus;
		this.costCoin=costCoin;
		this.type=type;
		this.rtype=rtype;
		this.resourceValue=resourceValue;
		this.dependency=dependency;
	}

	public String getDescription() {
		String s="Name: "+name+"\nCost: ";
		if (dependency!=null) s+=dependency+" / ";
		if (costClay>0) s+=costClay+" CLAY ";
		if (costOre>0) s+=costOre+" ORE ";
		if (costStone>0) s+=costStone+" STONE ";
		if (costWood>0) s+=costWood+" WOOD ";
		if (costLoom>0) s+=costLoom+" LOOM ";
		if (costGlass>0) s+=costGlass+" GLASS ";
		if (costPapyrus>0) s+=costPapyrus+" PAPYRUS ";
		if (costCoin>0) s+=costCoin+" COIN";
		s+="\nProvides: ";
		if (rtype==ResourceType.COIN) s+=resourceValue+" COIN";
		else if (rtype==ResourceType.VICTORY) s+=resourceValue+" VICTORY";
		else if (rtype==ResourceType.SHIELD) s+=resourceValue+" SHIELD";
		else if (rtype==ResourceType.WOOD) s+=resourceValue+" WOOD";
		else if (rtype==ResourceType.CLAY) s+=resourceValue+" CLAY";
		else if (rtype==ResourceType.ORE) s+=resourceValue+" ORE";
		else if (rtype==ResourceType.STONE) s+=resourceValue+" STONE";
		else if (rtype==ResourceType.GLASS) s+="GLASS";
		else if (rtype==ResourceType.LOOM) s+="LOOM";
		else if (rtype==ResourceType.PAPYRUS) s+="PAPYRUS";
		else if (rtype==ResourceType.CLAYORE) s+="CLAY/ORE";
		else if (rtype==ResourceType.CLAYSTONE) s+="CLAY/STONE";
		else if (rtype==ResourceType.CLAYWOOD) s+="CLAY/WOOD";
		else if (rtype==ResourceType.ORESTONE) s+="ORE/STONE";
		else if (rtype==ResourceType.OREWOOD) s+="ORE/WOOD";
		else if (rtype==ResourceType.STONEWOOD) s+="STONE/WOOD";
		else if (rtype==ResourceType.SCIENCE) {
			if (resourceValue==ScienceType.GEAR.ordinal()) s+="GEAR";
			else if (resourceValue==ScienceType.COMPASS.ordinal()) s+="COMPASS";
			else if (resourceValue==ScienceType.TABLET.ordinal()) s+="TABLET";
		} else if (name=="MARKETPLACE") s+="1 COIN for GLASS/LOOM/PAPYRUS";
		else if (name=="WEST TRADING POST") s+="1 COIN for CLAY/ORE/STONE/WOOD (left)";
		else if (name=="EAST TRADING POST") s+="1 COIN for CLAY/ORE/STONE/WOOD (right)";
		else if (name=="CARAVANSERY") s+="CLAY/ORE/STONE/WOOD";
		else if (name=="FORUM") s+="GLASS/LOOM/PAPYRUS";
		else if (name=="VINEYARD") s+="1 COIN/BROWN CARD";
		else if (name=="BAZAR") s+="2 COIN/GRAY CARD";
		else if (name=="HAVEN") s+="1 COIN 1 VICTORY/BROWN CARD";
		else if (name=="LIGHTHOUSE") s+="1 COIN 1 VICTORY/YELLOW CARD";
		else if (name=="CHAMBER OF COMMERCE") s+="2 COIN 2 VICTORY/GRAY CARD";
		else if (name=="ARENA") s+="3 COIN 1 VICTORY/WONDER STAGE";
		if (dependencyMap.containsKey(name)) {
			s+="\nUpgrades to: ";
			for (String ss:dependencyMap.get(name)) s+=ss+",";
		}
		return s;
	}


}
