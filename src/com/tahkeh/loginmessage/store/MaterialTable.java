package com.tahkeh.loginmessage.store;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import de.xzise.XLogger;

public class MaterialTable extends PropertiesFile {

	public static String[] blockValues = { "0", "1", "2", "3", "4", "5;0",
			"5;1", "5;2", "5;3", "6;0", "6;1", "6;2", "6;3", "7", "8", "9",
			"10", "11", "12", "13", "14", "15", "16", "17;0", "17;1", "17;2",
			"17;3", "18;0", "18;1", "18;2", "18;3", "19", "20", "21", "22",
			"23", "24;0", "24;1", "24;2", "25", "26", "27", "28", "29", "30",
			"31;0", "31;1", "31;2", "32", "33", "34", "35;0", "35;1", "35;2",
			"35;3", "35;4", "35;5", "35;6", "35;7", "35;8", "35;9", "35;10",
			"35;11", "35;12", "35;13", "35;14", "35;15", "36", "37", "38",
			"39", "40", "41", "42", "43;0", "43;1", "43;2", "43;3", "43;4",
			"43;5", "43;6", "44;0", "44;1", "44;2", "44;3", "44;4", "44;5",
			"44;6", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54",
			"55", "56", "57", "58", "59", "60", "61", "62", "63", "64", "65",
			"66", "67", "68", "69", "70", "71", "72", "73", "74", "75", "76",
			"77", "78", "79", "80", "81", "82", "83", "84", "85", "86", "87",
			"88", "89", "90", "91", "92", "93", "94", "95", "96", "97;0",
			"97;1", "97;2", "98;0", "98;1", "98;2", "98;3", "99", "100", "101",
			"102", "103", "104", "105", "106", "107", "108", "109", "110",
			"111", "112", "113", "114", "115", "116", "117", "118", "119",
			"120", "121", "122", "123", "124", "125;0", "125;1", "125;2",
			"125;3", "126;0", "126;1", "126;2", "126;3", "127", "128", "129",
			"130", "131", "132", "133", "134", "135", "136" };
	public static String[] blockNames = { "nothing", "stone", "grass", "dirt",
			"cobblestone", "oak planks", "pine planks", "birch planks",
			"jungle wood planks", "an oak sapling", "a pine sapling",
			"a birch sapling", "a jungle tree sapling", "bedrock", "water",
			"water", "lava", "lava", "sand", "gravel", "gold ore", "iron ore",
			"coal ore", "a log", "a pine log", "a birch log",
			"a jungle tree log", "oak leaves", "pine leaves", "birch leaves",
			"jungle tree leaves", "a sponge", "glass", "lapis lazuli ore",
			"a lapis lazuli block", "a dispenser", "sandstone",
			"hieroglyph sandstone", "smooth sandstone", "a note block",
			"a bed", "a powered rail", "a detector rail", "a sticky piston",
			"a web", "a dead shrub", "tall grass", "a shrub", "a dead shrub",
			"a piston", "a piston", "white wool", "orange wool",
			"magenta wool", "light blue wool", "yellow wool",
			"light green wool", "pink wool", "dark gray wool",
			"light gray wool", "cyan wool", "purple wool", "blue wool",
			"brown wool", "green wool", "red wool", "black wool",
			"a piston block", "a dandelion", "a rose", "a brown mushroom",
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
			"a sign", "a lever", "a stone pressure plate", "an iron door",
			"a wood pressure plate", "redstone ore", "redstone ore",
			"a redstone torch", "a redstone torch", "a button", "snow", "ice",
			"a snow block", "cacti", "clay", "sugar cane", "a jukebox",
			"a wood fence", "a pumpkin", "netherrack", "soulsand", "glowstone",
			"a portal", "a jack-o-lantern", "cake", "double stone slabs",
			"double stone slabs", "a locked chest", "a trapdoor",
			"a stone trap block", "a cobblestone trap block",
			"a stone brick trap block", "stone brick", "moss stone brick",
			"cracked stone brick", "circle stone brick", "mushrooms",
			"mushrooms", "iron bars", "a glass pane", "a melon", "a stem",
			"a stem", "vines", "a fence gate", "a brick stair",
			"a stone brick stair", "mycelium", "a lily pad", "nether brick",
			"a nether brick fence", "a nether brick stair", "nether warts",
			"an enchantment table", "a brewing stand", "a cauldron",
			"an end portal", "an end portal frame", "end stone",
			"a dragon egg", "a redstone lamp", "a redstone lamp", "oak planks",
			"pine planks", "birch planks", "jungle wood planks", "an oak slab",
			"a pine slab", "a birch slab", "a jungle wood slab", "cocoa plant",
			"sandstone stairs", "emerald ore", "an ender chest",
			"a tripwire hook", "string", "an emerald block", "pine stairs",
			"birch stairs", "jungle wood stairs" };
	public static String[] itemValues = { "256", "257", "258", "259", "260",
			"261", "262", "263;0", "263;1", "264", "265", "266", "267", "268",
			"269", "270", "271", "272", "273", "274", "275", "276", "277",
			"278", "279", "280", "281", "282", "283", "284", "285", "286",
			"287", "288", "289", "290", "291", "292", "293", "294", "295",
			"296", "297", "298", "299", "300", "301", "302", "303", "304",
			"305", "306", "307", "308", "309", "310", "311", "312", "313",
			"314", "315", "316", "317", "318", "319", "320", "321", "322",
			"323", "324", "325", "326", "327", "328", "329", "330", "331",
			"332", "333", "334", "335", "336", "337", "338", "339", "340",
			"341", "342", "343", "344", "345", "346", "347", "348", "349",
			"350", "351;0", "351;1", "351;2", "351;3", "351;4", "351;5",
			"351;6", "351;7", "351;8", "351;9", "351;10", "351;11", "351;12",
			"351;13", "351;14", "351;15", "352", "353", "354", "355", "356",
			"357", "358", "359", "360", "361", "362", "363", "364", "365",
			"366", "367", "368", "369", "370", "371", "372", "373", "374",
			"375", "376", "377", "378", "379", "380", "381", "382", "383;50",
			"383;51", "383;52", "383;54", "383;55", "383;56", "383;57",
			"383;58", "383;59", "383;60", "383;61", "383;62", "383;90",
			"383;91", "383;92", "383;93", "383;94", "383;95", "383;96",
			"383;98", "383;120", "384", "385", "386", "387", "388", "2256",
			"2257", "2258", "2259", "2260", "2261", "2262", "2263", "2264",
			"2265", "2266" };
	public static String[] itemNames = { "an iron shovel", "an iron pickaxe",
			"an iron axe", "flint and steel", "an apple", "a bow", "an arrow",
			"coal", "charcoal", "a diamond", "an iron ingot", "a gold ingot",
			"an iron sword", "a wood sword", "a wood shovel", "a wood pickaxe",
			"a wood axe", "a stone sword", "a stone shovel", "a stone pickaxe",
			"a stone axe", "a diamond sword", "a diamond shovel",
			"a diamond pickaxe", "a diamond axe", "a stick", "a bowl",
			"mushroom soup", "a gold sword", "a gold shovel", "a gold pickaxe",
			"a gold axe", "string", "a feather", "gunpowder", "a wood hoe",
			"a stone hoe", "an iron hoe", "a diamond hoe", "a gold hoe",
			"wheat seeds", "wheat", "bread", "a leather helmet",
			"a leather chestplate", "leather leggings", "leather boots",
			"a chainmail helmet", "a chainmail chestplate",
			"chainmail leggings", "chainmail boots", "an iron helmet",
			"an iron chestplate", "iron leggings", "iron boots",
			"a diamond helmet", "a diamond chestplate", "diamond leggings",
			"diamond boots", "a gold helmet", "a gold chestplate",
			"gold leggings", "gold boots", "flint", "raw porkchop",
			"cooked porkchop", "a painting", "a golden apple", "a sign",
			"a wooden door", "an empty bucket", "a water bucket",
			"a lava bucket", "a minecart", "a saddle", "an iron door",
			"redstone dust", "a snowball", "a boat", "leather",
			"a milk bucket", "a brick ingot", "a clay ball", "sugar cane",
			"paper", "a book", "a slimeball", "a storage minecart",
			"a powered minecart", "an egg", "a compass", "a fishing rod",
			"a watch", "glowstone dust", "raw fish", "cooked fish",
			"an ink sac", "rose dye", "cacti dye", "cocoa beans",
			"lapis lazuli", "purple dye", "cyan dye", "light gray dye",
			"dark gray dye", "pink dye", "light green dye", "dandelion dye",
			"light blue dye", "magenta dye", "orange dye", "bonemeal",
			"a bone", "sugar", "a cake", "a bed", "a redstone repeater",
			"a cookie", "a map", "shears", "a watermelon slice",
			"pumpkin seeds", "watermelon seeds", "raw steak", "cooked steak",
			"raw chicken", "cooked chicken", "rotten flesh", "an ender pearl",
			"a blaze rod", "a ghast tear", "a gold nugget", "a nether wart",
			"a potion", "a bottle", "a spider eye", "a fermented spider eye",
			"blaze powder", "magma cream", "a brewing stand", "a cauldron",
			"an eye of ender", "a glistering melon", "a creeper spawn egg",
			"a skeleton spawn egg", "a spider spawn egg", "a zombie spawn egg",
			"a slime spawn egg", "a ghast spawn egg",
			"a zombie pigman spawn egg", "an enderman spawn egg",
			"a cave spider spawn egg", "a silverfish spawn egg",
			"a blaze spawn egg", "a magma cube spawn egg", "a pig spawn egg",
			"a sheep spawn egg", "a cow spawn egg", "a chicken spawn egg",
			"a squid spawn egg", "a wolf spawn egg", "a mooshroom spawn egg",
			"an ocelot spawn egg", "a villager spawn egg",
			"a bottle of enchanting", "a flame charge", "a book and quill",
			"a signed book", "an emerald", "a 13 record", "a Cat record",
			"a blocks record", "a chirp record", "a far record",
			"a mall record", "a mellohi record", "a stal record",
			"a strad record", "a ward record", "an 11 record" };

