package it.ItzSiL3Nce.simpleelo.listeners;

import it.ItzSiL3Nce.simpleelo.ELOManager;
import it.ItzSiL3Nce.simpleelo.ELOPlayer;
import it.ItzSiL3Nce.simpleelo.ELOTop;
import it.ItzSiL3Nce.simpleelo.config.Configuration;
import it.ItzSiL3Nce.simpleelo.worldguard.WorldGuardELO;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerDeathListener implements Listener {

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		if (p.getKiller() instanceof Player && p.getKiller() != null) {
			Player k = p.getKiller();
			if((!hasArmor(p) && Configuration.preventSpawnKill())
					|| (Configuration.justOneKill() && new ELOPlayer(k).hasKilled(p))
					|| (Configuration.wgIntegration() && WorldGuardELO.isInBlockedRegion(k)))
				return;

			int elo1 = ELOManager.getELO(k);
			int elo2 = ELOManager.getELO(p);
			
			ELOManager.setELO(k, ELOManager.getWinnerELO(elo1, elo2));
			ELOManager.setELO(p, ELOManager.getLoserELO(elo1, elo2));
			
			for(String s: Configuration.eloMessage()) {
				k.sendMessage(ELOManager.placeholders(s, k, p, elo1, elo2));
				p.sendMessage(ELOManager.placeholders(s, k, p, elo1, elo2));
			}
			
			ELOTop.reloadTop();
		}
	}

	private static final boolean hasArmor(Player p) {
		ItemStack[] i = p.getInventory().getArmorContents();
		for (ItemStack is : i) {
			if (is != null && is.getType() != Material.AIR)
				return true;
		}
		return false;
	}

}
