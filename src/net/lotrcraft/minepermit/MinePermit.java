package net.lotrcraft.minepermit;

import java.io.IOException;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
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

		this.saveResource("config.yml", false);

		this.getServer().getPluginManager()
				.registerEvents(new BlockListener(), this);

		loadConf();
		if(Config.useEconomyPlugin){
			if(loadVaultEcon())
				log.info("[MinePermit] Loaded Vault!");
			else{
				log.warning("[MinePermit] Unnable to load Vault! Reverting to original payment method.");
				Config.useEconomyPlugin = false;
			}
			
		}
		getCommand("permit").setExecutor(new CommandInterpreter());

		log.info("MinePermit Enabled");

	}

	private void loadConf() {

		if (!Config.pluginFolder.exists())
			Config.pluginFolder.mkdir();

		/*
		 * if(!Config.conf.exists()){ try { Config.conf.createNewFile(); } catch
		 * (IOException e) {
		 * log.warning("[MinePermit] Cannot create conf file!"); return; } }
		 */

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
