package net.lotrcraft.minepermit;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import net.lotrcraft.config.Configuration;
import net.lotrcraft.config.ConfigurationNode;

public class Config {

	static int cost;
	static final File pluginFolder = new File("plugins" + File.separator + "MinePermit");
	static Logger log = Logger.getLogger("minecraft");
	private static Map<Integer, Integer> blocks = new TreeMap<Integer, Integer>();
	private static final File conf = new File(pluginFolder.getPath() + File.separator + "config.yml");

	public static void load(Configuration config) {
		
		if(!pluginFolder.exists())
			pluginFolder.mkdir();
		
		if(!conf.exists()){
			try {
				conf.createNewFile();
			} catch (IOException e) {
				log.warning("[MinePermit] Cannot create conf file!");
			}
		}
		
		config.load();

		Map<String, ConfigurationNode> list = config.getNodes("Blocks");

		if (list == null || list.isEmpty()) {
			log.warning("[MinePermit] No Blocks detected!");
			config.setProperty("Blocks", null);
			
			
			
			//config.setProperty("Blocks.block.id", 2);
			//config.setProperty("Blocks.block.time", 3);
			
			blocks.put(3, 3);
		
		} else {

			for (int counter = 0; counter < list.size(); counter++) {
				blocks.put(getInt("Blocks." + list.keySet().toArray()[counter] + ".id", counter, config), 
						getInt("Blocks." + list.keySet().toArray()[counter] + ".cost", 50, config));

			}
			


		}
		
		config.save();

		File playerDataFolder = new File("plugins" + File.separator + "MinePermit" + File.separator + "Players");

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
					log.warning("Can't read file!");
					continue;
				}

				Miner m = loadPlayerConf(playerFiles[counter]);

				log.info("[MinePermit] " + m.getPlayer() + " loaded!");

			}
		}

		log.info("[MinePermit] Finished loading players.");

	}

	public static Miner loadPlayerConf(File file) {
		Configuration config = new Configuration(file);
		String playerName = file.getName().substring(0,
				file.getName().indexOf('.'));

		config.load();

		Miner miner = new Miner(playerName);

		List<ConfigurationNode> list = config.getNodeList("Blocks", null);

		if (list == null) {
			
		} else {

			for (int counter = 0; counter < list.size(); counter++) {
				miner.addPermit((getInt("id", counter, list.get(counter))), getInt("time", counter, list.get(counter)) * 60000L);
			}
		}

		return miner;
	}

	public static void savePlayerConf(Miner m) {
		Configuration g = new Configuration(new File(pluginFolder.getPath() + File.separator + "Players" + File.separator +  m.getPlayer() + ".yml"));
		Map<Integer, Long> p = m.getPermits();
		g.load();
		
		for (int y = 0; y < p.size(); y++){		
			
			g.setProperty("Blocks.block" + y + ".id", p.keySet().toArray()[y]);
			g.setProperty("Blocks.block" + y + ".time", m.getRemainingTime((Integer) p.keySet().toArray()[y]));
		
		}
		
		g.save();
	}	
	
	public static boolean isPermitRequired(int typeId) {
		return blocks.containsKey(typeId);
	}
	
	public static int getCost(int itemID){
		return blocks.get(itemID);
	}

	// Functions for AutoUpdating the Config.yml
	public Object getProperty(String path, Object def, Configuration config) {
		if (isNull(path, config))
			return setProperty(path, def, config);
		return config.getProperty(path);
	}

	public static int getInt(String path, Integer def, ConfigurationNode config) {
		if (isNull(path, config))
			return (Integer) setProperty(path, def, config);
		return config.getInt(path, def);
	}

	public static Boolean getBoolean(String path, Boolean def,
			Configuration config) {
		if (isNull(path, config))
			return (Boolean) setProperty(path, def, config);
		return config.getBoolean(path, def);
	}

	private static Object setProperty(String path, Object val,
			ConfigurationNode config) {
		config.setProperty(path, val);
		return val;
	}

	private static boolean isNull(String path, ConfigurationNode config) {
		return config.getProperty(path) == null;
	}



}
