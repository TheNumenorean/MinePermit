package net.lotrcraft.minepermit;

import java.io.File;
import java.util.logging.Logger;

import net.lotrcraft.config.Configuration;

import org.bukkit.configuration.file.FileConfiguration;
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
		Config.load(new Configuration(new File(Config.pluginFolder.getPath() + File.separator + "config.yml")));
		
	}

}
