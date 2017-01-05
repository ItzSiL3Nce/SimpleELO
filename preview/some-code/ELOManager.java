package it.ItzSiL3Nce.simpleelo;

import it.ItzSiL3Nce.simpleelo.config.Configuration;
import it.ItzSiL3Nce.simpleelo.ornamental.Holograms;
import it.ItzSiL3Nce.simpleelo.ornamental.Signs;

import java.io.File;
import java.io.IOException;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public final class ELOManager {

	private ELOManager() {
	}

	public static final void init() {
		File f = new File(SimpleELO.instance.getDataFolder(), "placeholders_help.yml");
		if(!f.exists())
			SimpleELO.instance.saveResource("placeholders_help.yml", true);
		f = new File(SimpleELO.instance.getDataFolder(), "config.yml");
		if (!f.exists())
			SimpleELO.instance.saveDefaultConfig();
		f = new File(SimpleELO.instance.getDataFolder(), "signs.yml");
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		f = new File(SimpleELO.instance.getDataFolder(), "holograms.yml");
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		f = new File(SimpleELO.instance.getDataFolder(), "data.yml");
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Signs.update();
		if(checkHolograms())
			Holograms.update();
	}

	private static final boolean checkHolograms() {
		
		if(SimpleELO.HDEnabled())
		{
			SimpleELO.instance.getServer().getConsoleSender().sendMessage("§6[SimpleELO] §bHooked into HolographicDisplays!");
			return true;
		}
		
		SimpleELO.instance.getServer().getConsoleSender().sendMessage("§6[SimpleELO] §cUnable to find HolographicDisplays! Elograms won't work.");
		return false;
	}
	
	public static final int getELO(OfflinePlayer p) {
		FileConfiguration fc = YamlConfiguration.loadConfiguration(Configuration.data);
		if (fc.contains(p.getUniqueId().toString()))
			return fc.getInt(p.getUniqueId().toString());
		return Configuration.defaultELO();
	}

	public static final void setELO(OfflinePlayer p, Object i) {
		FileConfiguration fc = YamlConfiguration.loadConfiguration(Configuration.data);
		fc.set(p.getUniqueId().toString(), i);
		try {
			fc.save(Configuration.data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static final void resetELO(OfflinePlayer p) {
		setELO(p, null);
		ELOTop.reloadTop();
	}

	public static final String placeholders(String s, OfflinePlayer p) {
		return s.replace("{player}", p.getName()).replace("{elo}",
				String.valueOf(getELO(p))).replace("{position}", String.valueOf(SimpleUtils.getPosition(p)));
	}

	public static final String placeholders(String s, Player killer, Player dead) {
		return s.replace("{killer}", killer.getName())
				.replace("{dead}", dead.getName())
				.replace("{world}", killer.getWorld().getName())
				.replace("{killerelo}", String.valueOf(getELO(killer)))
				.replace("{killerposition}", String.valueOf(SimpleUtils.getPosition(killer)))
				.replace("{deadposition}", String.valueOf(SimpleUtils.getPosition(dead)))
				.replace("{deadelo}", String.valueOf(getELO(dead)));
	}

	public static final String placeholders(String s, Player killer,
			Player dead, int killerprevelo, int deadprevelo) {
		return placeholders(s, killer, dead).replace("{killerprevelo}",
				String.valueOf(killerprevelo)).replace("{deadprevelo}",
				String.valueOf(deadprevelo));
	}
	
	// eloWinner & eloLoser aren't floating point numbers, although it is needed this way otherwise java would break the formula.
	public static final int getWinnerELO(double eloWinner, double eloLoser) {
		if(eloLoser == 0 && eloWinner == 0)
			eloLoser = 1;
		return (int) (eloWinner + (double)(Configuration.eloModificator() * (1 - (eloWinner / (eloWinner + eloLoser)))));
	}
	
	// eloWinner & eloLoser aren't floating point numbers, although it is needed this way otherwise java would break the formula.
	public static final int getLoserELO(double eloWinner, double eloLoser) {
		if(eloLoser == 0 && eloWinner == 0)
			eloWinner = 1;
		return (int) (eloLoser - (double)(Configuration.eloModificator() * (1 - (eloLoser / (eloWinner + eloLoser)))));
	}
}
