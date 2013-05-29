package net.lotrcraft.minepermit;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import net.lotrcraft.minepermit.world.PermitWorld;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class MinePermit extends JavaPlugin {

	private Logger log;
	private FileConfiguration conf;
	
	@Override
	public void onLoad(){
		log = this.getLogger();
		log.info("Loading " + this.getDescription().getName() + " version " + this.getDescription().getVersion());
		
		conf = this.getConfig();
		conf.options().copyDefaults(true);
		
		ConfigurationSection worlds = conf.getConfigurationSection("worlds");
		if(worlds == null)
			worlds = conf.createSection("worlds");
		
		for(String s : worlds.getKeys(false)){
			if(this.getServer().getWorld(s) == null)
				log.warning("Configuration values for world " + s + " can't be loaded because the world doesn't exist.");
			else
				PermitWorld.getNewPermitWorld(worlds.getConfigurationSection(s), s);
		}
		
		try {
			conf.save(this.getDataFolder().getPath() + File.separator + "config.yml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onDisable(){
	}
	
	@Override
	public void onEnable(){
		
		log.info("Enabling " + this.getDescription().getName());
		
		this.getServer().getPluginManager().registerEvents(new WorldListener(), this);
	}
}
