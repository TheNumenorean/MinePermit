package net.lotrcraft.minepermit;

import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.java.JavaPlugin;

public class MinePermit extends JavaPlugin {
	
	static Logger log = Logger.getLogger("minecraft");
	FileConfiguration conf;
	

	@Override
	public void onDisable() {
		
		log.info("[MinePermit] Saving players...");
		MinerManager.savePlayers();
		log.info("[MinePermit] Players saved!");
		log.info("MinePermit Disabled");

	}

	@Override
	public void onEnable() {
		
		this.getServer().getPluginManager().registerEvent(Type.BLOCK_BREAK, new Listener(), Priority.Normal, this);
		
		loadConf();
		getCommand("permit").setExecutor(new CommandInterpreter());
		
		log.info("MinePermit Enabled");

	}

	private void loadConf() {
		
		if(!Config.pluginFolder.exists())
			Config.pluginFolder.mkdir();
		
		if(!Config.conf.exists()){
			try {
				Config.conf.createNewFile();
			} catch (IOException e) {
				log.warning("[MinePermit] Cannot create conf file!");
				return;
			}
		}
		
		try {
			Config.load(this.getConfig());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
		
	}

}
