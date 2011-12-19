package net.lotrcraft.minepermit;

import java.util.Map;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MinerManager {

	private static Map<String, Miner> miners = new TreeMap<String, Miner>();

	public static Miner getMiner(String playerName) {

		Miner m = miners.get(playerName);

		if (m == null) {
			return addNewMiner(playerName);
		}

		return m;

	}
	
	public static Miner getMiner(Player p) {
		return getMiner(p.getName());
		
	}

	public static Miner addNewMiner(String playerName) {
		return addNewMiner(Bukkit.getPlayerExact(playerName));

	}

	public static Miner addNewMiner(Player player) {
		return addMiner(new Miner(player.getName()));
	}

	public static Miner addMiner(Miner player) {
		miners.put(player.getPlayer(), player);
		return player;
	}
	
	public static void savePlayers() {
		
		for(int y = 0; y < miners.size(); y++){
			Config.savePlayerConf(miners.get(miners.keySet().toArray()[y]));
		}
		
	}


}
