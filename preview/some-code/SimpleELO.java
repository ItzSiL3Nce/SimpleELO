package it.ItzSiL3Nce.simpleelo;

import it.ItzSiL3Nce.simpleelo.commands.ELOCommand;
import it.ItzSiL3Nce.simpleelo.commands.ELOGiveCommand;
import it.ItzSiL3Nce.simpleelo.commands.ELOReloadCommand;
import it.ItzSiL3Nce.simpleelo.commands.ELOResetCommand;
import it.ItzSiL3Nce.simpleelo.commands.ELOSetCommand;
import it.ItzSiL3Nce.simpleelo.commands.ELOSignCommand;
import it.ItzSiL3Nce.simpleelo.commands.ELOTopCommand;
import it.ItzSiL3Nce.simpleelo.commands.hologram.ELOHologramCommand;
import it.ItzSiL3Nce.simpleelo.commands.hologram.ELOHologramMoveHereCommand;
import it.ItzSiL3Nce.simpleelo.commands.hologram.ELOHologramRemoveCommand;
import it.ItzSiL3Nce.simpleelo.commands.hologram.ELOHologramsCommand;
import it.ItzSiL3Nce.simpleelo.commands.worldguard.ELOAllowRegionCommand;
import it.ItzSiL3Nce.simpleelo.commands.worldguard.ELODenyRegionCommand;
import it.ItzSiL3Nce.simpleelo.listeners.ELOTopChangedListener;
import it.ItzSiL3Nce.simpleelo.listeners.PlayerDeathListener;
import it.ItzSiL3Nce.simpleelo.listeners.PlayerInteractListener;
import it.ItzSiL3Nce.simpleelo.ornamental.Holograms;
import it.ItzSiL3Nce.simpleelo.placeholders.Loader;
import it.ItzSiL3Nce.simpleelo.worldguard.WorldGuardELO;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleELO extends JavaPlugin {

	public static SimpleELO instance;
	
	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage("§6[SimpleELO] §bMade by ItzSiL3Nce");
		instance = this;
		ELOManager.init();
		WorldGuardELO.init();
		try {
			Loader.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
		PluginManager pm = getServer().getPluginManager();
		getCommand("elo").setExecutor(new ELOCommand());
		getCommand("eloreload").setExecutor(new ELOReloadCommand());
		getCommand("topelo").setExecutor(new ELOTopCommand());
		getCommand("elosign").setExecutor(new ELOSignCommand());
		getCommand("eloholo").setExecutor(new ELOHologramCommand());
		getCommand("eloholoremove").setExecutor(new ELOHologramRemoveCommand());
		getCommand("elograms").setExecutor(new ELOHologramsCommand());
		getCommand("eloholohere").setExecutor(new ELOHologramMoveHereCommand());
		getCommand("eloreset").setExecutor(new ELOResetCommand());
		getCommand("denyelo").setExecutor(new ELODenyRegionCommand());
		getCommand("allowelo").setExecutor(new ELOAllowRegionCommand());
		getCommand("elogive").setExecutor(new ELOGiveCommand());
		getCommand("eloset").setExecutor(new ELOSetCommand());
		pm.registerEvents(new PlayerDeathListener(), this);
		pm.registerEvents(new PlayerInteractListener(), this);
		pm.registerEvents(new ELOTopChangedListener(), this);
		Bukkit.getConsoleSender().sendMessage("§6[SimpleELO] §bEnabled.");
	}
	
	public void onDisable() {
		Loader.unload();
		Holograms.deleteHolograms();
	}

	public List<String> onTabComplete(CommandSender sender, Command command,
			String alias, String[] args, Location location) {
		return null;
	}
	
	public static final boolean HDEnabled() {
		return instance.getServer().getPluginManager().isPluginEnabled("HolographicDisplays");
	}
	
	public static final boolean PAPIEnabled(JavaPlugin p) {
		return p.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
	}
	
	public static final boolean MVPAPIEnabled(JavaPlugin p) {
		return p.getServer().getPluginManager().isPluginEnabled("MVdWPlaceholderAPI");
	}
}
