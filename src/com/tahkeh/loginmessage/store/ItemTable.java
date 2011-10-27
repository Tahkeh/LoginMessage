package com.tahkeh.loginmessage.store;

import java.util.ArrayList;
import java.util.List;

import com.tahkeh.loginmessage.Main;

public class ItemTable {
	private final Main plugin;
	private final PropertiesFile prop;

	String[] blockValues = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
			"11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21",
			"22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32",
			"33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43:0",
			"43:1", "43:2", "43:3", "43:4", "43:5", "43:6", "44:0", "44:1",
			"44:2", "44:3", "44:4", "44:5", "44:6", "45", "46", "47", "48",
			"49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59",
			"60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "70",
			"71", "72", "73", "74", "75", "76", "77", "78", "79", "80", "81",
			"82", "83", "84", "85", "86", "87", "88", "89", "90", "91", "92",
			"93", "94", "95", "96", "97", "98:0", "98:1", "98:2", "99", "100",
			"102", "103", "104", "105", "106", "107", "108", "109" };
	// Non-plural names should be prefixed with "a".
	// Think of "Player was killed with x" where x is the item name!
	String[] blockNames = { "stone", "grass", "dirt", "cobblestone", "planks",
			"a sapling", "bedrock", "water", "water", "lava", "lava", "sand",
			"gravel", "gold ore", "iron ore", "coal ore", "a log", "leaves",
			"a sponge", "glass", "lapis lazuli ore", "a lapis lazuli block",
			"a dispenser", "sandstone", "a note block", "a bed",
			"a powered rail", "a detector rail", "a sticky piston", "a web",
			"tall grass", "a dead shrub", "a piston", "a piston", "wool",
			"grass", "a dandelion", "a rose", "a brown mushroom",
			"a red mushroom", "a gold block", "an iron block",
			"double stone slabs", "sandstone", "planks", "cobblestone",
			"brick", "stone brick", "marble", "a stone slab",
			"a sandstone slab", "a plank slab", "a cobblestone slab",
			"a brick slab", "a stone brick slab", "a marble slab", "brick",
			"TNT", "a bookshelf", "moss cobblestone", "obsidian", "a torch",
			"fire", "a monster spawner", "a wooden stair", "a chest",
			"redstone wire", "diamond ore", "a diamond block", "a workbench",
			"seeds", "farmland", "a furnace", "a furnace", "a sign",
			"a wooden door", "a ladder", "a rail", "a cobblestone stair",
			"a lever", "a stone pressure plate", "an iron door",
			"a wood pressure plate", "redstone ore", "redstone ore",
			"a redstone torch", "a redstone torch", "a button", "snow", "ice",
			"a snow block", "cacti", "clay", "sugar cane", "a jukebox",
			"a wood fence", "a pumpkin", "netherrack", "soulsand", "glowstone",
			"a portal", "a jack-o-lantern", "cake", "double stone slabs",
			"double stone slabs", "a locked chest", "a trapdoor",
			"a silverfish block", "stone brick", "moss stone brick",
			"cracked stone brick", "mushrooms", "mushrooms", "iron bars",
			"a glass pane", "a melon", "a stem", "a stem", "vines",
			"a fence gate", "a brick stair", "a stone brick stair" };
	String[] itemValues = { "256", "257", "258", "259", "260", "261", "262",
			"263", "264", "265", "266", "267", "268", "269", "270", "271",
			"272", "273", "274", "275", "276", "277", "278", "279", "280",
			"281", "282", "283", "284", "285", "286", "287", "288", "289",
			"290", "291", "292", "293", "294", "295", "296", "297", "298",
			"299", "300", "301", "302", "303", "304", "305", "306", "307",
			"308", "309", "310", "311", "312", "313", "314", "315", "316",
			"317", "318", "319", "320", "321", "322", "323", "324", "325",
			"326", "327", "328", "329", "330", "331", "332", "333", "334",
			"335", "336", "337", "338", "339", "340", "341", "342", "343",
			"344", "345", "346", "347", "348", "349", "350", "351:0", "351:1",
			"351:2", "351:3", "351:4", "351:5", "351:6", "351:7", "351:8",
			"351:9", "351:10", "351:11", "351:12", "351:13", "351:14",
			"351:15", "352", "353", "354", "355", "356", "357", "358", "359",
			"360", "361", "362", "363", "364", "365", "366", "367", "368",
			"2256", "2257" };
	String[] itemNames = { "an iron shovel", "an iron pickaxe", "an iron axe",
			"flint and steel", "an apple", "a bow", "an arrow", "coal", "a diamond",
			"an iron ingot", "a gold ingot", "an iron sword", "a wood sword", "a wood shovel",
			"a wood pickaxe", "a wood axe", "a stone sword", "a stone shovel", "a stone pickaxe",
			"a stone axe", "a diamond sword", "a diamond shovel", "a diamond pickaxe", "a diamond axe",
			"a stick", "a bowl", "mushroom soup", "a gold sword", "a gold shovel", "a gold pickaxe",
			"a gold axe", "string", "a feather", "gunpowder", "a wood hoe"
	};

	public ItemTable(Main plugin, PropertiesFile prop) {
		this.plugin = plugin;
		this.prop = prop;
	}

	public void load() {
		List<String> blockValueList = new ArrayList<String>();
		List<String> itemValueList = new ArrayList<String>();
		int blockIndex = 0;
		int itemIndex = 0;

		for (String blockID : blockValues) {
			blockValueList.add(blockID);
		}

		for (String itemID : itemValues) {
			itemValueList.add(itemID);
		}

		for (String blockName : blockNames) {
			prop.getString(blockValueList.get(blockIndex), blockName);
			blockIndex = blockIndex + 1;
		}
	}
}
