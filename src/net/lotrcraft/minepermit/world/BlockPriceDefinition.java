package net.lotrcraft.minepermit.world;

import java.util.TreeMap;

import org.bukkit.configuration.ConfigurationSection;

public class BlockPriceDefinition {
	
	TreeMap<Integer, Integer> blocks;
	
	public BlockPriceDefinition(){
		blocks = new TreeMap<Integer, Integer>();
	}
	
	public int getBlockPrice(int id){
		Integer i = blocks.get(id);
		return i == null? 0 : i;
	}
	
	public void setBlockPrice(int id, int cost){
		blocks.put(id, cost);
	}

	
	public static BlockPriceDefinition getNewDefinition(ConfigurationSection cs){
		BlockPriceDefinition b = new BlockPriceDefinition();
		
		cs.addDefault("21", 50);
		cs.addDefault("14", 30);
		cs.addDefault("15", 20);
		cs.addDefault("16", 10);
		cs.addDefault("56", 150);
		cs.addDefault("73", 80);
		cs.addDefault("129", 130);
		
		for(String s : cs.getKeys(false)){
			try{
				b.setBlockPrice(Integer.parseInt(s),cs.getInt(s));
				
			} catch (NumberFormatException e){
				cs.set(s, null);
			}
		}
		
		return b;
		
	}

	public void save(ConfigurationSection cs) {
		
		for(int i : blocks.keySet()){
			cs.set(i + "", blocks.get(i));
		}
		
	}
}
