package net.lotrcraft.minepermit;

import java.io.IOException;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class MinePermit extends JavaPlugin {

	static Logger log = Logger.getLogger("minecraft");
	public static Economy econ;
	FileConfiguration conf;
	public static Permission perm;

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
				log.info("[MinePermit] Loaded Vault Economy!");
			else {
				log.warning("[MinePermit] Unnable to load Vault! Reverting to original payment method.");
				Config.useEconomyPlugin = false;
			}
		}
		
		//Load permissions plugins
		if (Config.useVaultPermissions) {
			if (loadVaultPerm())
				log.info("[MinePermit] Loaded Vault Permissions!");
			else {
				log.warning("[MinePermit] Unnable to load Vault! Reverting to original permissions method.");
				Config.useVaultPermissions = false;
			}
		}
		//Set the command executer
		getCommand("permit").setExecutor(new CommandInterpreter());

		log.info("MinePermit Enabled");

	}


	private void loadConf() {
		
		if(!Config.pluginFolder.exists())
			Config.pluginFolder.mkdirs();
		
		
		//If there is no conf save the default
		if(!Config.conf.exists()){
			saveDefaultConfig();
		}


		
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
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

	private boolean loadVaultPerm() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
		if (rsp == null) {
			return false;
		}
		perm = rsp.getProvider();
		return perm != null;
	}
}
