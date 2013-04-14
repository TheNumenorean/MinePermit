package net.lotrcraft.minepermit.miner;

import java.util.ArrayList;

import net.lotrcraft.minepermit.MinePermit;

import org.bukkit.Location;

public class MinerManager {
	
	private ArrayList<Miner> miners;
	private MinePermit minepermmit;
	
	public MinerManager(MinePermit m){
		this.minepermmit = m;
		
		miners = new ArrayList<Miner>();
	}
	
	public Miner getMiner(String name){
		for(Miner m : miners){
			if(m.getName().equals(name))
				return m;
		}
		
		return null;
	}
	
	public Permit getPermit(Location l){
		for(Miner m : miners){
			Permit p = m.getPermit(l);
			if(p != null) return p;
		}
		
		return null;
	}
}
