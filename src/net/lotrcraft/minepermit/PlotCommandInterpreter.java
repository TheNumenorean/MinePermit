package net.lotrcraft.minepermit;

import net.lotrcraft.minepermit.plot.Plot;
import net.lotrcraft.minepermit.world.PermitWorld;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class PlotCommandInterpreter implements CommandExecutor {

	private MinePermit mp;

	public PlotCommandInterpreter(MinePermit minePermit) {
		this.mp = minePermit;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		
		if(sender instanceof ConsoleCommandSender)
			mp.log.info("Console cannot use the Plot command");
		
		if(args[0].equalsIgnoreCase("list")){
			
			
			if(args.length > 1){
				World w = mp.getServer().getWorld(args[1]);
				
				if(w == null){
					sender.sendMessage(ChatColor.RED + "Invalid World!");
					return true;
				}
				
				PermitWorld pw = mp.getPWM().getPermitWorld(w);
				if(pw == null){
					sender.sendMessage(ChatColor.DARK_GRAY + "That world is not able to have plots.");
				}
				
				for (Plot p : pw.getPlots()){
					if(p.getOwner().equals(sender.getName()))
						sender.sendMessage(ChatColor.GREEN + p.toString());
					else if(p.canUse(sender.getName()))
						sender.sendMessage(ChatColor.YELLOW + p.toString());
				}
			}
		}
		
		return false;
	}

}
