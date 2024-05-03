package io.github.townyadvanced.townyspawnointlimits9000;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.initialization.TownyInitException;
import com.palmergames.bukkit.towny.object.Translatable;
import com.palmergames.bukkit.towny.object.TranslationLoader;
import com.palmergames.bukkit.towny.scheduling.TaskScheduler;
import com.palmergames.bukkit.towny.scheduling.impl.BukkitTaskScheduler;
import com.palmergames.bukkit.towny.scheduling.impl.FoliaTaskScheduler;

import io.github.townyadvanced.townyspawnointlimits9000.settings.Settings;

public class TownySpawnPointLimits9000 extends JavaPlugin {

	private static TownySpawnPointLimits9000 plugin;
	private final Object scheduler;
	private static String requiredTownyVersion = "0.100.2.7";
	boolean hasConfig = true;
	boolean hasLocale = true;
	boolean hasListeners = true;

	public TownySpawnPointLimits9000() {
		plugin = this;
		this.scheduler = townyVersionCheck() ? isFoliaClassPresent() ? new FoliaTaskScheduler(this) : new BukkitTaskScheduler(this) : null;
	}
	
	@Override
	public void onEnable() {

		if (!townyVersionCheck()) {
			severe("Towny version does not meet required minimum version: " + requiredTownyVersion);
			onDisable();
			return;
		} else {
			info("Towny version " + getTownyVersion() + " found.");
		}

		if ((hasConfig && !loadConfig())
		|| (hasLocale && !loadLocalization(false))) {
			onDisable();
			return;
		}

		if (hasListeners)
			registerListeners(Bukkit.getPluginManager());
	}

	private boolean loadConfig() {
		try {
			Settings.loadConfig();
		} catch (TownyInitException e) {
			e.printStackTrace();
			severe("Config.yml failed to load! Disabling!");
			return false;
		}
		info("Config.yml loaded successfully.");
		return true;
	}

	public static boolean loadLocalization(boolean reload) {
		try {
			Plugin plugin = getPlugin(); 
			Path langFolderPath = Paths.get(plugin.getDataFolder().getPath()).resolve("lang");
			TranslationLoader loader = new TranslationLoader(langFolderPath, plugin, TownySpawnPointLimits9000.class);
			loader.load();
			TownyAPI.getInstance().addTranslations(plugin, loader.getTranslations());
		} catch (TownyInitException e) {
			e.printStackTrace();
			severe("Locale files failed to load! Disabling!");
			return false;
		}
		if (reload) {
			info(Translatable.of("msg_reloaded_lang").defaultLocale());
		}
		return true;
	}

	private void registerListeners(PluginManager pm) {
		pm.registerEvents(new TownyListener(), this);
	}

	public String getVersion() {
		return this.getDescription().getVersion();
	}

	public static TownySpawnPointLimits9000 getPlugin() {
		return plugin;
	}

	public static String getPrefix() {
		return "[" + plugin.getName() + "]";
	}

	private boolean townyVersionCheck() {
		try {
			return Towny.isTownyVersionSupported(requiredTownyVersion);
		} catch (NoSuchMethodError e) {
			return false;
		}
	}

	private String getTownyVersion() {
		return Bukkit.getPluginManager().getPlugin("Towny").getDescription().getVersion();
	}

	public static void info(String message) {
		plugin.getLogger().info(message);
	}

	public static void severe(String message) {
		plugin.getLogger().severe(message);
	}
	
	public static boolean hasLocale() {
		return plugin.hasLocale;
	}

	public TaskScheduler getScheduler() {
		return (TaskScheduler) this.scheduler;
	}

	private static boolean isFoliaClassPresent() {
		try {
			Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
}
