package net.lotrcraft.minepermit.world;

import org.bukkit.configuration.ConfigurationSection;

public class PermitWorld {
	
	private int maxPlotSize, minPlotSize, uncalculatedCostPerBlock;
	private boolean calculate_cost;
	private BlockPriceDefinition blockPrices;

	public PermitWorld() {
		// TODO Auto-generated constructor stub
	}

	public static PermitWorld getNewPermitWorld(ConfigurationSection cs, String name) {
		cs.addDefault("max_plot_size", 15);
		cs.addDefault("min_plot_size", 60);
		cs.addDefault("calculate_cost", true);
		cs.addDefault("un_calculated_cost_per_block", 5);
		
		ConfigurationSection blocks;
		if((blocks = cs.getConfigurationSection("blocks")) == null)
			blocks = cs.createSection("blocks");
		
		BlockPriceDefinition bpd = BlockPriceDefinition.getNewDefinition(blocks);
		
		new PermitWorld(cs.getInt("max_plot_size"), 
				cs.getInt("min_plot_size"), 
				cs.getBoolean("calculate_cost"), 
				cs.getInt("un_calculated_cost_per_block"));
		
		return null;
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

}
