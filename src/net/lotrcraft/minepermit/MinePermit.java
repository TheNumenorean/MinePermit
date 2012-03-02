package net.lotrcraft.minepermit;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class MinePermit extends JavaPlugin {

	static Logger log = Logger.getLogger("minecraft");
	public static Economy econ;
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

		//Register the blockListener
		this.getServer().getPluginManager()
				.registerEvents(new BlockListener(), this);
		
		//load settings from conf
		loadConf();
		
		//Load economy plugins
		if (Config.useEconomyPlugin) {
			if (loadVaultEcon())
				log.info("[MinePermit] Loaded Vault!");
			else {
				log.warning("[MinePermit] Unnable to load Vault! Reverting to original payment method.");
				Config.useEconomyPlugin = false;
			}
		}
		
		//Set the command executer
		getCommand("permit").setExecutor(new CommandInterpreter());

		log.info("MinePermit Enabled");

	}

	private void loadConf() {
		//If there is no conf save the default
		if(!Config.conf.exists())
			this.saveDefaultConfig();
		
		if(!Config.pluginFolder.exists())
			Config.pluginFolder.mkdirs();

		try {
			Config.load(this.getConfig());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}

	}

	private boolean loadVaultEcon() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer()
				.getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

}
