package it.ItzSiL3Nce.simpleelo.ornamental;

import it.ItzSiL3Nce.simpleelo.ELOTop;
import it.ItzSiL3Nce.simpleelo.SimpleELO;
import it.ItzSiL3Nce.simpleelo.config.Configuration;
import it.ItzSiL3Nce.simpleelo.util.Localizer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

public final class Holograms {

	private Holograms() {
	}

	private static Map<String, Hologram> holos = new HashMap<String, Hologram>();

	private static final ItemStack first = new ItemStack(Material.DIAMOND_SWORD);
	private static final ItemStack second = new ItemStack(Material.IRON_SWORD);
	private static final ItemStack third = new ItemStack(Material.GOLD_SWORD);

	static {
		first.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		second.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		third.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
	}

	public static final void deleteHolograms() {
		if (SimpleELO.HDEnabled()) {
			for (Hologram h : holos.values())
				h.delete();
		}
		holos.clear();
	}

	public static final Map<String, Hologram> getHolograms() {
		return holos;
	}

	public static final boolean remove(String name) {
		FileConfiguration fc = YamlConfiguration
				.loadConfiguration(Configuration.holograms);
		if (fc.contains(name)) {
			fc.set(name, null);
			try {
				fc.save(Configuration.holograms);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (SimpleELO.HDEnabled() && holos.containsKey(name)) {
				holos.get(name).delete();
				holos.remove(name);
			}
			return true;
		}
		return false;
	}

	public static final void create(Location loc, String name, int pos,
			boolean alreadySpawned) {
		Localizer.saveLocation(loc, Configuration.holograms,
				name.toLowerCase(), pos);
		if (!alreadySpawned)
			spawn(loc, name.toLowerCase(), pos);
	}

	public static final void create(Location loc, String name, int pos) {
		create(loc, name, pos, false);
	}

	private static final void spawn(Location loc, String n, int pos) {
		if (SimpleELO.HDEnabled()) {
			List<String> holoLines = Configuration.Hologram.getLines();
			Hologram holo = HologramsAPI
					.createHologram(SimpleELO.instance, loc);
			holo.getVisibilityManager().setVisibleByDefault(true);
			List<Entry<String, Integer>> top = ELOTop.getTop();
			String name;
			Integer elo;
			String line;
			boolean itemPut = false;
			try {
				Entry<String, Integer> entry = top.get(pos - 1);
				name = entry.getKey();
				elo = entry.getValue();
			} catch (IndexOutOfBoundsException | NullPointerException e) {
				name = "[Nobody]";
				elo = Integer.valueOf(Configuration.defaultELO());
			}
			for (int i = 0; i < holoLines.size(); i++) {
				line = holoLines.get(i);
				if(line.equalsIgnoreCase("{item}")){
					if (pos == 1)
						holo.insertItemLine(i, first);
					if (pos == 2)
						holo.insertItemLine(i, second);
					if (pos == 3)
						holo.insertItemLine(i, third);
					itemPut = true;
				} else
				holo.appendTextLine(
						
						line.replace("{elo}", elo.toString())
								.replace("{position}", String.valueOf(pos))
								.replace("{player}", name));

			}
			if (Configuration.Hologram.top3Item() && !itemPut) {
				if (pos == 1)
					holo.appendItemLine(first);
				if (pos == 2)
					holo.appendItemLine(second);
				if (pos == 3)
					holo.appendItemLine(third);
			}
			holos.put(n, holo);
		} else
			SimpleELO.instance
					.getServer()
					.getConsoleSender()
					.sendMessage(
							"§6[SimpleELO] §bHolographicDisplays not found. Unable to create Elograms.");
	}

	public static final void update() {
		if (SimpleELO.HDEnabled()) {
			deleteHolograms();
			final FileConfiguration fc = YamlConfiguration
					.loadConfiguration(Configuration.holograms);
			String st;
			Location l;
			int pos;
			for (Iterator<String> it = fc.getKeys(false).iterator(); it
					.hasNext();) {
				st = it.next();
				l = Localizer.getLocation(Configuration.holograms, st);
				pos = fc.getInt(st + ".data");
				spawn(l, st, pos);
			}
		}
	}
}
