package net.lotrcraft.minepermit.miner;

import java.util.ArrayList;

import org.bukkit.Location;

public class Miner{
	
	public Miner(String name){
		
		this.name = name;
		permits = new ArrayList<Permit>();
		
	}
	
	private ArrayList<Permit> permits;
	private String name;
	
	public Permit getPermit(Location l)
	{
		for(Permit p : permits){
			if(p.contains(l))
				return p;
		}	
			return null;
	}
	
	public String getName(){
		return name;
	}
	
	public void addPermit(Permit p){
		
		permits.add(p);
		p.setOwner(this);
		
	}
}
