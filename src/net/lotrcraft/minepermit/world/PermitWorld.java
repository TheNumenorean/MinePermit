package net.lotrcraft.minepermit.world;

import java.util.ArrayList;

import net.lotrcraft.minepermit.MinePermit;
import net.lotrcraft.minepermit.plot.Plot;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

public class PermitWorld {
	
	private int maxPlotSize, minPlotSize, uncalculatedCostPerBlock, maxPlots, spacing;
	private boolean calculatePlotCost;
	private BlockPriceDefinition plotBlockPrices;
	private World world;
	private ArrayList<Plot> plots;

	public PermitWorld(World world, int maxPlotSize, int minPlotSize, boolean calculatePlotCost, int uncalculatedCostPerBlock, int maxPlots, int spacing, BlockPriceDefinition bpd) {
		this.maxPlotSize = maxPlotSize;
		this.minPlotSize = minPlotSize;
		this.calculatePlotCost = calculatePlotCost;
		this.uncalculatedCostPerBlock = uncalculatedCostPerBlock;
		this.maxPlots = maxPlots;
		plotBlockPrices = bpd;
		this.world =  world;
		this.spacing = spacing;
		
		plots = new ArrayList<Plot>();
	}
	
	/**
	 * Creates a new plot spanninig the coordinates in this world. Returns null if it is too small or large 
	 * or it intersects another plot or goes within mandatory spacing. Note that meerly getting the plot does not mean that it 
	 * is confrmed added to this world; in order for it to be confirmed it must be passed to registerPlot().
	 * @param x1 First x coord
	 * @param z1 First z coord
	 * @param x2 Second x coord
	 * @param z2 Second z coord
	 * @return The generated Plot or Null.
	 */
	public Plot getNewPlot(int x1, int z1, int x2, int z2){
		
		Plot plot = new Plot(new Location(world, x1, 0, z1), new Location(world, x2, 0, z2), null, null, this);
		
		if(!checkValidPlot(plot))
			return null;
		
		return plot;
	}
	
	public boolean registerPlot(Plot p){
		
		if(!checkValidPlot(p))
			return false;
		
		plots.add(p);
		
		return true;
	}
	
	private boolean checkValidPlot(Plot p){
		
		if(Math.abs(p.getLocation1().getBlockX() - p.getLocation2().getBlockX()) < minPlotSize || Math.abs(p.getLocation1().getBlockX() - p.getLocation2().getBlockX()) > maxPlotSize || Math.abs(p.getLocation1().getBlockZ() - p.getLocation2().getBlockZ()) < minPlotSize || Math.abs(p.getLocation1().getBlockZ() - p.getLocation2().getBlockZ()) > maxPlotSize)
			return false;
		

		
		for(Plot plot : plots){
			if(p.getLocation1().getBlockX() - p.getLocation2().getBlockX() < 2 * maxPlotSize + spacing &&
					p.getLocation1().getBlockZ() - p.getLocation2().getBlockZ() < 2 * maxPlotSize + spacing)
				if(p.intersects(plot, spacing))
					return false;
		}
		
		return true;
	}
	

	public void save(ConfigurationSection cs) {
		cs.set("max_plot_size", maxPlotSize);
		cs.set("min_plot_size", minPlotSize);
		cs.set("max_plots_per_player", maxPlots);
		cs.set("plot_spacing", spacing);
		cs.set("calculate_plot_cost", calculatePlotCost);
		cs.set("un_calculated_cost_per_block", uncalculatedCostPerBlock);
		
		plotBlockPrices.save(cs.getConfigurationSection("plot_block_prices"));
		
	}
	
	public Plot getContainingPlot(Location location) {
		for(Plot p : plots)
			if(p.contains(location))
				return p;
		return null;
	}

	public static PermitWorld getNewPermitWorld(ConfigurationSection cs, World world) {
		cs.addDefault("max_plot_size", 60);
		cs.addDefault("min_plot_size", 10);
		cs.addDefault("max_plots_per_player", 5);
		cs.addDefault("plot_spacing", 3);
		cs.addDefault("calculate_plot_cost", true);
		cs.addDefault("un_calculated_cost_per_block", 5);
		
		ConfigurationSection blocks;
		if((blocks = cs.getConfigurationSection("plot_block_prices")) == null)
			blocks = cs.createSection("plot_block_prices");
		
		BlockPriceDefinition bpd = BlockPriceDefinition.getNewDefinition(blocks);
		
		PermitWorld pw = new PermitWorld(world, cs.getInt("max_plot_size"), 
				cs.getInt("min_plot_size"), 
				cs.getBoolean("calculate_plot_cost"), 
				cs.getInt("un_calculated_cost_per_block"),
				cs.getInt("max_plots_per_player"),
				cs.getInt("plot_spacing"), bpd);
		
		return pw;
	}
	
	//SETTERS AND GETTERS

	/**
	 * @return the maxPlotSize
	 */
	public int getMaxPlotSize() {
		return maxPlotSize;
	}

	/**
	 * @param maxPlotSize the maxPlotSize to set
	 */
	public void setMaxPlotSize(int maxPlotSize) {
		this.maxPlotSize = maxPlotSize;
	}

	/**
	 * @return the minPlotSize
	 */
	public int getMinPlotSize() {
		return minPlotSize;
	}

	/**
	 * @param minPlotSize the minPlotSize to set
	 */
	public void setMinPlotSize(int minPlotSize) {
		this.minPlotSize = minPlotSize;
	}

	/**
	 * @return the uncalculatedCostPerBlock
	 */
	public int getUncalculatedCostPerBlock() {
		return uncalculatedCostPerBlock;
	}

	/**
	 * @param uncalculatedCostPerBlock the uncalculatedCostPerBlock to set
	 */
	public void setUncalculatedCostPerBlock(int uncalculatedCostPerBlock) {
		this.uncalculatedCostPerBlock = uncalculatedCostPerBlock;
	}

	/**
	 * @return Whether this world's plots should have their cost calculated from ore costs or should use a flat fee.
	 */
	public boolean isPlotCostCalculated() {
		return calculatePlotCost;
	}

	/**
	 * @param calculatePlotCost the calculatePlotCost to set
	 */
	public void setCalculatePlotCost(boolean calculatePlotCost) {
		this.calculatePlotCost = calculatePlotCost;
	}

	/**
	 * @return the blockPrices
	 */
	public BlockPriceDefinition getPlotBlockPrices() {
		return plotBlockPrices;
	}

	/**
	 * @return the maxPlots
	 */
	public int getMaxPlots() {
		return maxPlots;
	}

	/**
	 * @param maxPlots the maxPlots to set
	 */
	public void setMaxPlots(int maxPlots) {
		this.maxPlots = maxPlots;
	}

	/**
	 * @return the world
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * @return the minBlocksBetween
	 */
	public int getSpacing() {
		return spacing;
	}

	/**
	 * @param minBlocksBetween the minBlocksBetween to set
	 */
	public void setSpacing(int spacing) {
		this.spacing = spacing;
	}

	public ArrayList<Plot> getPlots() {
		return plots;
	}


}
