package net.lotrcraft.minepermit;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;

public class Listener extends BlockListener {
	
	public void onBlockBreak(BlockBreakEvent e){
		
		if(!Config.isPermitRequired(e.getBlock().getTypeId()))
			return;
		
		Player p = e.getPlayer();
		
		if(p.hasPermission("MinePermit.exempt"))
			return;
		
		if(!MinerManager.getMiner(p).hasPermit(e.getBlock().getTypeId())){
			e.setCancelled(true);
			p.sendMessage(ChatColor.DARK_RED + "You may not mine these blocks! Use /permit buy <id> to buy a permit.");
		}
		
	}

}
