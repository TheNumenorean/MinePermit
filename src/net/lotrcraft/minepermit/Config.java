package net.lotrcraft.minepermit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;

import net.lotrcraft.minepermit.languages.InvalidLanguageFileException;
import net.lotrcraft.minepermit.languages.TextManager;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {
	
	public static int universalCost;
	public static long permitDuration;
	public static int currencyBlockID;
	public static boolean useEconomyPlugin;
	public static boolean multiPermit;
	public static boolean useVaultPermissions;

	public static Logger log = Logger.getLogger("minecraft");
	private static Map<Integer, Integer> blocks = new TreeMap<Integer, Integer>();
	
	public static final File pluginFolder = new File("plugins" + File.separator + "MinePermit");
	public static final File conf = new File(pluginFolder.getPath() + File.separator + "config.yml");
	public static final File languageFolder = new File(pluginFolder.getPath() + File.separator + "languages");

	public static void load(FileConfiguration config) throws IOException, InvalidConfigurationException{
		
		universalCost = getInt("UniversalPermitCost", 300, config);
		permitDuration = getLong("DefaultPermitDuration", 60, config);
		currencyBlockID = getInt("currencyBlockID", 371, config);
		multiPermit = getBoolean("AllowMultiPurchase", true, config);
		useEconomyPlugin = getBoolean("UseVaultEconomy", false, config);
		useVaultPermissions = getBoolean("UseVaultPermissions", false, config);
		
		
		//Load language file
		String tmp = getString("LanguageFile", null, config);
		
		try{
			if(tmp != null)
				TextManager.loadTextFromFile(new File(languageFolder.getPath() + File.separator + tmp));
			log.info("[MinePermit] Language file succesfully loaded!");
		} catch (InvalidLanguageFileException e){
			log.warning("[MinePermit] Couldn't load language file! " + e.getMessage());
		}

		ConfigurationSection sect = config.getConfigurationSection("Blocks");
		Set<String> list;

		if (sect == null || (list = sect.getKeys(false)) == null || list.isEmpty()) {
			log.warning("[MinePermit] No Blocks detected!");
			if(sect == null)
				config.createSection("Blocks");

		} else {

			for (int counter = 0; counter < list.size(); counter++) {
				blocks.put(
						getInt(list.toArray()[counter] + ".id", -1, sect),
						getInt(list.toArray()[counter] + ".cost", 50, sect));

			}

		}

		config.options().copyHeader(false);
		//config.save(conf);

		File playerDataFolder = new File("plugins" + File.separator
				+ "MinePermit" + File.separator + "Players");

		MinePermit.log.info("[MinePermit] Loading Players...");

		if (!playerDataFolder.exists()) {
			playerDataFolder.mkdirs();
		} else {
			File[] playerFiles = playerDataFolder.listFiles();
			if (playerFiles == null)
				return;

			for (int counter = 0; counter < playerFiles.length; counter++) {

				if (!playerFiles[counter].getName().endsWith(".yml"))
					continue;

				log.info("[MinePermit] Loading conf for "
						+ playerFiles[counter].getName().substring(0,
								playerFiles[counter].getName().indexOf('.')));

				if (!playerFiles[counter].canRead()) {
					log.warning("[MinePermit] Can't read file!");
					continue;
				}

				Miner m = loadPlayerConf(playerFiles[counter]);
				MinerManager.addMiner(m);

				log.info("[MinePermit] " + m.getPlayer() + " loaded!");

			}
		}

		log.info("[MinePermit] Finished loading players.");

	}
	
	public static Map<Integer, Integer> getPermits(){
		return blocks;
	}

	public static Miner loadPlayerConf(File file) throws FileNotFoundException,
			IOException, InvalidConfigurationException {
		FileConfiguration config = new YamlConfiguration();
		config.load(file);
		String playerName = file.getName().substring(0,
				file.getName().indexOf('.'));

		Miner miner = new Miner(playerName);
		miner.addUniversalPermit(getInt("UniversalPermit", 0, config));
		ConfigurationSection sect = config.getConfigurationSection("Blocks");
		Set<String> list;

		if (sect == null || (list = sect.getKeys(false)) == null || list.isEmpty()) {
			//log.warning("[MinePermit] No Permits detected!");
			config.set("Blocks", null);
			config.save(file);

		} else {

			for (int counter = 0; counter < list.size(); counter++) {
				miner.addPermit(
						(getInt(list.toArray()[counter] + ".id", counter, sect)),
						getInt(list.toArray()[counter] + ".time", counter, sect));
			}
		}

		return miner;
	}

	public static void savePlayerConf(Miner m) {
		YamlConfiguration g = new YamlConfiguration();
		Map<Integer, Long> p = m.getPermits();
		File f = new File(pluginFolder.getPath() + File.separator + "Players"
				+ File.separator + m.getPlayer() + ".yml");

		try {
			if (!f.exists()) {
				f.createNewFile();
			}

			g.load(f);
			g.set("UniversalPermit", m.getRemainingUniversalTime());
			g.createSection("Blocks");

			for (int y = 0; y < p.size(); y++) {
				
				Object blckID = p.keySet().toArray()[y];

				g.set("Blocks.block" + blckID + ".id", blckID);
				g.set("Blocks.block" + blckID + ".time",
						m.getRemainingTime((Integer) blckID));

			}

			g.save(f);
		} catch (IOException io) {
			io.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	public static boolean isPermitRequired(int typeId) {
		return blocks.containsKey(typeId);
	}

	public static int getCost(int itemID) {
		return blocks.get(itemID);
	}

	// Functions for AutoUpdating the Config.yml
	public static Object getProperty(String path, Object def, ConfigurationSection config) {
		if (isNull(path, config))
			return setProperty(path, def, config);
		return config.get(path);
	}

	public static int getInt(String path, int def, ConfigurationSection sect) {
		if (isNull(path, sect))
			return (Integer) setProperty(path, def, sect);
		return sect.getInt(path, def);
	}
	
	public static long getLong(String path, long def, ConfigurationSection sect) {
		if (isNull(path, sect))
			return (Long) setProperty(path, def, sect);
		return sect.getLong(path, def);
	}
	
	public static double getDouble(String path, double def, ConfigurationSection sect) {
		if (isNull(path, sect))
			return (Double) setProperty(path, def, sect);
		return sect.getDouble(path, def);
	}

	public static boolean getBoolean(String path, Boolean def, ConfigurationSection config) {
		if (isNull(path, config))
			return (Boolean) setProperty(path, def, config);
		return config.getBoolean(path, def);
	}
	
	public static String getString(String path, String def, ConfigurationSection config) {
		if (isNull(path, config))
			return (String) setProperty(path, def, config);
		return config.getString(path, def);
	}

	private static Object setProperty(String path, Object val, ConfigurationSection sect) {
		sect.set(path, val);
		return val;
	}

	private static boolean isNull(String path, ConfigurationSection sect) {
		return sect.get(path) == null;
	}

}
