package net.lotrcraft.minepermit;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerManager {

	public static boolean charge(Player p, int amount) {
		if (Config.useEconomyPlugin) {
			
			EconomyResponse r = MinePermit.econ.withdrawPlayer(p.getName(), amount);
			
			if(r.transactionSuccess())
				return true;
			return false;
			
		} else {
			
			while (amount > 0) {
				ItemStack x = p.getInventory().getItem(
						p.getInventory().first(Config.currencyBlockID));

				if (x.getAmount() < amount) {
					amount -= x.getAmount();
					x.setAmount(0);
				} else {
					x.setAmount(x.getAmount() - amount);
					amount = 0;
				}
			}
			return true;
			
		}
		
	}
	
	public static boolean hasPerm(Player p, String perm){
		if(Config.useVaultPermissions)
			return MinePermit.perm.has(p.getWorld(), p.getName(), perm);
		
		return p.hasPermission(perm);
	}
}