	public MaterialTable(File file, XLogger logger) {
		super(file, logger);
	}

	private static List<String> getBlockValueList() {
		List<String> blockValueList = new ArrayList<String>();
		for (String blockID : blockValues) {
			blockValueList.add(blockID);
		}
		return blockValueList;
	}

	private static List<String> getItemValueList() {
		List<String> itemValueList = new ArrayList<String>();
		for (String itemID : itemValues) {
			itemValueList.add(itemID);
		}
		return itemValueList;
	}

	public static void initialWrite(File file, XLogger log) {
		try {
			BufferedWriter o = new BufferedWriter(new FileWriter(file));
			List<String> blockValueList = getBlockValueList();
			List<String> itemValueList = getItemValueList();
			int blockIndex = 0;
			int itemIndex = 0;

			for (String blockName : blockNames) {
				o.write(blockValueList.get(blockIndex) + "=" + blockName + "\n");
				blockIndex = blockIndex + 1;
			}

			for (String itemName : itemNames) {
				o.write(itemValueList.get(itemIndex) + "=" + itemName + "\n");
				itemIndex = itemIndex + 1;
			}
			o.flush();
			o.close();
		} catch (IOException e) {
			log.severe("Error writing to items.txt file.");
		}
	}

	public String getMaterialName(int id, byte data) {
		String item = Integer.toString(id);
		String name = "a material";
		String lookup = (item)
				+ (containsKey(item) ? "" : ";" + Byte.toString(data));
		if (!getString(lookup, "").equals("")) {
			name = getString(lookup);
		}

		return name;
	}
}
