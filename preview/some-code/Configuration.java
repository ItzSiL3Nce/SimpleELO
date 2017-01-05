package it.ItzSiL3Nce.simpleelo.config;

import it.ItzSiL3Nce.simpleelo.SimpleELO;
import it.ItzSiL3Nce.simpleelo.placeholders.Loader;
import it.ItzSiL3Nce.simpleelo.util.Chat;
import it.ItzSiL3Nce.simpleelo.worldguard.WorldGuardELO;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public final class Configuration {

	private Configuration() {}
	
	private static int DEFAULT = -1;
	private static boolean PREVENT_SK = true;
	private static boolean SK_LOOKED_UP = false;
	private static List<String> ELO_MESSAGE = null;
	private static List<String> ELO_COMMAND = null;
	private static String INEXISTANT_PLAYER = null;
	private static String NO_PERMISSIONS = null;
	private static int TOP_PLAYERS = -1;
	private static String TOP_HEADER = null;
	private static String TOP_MESSAGE = null;
	private static String TOP_FOOTER = null;
	
	private static String ELO_RESETTED = null;
	
	private static boolean WG_LOOKED_UP = false;
	private static boolean WG_INTEGRATION = false;
	
	private static int ELO_MODIFICATOR = -1;
	
	private static boolean JOK_LOOKED_UP = false;
	private static boolean JOK = false;
	
	private static boolean PLACEHOLDERS_LOOKED_UP = false;
	private static boolean PLACEHOLDERS = true;
	
	public static final File config = new File(SimpleELO.instance.getDataFolder(), "config.yml");
	public static final File data = new File(SimpleELO.instance.getDataFolder(), "data.yml");
	public static final File signs = new File(SimpleELO.instance.getDataFolder(), "signs.yml");
	public static final File holograms = new File(SimpleELO.instance.getDataFolder(), "holograms.yml");
	public static final File regions = new File(SimpleELO.instance.getDataFolder(), "regions.yml");
	public static final File kills = new File(SimpleELO.instance.getDataFolder(), "kills.yml");

	public static final boolean wgIntegration() {
		if(!WG_LOOKED_UP) {
			WG_INTEGRATION = YamlConfiguration.loadConfiguration(config).getBoolean("worldguard-integration");
			WG_LOOKED_UP = true;
		}
		return WG_INTEGRATION;
	}
	
	public static final boolean placeholders() {
		if(!PLACEHOLDERS_LOOKED_UP) {
			PLACEHOLDERS = YamlConfiguration.loadConfiguration(config).getBoolean("enable-placeholders");
			PLACEHOLDERS_LOOKED_UP = true;
		}
		return PLACEHOLDERS;
	}
	
	public static final int defaultELO() {
		if(DEFAULT == -1)
			DEFAULT = YamlConfiguration.loadConfiguration(config).getInt("default-elo");
		return DEFAULT;
	}
	
	public static final int eloModificator() {
		if(ELO_MODIFICATOR == -1)
			ELO_MODIFICATOR = YamlConfiguration.loadConfiguration(config).getInt("elo-modificator");
		return ELO_MODIFICATOR;
	}
	
	public static final boolean preventSpawnKill() {
		if(!SK_LOOKED_UP) {
			PREVENT_SK = YamlConfiguration.loadConfiguration(config).getBoolean("prevent-spawn-kill");
			SK_LOOKED_UP = true;
		}
		return PREVENT_SK;
	}
	
	public static final boolean justOneKill() {
		if(!JOK_LOOKED_UP) {
			JOK = YamlConfiguration.loadConfiguration(config).getBoolean("just-one-kill");
			JOK_LOOKED_UP = true;
		}
		if(JOK && !kills.exists()) {
			try {
				kills.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return JOK;
	}
	
	
	public static final void reload() {
		if(!config.exists())
			SimpleELO.instance.saveDefaultConfig();
		FileConfiguration fc = YamlConfiguration.loadConfiguration(config);
		DEFAULT = fc.getInt("default-elo");
		PREVENT_SK = fc.getBoolean("prevent-spawn-kill");
		SK_LOOKED_UP = true;
		ELO_MESSAGE = Chat.translate(fc.getStringList("new-elo-message"));
		ELO_COMMAND = Chat.translate(fc.getStringList("elo-command"));
		INEXISTANT_PLAYER = Chat.translate(fc.getString("inexistant-player"));
		NO_PERMISSIONS = Chat.translate(fc.getString("no-permissions"));
		TOP_PLAYERS = fc.getInt("top-players");
		TOP_HEADER = Chat.translate(fc.getString("top-header"));
		TOP_MESSAGE = Chat.translate(fc.getString("top-message"));
		TOP_FOOTER = Chat.translate(fc.getString("top-footer"));
		ELO_RESETTED = Chat.translate(fc.getString("elo-reset"));
		WG_INTEGRATION = fc.getBoolean("worldguard-integration");
		WG_LOOKED_UP = true;
		JOK = YamlConfiguration.loadConfiguration(config).getBoolean("just-one-kill");
		JOK_LOOKED_UP = true;
		ELO_MODIFICATOR = YamlConfiguration.loadConfiguration(config).getInt("elo-modificator");
		PLACEHOLDERS = YamlConfiguration.loadConfiguration(config).getBoolean("enable-placeholders");
		PLACEHOLDERS_LOOKED_UP = true;
		if(JOK && !kills.exists()) {
			try {
				kills.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		WorldGuardELO.init();
		Sign.reload();
		Hologram.reload();
		try {
			Loader.load();
		} catch(Exception e) {
		}
	}
	
	public static final List<String> eloMessage() {
		if(ELO_MESSAGE == null)
			ELO_MESSAGE = Chat.translate(YamlConfiguration.loadConfiguration(config).getStringList("new-elo-message"));
		return ELO_MESSAGE;
	}
	
	public static final List<String> eloCommand() {
		if(ELO_COMMAND == null)
			ELO_COMMAND = Chat.translate(YamlConfiguration.loadConfiguration(config).getStringList("elo-command"));
		return ELO_COMMAND;
	}
	
	public static final String inexistantPlayer() {
		if(INEXISTANT_PLAYER == null)
			INEXISTANT_PLAYER = Chat.translate(YamlConfiguration.loadConfiguration(config).getString("inexistant-player"));
		return INEXISTANT_PLAYER;
	}
	
	public static final String noPermissions() {
		if(NO_PERMISSIONS == null)
			NO_PERMISSIONS = Chat.translate(YamlConfiguration.loadConfiguration(config).getString("no-permissions"));
		return NO_PERMISSIONS;
	}
	
	public static final int topPlayers() {
		if(TOP_PLAYERS == -1)
			TOP_PLAYERS = YamlConfiguration.loadConfiguration(config).getInt("top-players");
		return TOP_PLAYERS;
	}
	
	public static final String topHeader() {
		if(TOP_HEADER == null)
			TOP_HEADER = Chat.translate(YamlConfiguration.loadConfiguration(config).getString("top-header"));
		return TOP_HEADER;
	}
	
	public static final String topMessage() {
		if(TOP_MESSAGE == null)
			TOP_MESSAGE = Chat.translate(YamlConfiguration.loadConfiguration(config).getString("top-message"));
		return TOP_MESSAGE;
	}
	
	public static final String topFooter() {
		if(TOP_FOOTER == null)
			TOP_FOOTER = Chat.translate(YamlConfiguration.loadConfiguration(config).getString("top-footer"));
		return TOP_FOOTER;
	}
	
	public static final String eloResetted() {
		if(ELO_RESETTED == null)
			ELO_RESETTED = Chat.translate(YamlConfiguration.loadConfiguration(config).getString("elo-reset"));
		return ELO_RESETTED;
	}
	
	public static final class Sign {
		
		private static String[] LINES = {null, null, null, null};
		
		public static final String getLine(int i) {
			if(LINES[i] == null)
				LINES[i] = Chat.translate(YamlConfiguration.loadConfiguration(config).getString("sign.line-" + (i + 1)));
			return LINES[i];
		}
		
		public static final String[] getLines() {
			for(int i = 0; i < 4; i++)
				getLine(i);
			return LINES;
		}
		
		public static final void reload() {
			for(int i = 0; i < LINES.length; i++)
				LINES[i] = Chat.translate(YamlConfiguration.loadConfiguration(config).getString("sign.line-" + (i + 1)));
		}
	}
	
	public static final class Hologram {
		
		private static List<String> LINES = null;
		
		private static boolean LOOKED_UP = false;
		private static boolean TOP3_ITEM = true;
		
		public static final List<String> getLines() {
			if(LINES == null)
				LINES = Chat.translate(YamlConfiguration.loadConfiguration(config).getStringList("hologram"));
			return LINES;
		}
		
		public static final boolean top3Item() {
			if(!LOOKED_UP) {
				LOOKED_UP = true;
				TOP3_ITEM = YamlConfiguration.loadConfiguration(config).getBoolean("hologram-item-top3");
			}
			return TOP3_ITEM;
		}
		
		public static final void reload() {
			FileConfiguration fc = YamlConfiguration.loadConfiguration(config);
			LINES = Chat.translate(fc.getStringList("hologram"));
			LOOKED_UP = true;
			TOP3_ITEM = fc.getBoolean("hologram-item-top3");
		}
	}
}
