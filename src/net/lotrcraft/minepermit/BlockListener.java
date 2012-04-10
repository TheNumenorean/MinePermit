package net.lotrcraft.minepermit;

import net.lotrcraft.minepermit.languages.TextManager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockListener implements Listener {
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e){
		
		if(!Config.isPermitRequired(e.getBlock().getTypeId()))
			return;
		
		Player p = e.getPlayer();
		
		if(PlayerManager.hasPerm(p, "MinePermit.exempt"))
			return;
		
		
		
		if(!MinerManager.getMiner(p).hasPermit(e.getBlock().getTypeId())){
			e.setCancelled(true);
			p.sendMessage(TextManager.MISSING_PERMIT_ERROR + "");
		}
		
	}

}
