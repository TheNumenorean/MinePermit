package net.lotrcraft.minepermit;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import net.lotrcraft.minepermit.world.PermitWorld;
import net.lotrcraft.minepermit.world.PermitWorldManager;

import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class MinePermit extends JavaPlugin {

	private Logger log;
	private FileConfiguration conf;
	private PermitWorldManager pwm;
	
	@Override
	public void onLoad(){
		log = this.getLogger();
		log.info("Loading " + this.getDescription().getName() + " version " + this.getDescription().getVersion());
		
		conf = this.getConfig();
		conf.options().copyDefaults(true);
		
		ConfigurationSection worlds = conf.getConfigurationSection("worlds");
		if(worlds == null)
			worlds = conf.createSection("worlds");
		
		pwm = new PermitWorldManager();
		
		for(String s : worlds.getKeys(false)){
			World w;
			if((w = this.getServer().getWorld(s)) == null)
				log.warning("Configuration values for world " + s + " can't be loaded because the world doesn't exist.");
			else
				pwm.addPermitWorld(PermitWorld.getNewPermitWorld(worlds.getConfigurationSection(s), w));
		}
		
		saveConf();
		
		log.info("Loaded!");
	}
	
	@Override
	public void onDisable(){
		for(String name : pwm.getWorlds().keySet()){
			pwm.getPermitWorld(name).save(conf.getConfigurationSection("worlds." + name));
		}
		
		saveConf();
	}
	
	private void saveConf(){
		try {
			conf.save(this.getDataFolder().getPath() + File.separator + "config.yml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onEnable(){
		
		log.info("Enabling " + this.getDescription().getName());
		
		this.getServer().getPluginManager().registerEvents(new WorldListener(), this);
	}

	/**
	 * @return the pwm
	 */
	public PermitWorldManager getPWM() {
		return pwm;
	}
}
