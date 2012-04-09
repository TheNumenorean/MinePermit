package net.lotrcraft.minepermit;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandInterpreter implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmnd, String arg2,
			String[] arg3) {

		// Console cant send commands
		if (sender instanceof ConsoleCommandSender) {
			MinePermit.log.info("[MinePermit] Sorry, commands cant be sent from console yet.");
			return true;
		}

		Player p = (Player) sender;

		// Make sure there is a secondary parameter
		if (arg3.length == 0) {
			return false;

		} else if (arg3[0].equalsIgnoreCase("time")) {

			Miner m = MinerManager.getMiner(p);

			int id;

			try {
				id = Integer.parseInt(arg3[1]);
			} catch (Exception e) {

				Map<Integer, Long> tmp = m.getPermits();
				
				if(m.hasUniversalPermit()){
					p.sendMessage(ChatColor.DARK_GREEN + "You have " + m.getRemainingUniversalTime() + " minutes left on the Universal Permit.");
					return true;
				}
				
				if(tmp.isEmpty()){
					p.sendMessage(ChatColor.YELLOW + "You don't own any permits!");
					return true;
				}
					
				for (int y = 0; y < tmp.size(); y++) {
					int n = (Integer) tmp.keySet().toArray()[y];

					p.sendMessage(ChatColor.DARK_GREEN + "" + n + ": " + m.getRemainingTime(n) + " minutes.");
				}

				return true;
			}

			if (!m.hasPermit(id)) {
				p.sendMessage(ChatColor.DARK_RED
						+ "You do not own a permit for " + id);
			} else {
				p.sendMessage(ChatColor.DARK_GREEN + "You have "
						+ m.getRemainingTime(id) + " minutes left.");
			}

			return true;

		} else if (arg3[0].equalsIgnoreCase("cost")) {

			int id;

			try {
				id = Integer.parseInt(arg3[1]);
			} catch (ArrayIndexOutOfBoundsException e1){
				
				//If there is no number, change to Universal system
				p.sendMessage(ChatColor.AQUA + "The cost for the Universal permit for this world is " + Config.universalCost);
				return true;
			} catch (NumberFormatException e2) {
				return false;
			}

			if (!Config.isPermitRequired(id)) {
				p.sendMessage(ChatColor.GRAY
						+ "A permit is not required for this item.");
				return true;
			}

			p.sendMessage(ChatColor.AQUA + "The cost for this item is "
					+ Config.getCost(id) + " dolars");
			return true;

		} else if (arg3[0].equalsIgnoreCase("buy")) {

			int id;

			//Atempt to get an id number for the block
			try {
				id = Integer.parseInt(arg3[1]);
			} catch (ArrayIndexOutOfBoundsException e1){				
				return false;
			} catch (NumberFormatException e) {
				
				if(!arg3[1].equalsIgnoreCase("universal"))
					return false;
				
				//Check if the player already has the Universal permit
				if(!Config.multiPermit && MinerManager.getMiner(p).hasUniversalPermit()){
					p.sendMessage(ChatColor.YELLOW + "You already own the Universal Permit!");
					return true;
				}
				
				//Charge player if possible
				if (!PlayerManager.charge(p, Config.universalCost)) {
					p.sendMessage(ChatColor.DARK_RED + "You dont have enough money!");
					return true;
				}
				
				MinerManager.getMiner(p).addUniversalPermit(Config.permitDuration);

				p.sendMessage(ChatColor.DARK_GREEN + "Permit purchased!");
				return true;
			}

			//Check if a permit is required for this block
			if (!Config.isPermitRequired(id)) {
				p.sendMessage(ChatColor.GRAY + "A permit is not required for this item.");
				return true;
			}
			
			//Check if the player already has a permit for this
			if(!Config.multiPermit && MinerManager.getMiner(p).hasPermit(id)){
				p.sendMessage(ChatColor.YELLOW + "You already have a permit for this!");
				return true;
			}
			
			//Charge player if possible
			if (!PlayerManager.charge(p, Config.getCost(id))) {
				p.sendMessage(ChatColor.DARK_RED + "You dont have enough money!");
				return true;
			}

			



			MinerManager.getMiner(p).addPermit(id, Config.permitDuration);

			p.sendMessage(ChatColor.DARK_GREEN + "Permit purchased!");

			return true;
		} else if (arg3[0].equalsIgnoreCase("view")) {

			int id;

			try {
				id = Integer.parseInt(arg3[1]);
			} catch (IndexOutOfBoundsException e) {

				Map<Integer, Integer> perms = Config.getPermits();
				String tmp = ChatColor.DARK_PURPLE + "Permits are required for: ";
				
				for(Integer i : perms.keySet()){
					tmp += i +  " ";
				}
				
				p.sendMessage(tmp);

				return true;
			} catch (NumberFormatException e2){
				return false;
			}

			if (Config.isPermitRequired(id)) {
				p.sendMessage(ChatColor.DARK_RED
						+ "A permit is required for " + id);
			} else {
				p.sendMessage(ChatColor.DARK_GREEN + "A permit is not required for " + id);
			}

			return true;

		} 

		return false;
	}
}
