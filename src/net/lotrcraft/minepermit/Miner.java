package net.lotrcraft.minepermit;

import java.util.Map;
import java.util.TreeMap;

public class Miner {
	
	private String player;
	private Map<Integer, Long> permits;
	private long univPermit;
	

	public Miner(String playerName){
		this.player = playerName;
		permits = new TreeMap<Integer, Long>();
	}
	
	public String getPlayer(){
		return player;
	}
	
	public boolean hasPermit(int blockID){
		if(permits.containsKey(blockID) && checkTime(blockID) || hasUniversalPermit())
			return true;
		
		return false;
	}
	
	public boolean addPermit(int blockID, long minutes){
		if (permits.containsKey(blockID)){
			permits.put(blockID, permits.get(blockID) + minutes * 60000L);
		} else
			permits.put(blockID, System.currentTimeMillis() + minutes * 60000L);
		return true;
		
	}
	
	public long getRemainingTime(int blockID){
		if(!hasPermit(blockID))
			return 0;
		return (permits.get(blockID) - System.currentTimeMillis()) / 60000L;
	}
	
	public Map<Integer, Long> getPermits(){
		return permits;
	}
	
	public void removePermit(int blockID){
		permits.remove(blockID);
	}
	
	public boolean checkTime(int id){
		if(permits.containsKey(id)){
			
			if(permits.get(id) - System.currentTimeMillis() <= 0){
				removePermit(id);
				return false;
			}
			
			return true;
		}
		return false;
	}
	
	public String toString(){
		return player;
	}

	public void addUniversalPermit(long permitDuration) {
		univPermit += System.currentTimeMillis() + permitDuration * 60000L;
		
	}

	public boolean hasUniversalPermit() {
		if(univPermit - System.currentTimeMillis() <= 0){
			univPermit = 0;
			return false;
		}
		
		return true;
	}

	public long getRemainingUniversalTime() {
		if(univPermit - System.currentTimeMillis() <= 0){
			univPermit = 0;
			return 0;
		}
		
		return (univPermit - System.currentTimeMillis()) / 60000L;
	}

}
