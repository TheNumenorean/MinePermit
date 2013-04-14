package net.lotrcraft.minepermit;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class MinePermit extends JavaPlugin {

	private Logger log;
	
	
	@Override
	public void onDisable(){
	}
	
	@Override
	public void onEnable(){
		log = this.getLogger();
		log.log(null, "Enabling MinePermit version ");
		
		this.getServer().getPluginManager().registerEvents(listener, this);
	}
}
