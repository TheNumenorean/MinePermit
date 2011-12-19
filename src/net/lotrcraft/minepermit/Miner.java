package net.lotrcraft.minepermit;

import java.util.Map;
import java.util.TreeMap;

public class Miner {
	
	private String player;
	private Map<Integer, Long> permits = new TreeMap<Integer, Long>();

	public Miner(String playerName){
		this.player = playerName;
	}
	
	public String getPlayer(){
		return player;
	}
	
	public boolean hasPermit(int blockID){
		if(permits.containsKey(blockID)){
			
			if(permits.get(blockID) - System.currentTimeMillis() <= 0){
				removePermit(blockID);
				return false;
			}
			
			return true;
		}
		return false;
	}
	
	public boolean addPermit(int blockID, long minutes){
		if (permits.containsKey(blockID))
			return false;
		
		permits.put(blockID, System.currentTimeMillis() + minutes * 60000);
		return true;
		
	}
	
	public Long getRemainingTime(int blockID){
		return (permits.get(blockID) - System.currentTimeMillis()) / 60000;
	}
	
	public Map<Integer, Long> getPermits(){
		return permits;
	}
	
	public void removePermit(int blockID){
		permits.remove(blockID);
	}

}
