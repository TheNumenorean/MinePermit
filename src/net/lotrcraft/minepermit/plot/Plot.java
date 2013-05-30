package net.lotrcraft.minepermit.plot;

import org.bukkit.Location;

public class Plot {

	private Location location1;
	private Location location2;
	private String owner;

	public Plot(Location location1, Location location2) {
		
		if(!location1.getWorld().equals(location2.getWorld()))
			throw new IllegalArgumentException("Must be in same world!");
		
		this.setLocation1(location1);
		this.setLocation2(location2);
	}
	
	public boolean intersects(Plot p, int spacing){
		
		return !contains(p, spacing) && !p.contains(this, spacing);
	}
	
	/**
	 * Only checks if the Plot is contained at all in this plot.
	 * @param p Plot to check locations of.
	 * @param spacing Optional spacing to include outside this plot's bounds.
	 * @return true if p1 is contained in p2, false otherwise.
	 */
	public boolean contains(Plot p, int spacing){
		
		int higherX = Math.max(p.location1.getBlockX(), p.location2.getBlockX()) + spacing;
		int lowerX = Math.min(p.location1.getBlockX(), p.location2.getBlockX()) - spacing;
		
		int higherZ = Math.max(p.location1.getBlockZ(), p.location2.getBlockZ()) + spacing;
		int lowerZ = Math.min(p.location1.getBlockZ(), p.location2.getBlockZ()) - spacing;
		
		Location l1 = new Location(p.location1.getWorld(), higherX, 0, higherZ);
		Location l2 = new Location(p.location1.getWorld(), higherX, 0, lowerZ);
		Location l3 = new Location(p.location1.getWorld(), lowerX, 0, higherZ);
		Location l4 = new Location(p.location1.getWorld(), lowerX, 0, lowerZ);
		
		return !(contains(l1, spacing) || contains(l2, spacing) || contains(l3, spacing) || contains(l4, spacing));
	}
	
	public boolean contains(Location l){
		return contains(l, 0);
	}
	
	public boolean contains(Location l, int spacing){
		
		if(!location1.getWorld().equals(l.getWorld()))
			throw new IllegalArgumentException("Not in the same world!");
		
		int higherX = Math.max(location1.getBlockX(), location2.getBlockX()) + spacing;
		int lowerX = Math.min(location1.getBlockX(), location2.getBlockX()) - spacing;
		
		int higherZ = Math.max(location1.getBlockZ(), location2.getBlockZ()) + spacing;
		int lowerZ = Math.min(location1.getBlockZ(), location2.getBlockZ()) - spacing;
		
		if(l.getBlockX() > lowerX || l.getBlockX() < higherX && l.getBlockZ() > lowerZ || l.getBlockZ() < higherZ)
			return false;
		
		return true;
	}

	/**
	 * @return the location1
	 */
	public Location getLocation1() {
		return location1;
	}

	/**
	 * @param location1 the location1 to set
	 */
	public void setLocation1(Location location1) {
		this.location1 = location1;
	}

	/**
	 * @return the location2
	 */
	public Location getLocation2() {
		return location2;
	}

	/**
	 * @param location2 the location2 to set
	 */
	public void setLocation2(Location location2) {
		this.location2 = location2;
	}

	/**
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

}
