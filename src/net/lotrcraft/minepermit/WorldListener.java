package net.lotrcraft.minepermit;

import net.lotrcraft.minepermit.miner.Miner;
import net.lotrcraft.minepermit.world.PermitWorld;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class WorldListener implements Listener {
	
	private MinePermit mp;

	public WorldListener(MinePermit mp){
		this.mp = mp;
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e){
		Miner m = mp.getMinerManager().getMiner(e.getPlayer());
		
		PermitWorld pw = mp.getPWM().getPermitWorld(e.getBlock().getLocation().getWorld().getName());
		if(pw != null){
			Plot = pw.getContainingPlot(e.getBlock().getLocation());
		} else 
			return;
		
		if()
	}
}
