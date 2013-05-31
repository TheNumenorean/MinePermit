package net.lotrcraft.minepermit.world;

import java.util.TreeMap;

import org.bukkit.World;

public class PermitWorldManager {
	
	private TreeMap<String, PermitWorld> worlds;
	
	public PermitWorldManager(){
		worlds = new TreeMap<String, PermitWorld>();
	}
	
	public void addPermitWorld(PermitWorld pw){
		worlds.put(pw.getWorld().getName(), pw);
	}
	
	public PermitWorld getPermitWorld(String name){
		return worlds.get(name);
	}
	
	public PermitWorld getPermitWorld(World w) {
		return getPermitWorld(w.getName());
	}

	/**
	 * @return the worlds
	 */
	public TreeMap<String, PermitWorld> getWorlds() {
		return worlds;
	}

}
