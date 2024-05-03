package io.github.townyadvanced.townyspawnointlimits9000;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.palmergames.bukkit.towny.event.CancellableTownyEvent;
import com.palmergames.bukkit.towny.event.TranslationLoadEvent;
import com.palmergames.bukkit.towny.event.nation.NationSetSpawnEvent;
import com.palmergames.bukkit.towny.event.town.TownSetOutpostSpawnEvent;
import com.palmergames.bukkit.towny.event.town.TownSetSpawnEvent;
import com.palmergames.bukkit.towny.object.Translatable;
import com.palmergames.bukkit.towny.object.TranslationLoader;

import io.github.townyadvanced.townyspawnointlimits9000.settings.Settings;

public class TownyListener implements Listener {

	/* Handle re-adding the lang string into Towny when Towny reloads the Translation Registry. */
	@EventHandler
	public void onTownyLoadLang(TranslationLoadEvent event) {
		Plugin plugin = TownySpawnPointLimits9000.getPlugin();
		if (!TownySpawnPointLimits9000.hasLocale())
			return;
		Path langFolderPath = Paths.get(plugin.getDataFolder().getPath()).resolve("lang");
		TranslationLoader loader = new TranslationLoader(langFolderPath, plugin, TownySpawnPointLimits9000.class);
		loader.load();
		Map<String, Map<String, String>>  translations = loader.getTranslations();
		for (String language : translations.keySet())
			for (Map.Entry<String, String> map : translations.get(language).entrySet())
				event.addTranslation(language, map.getKey(), map.getValue());
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onTownSetSpawn(TownSetSpawnEvent event) {
		if (!Settings.isSpawnYLevelLimitingEnabled())
			return;
		testY(event, event.getPlayer(), event.getNewSpawn().getY());
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onTownSetOutpostSpawn(TownSetOutpostSpawnEvent event) {
		if (!Settings.isSpawnYLevelLimitingEnabled())
			return;
		testY(event, event.getPlayer(), event.getNewSpawn().getY());
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onNationSetSpawn(NationSetSpawnEvent event) {
		if (!Settings.isSpawnYLevelLimitingEnabled())
			return;
		testY(event, event.getPlayer(), event.getNewSpawn().getY());
	}

	private void testY(CancellableTownyEvent event, Player player, double y) {
		if (y <= Settings.getSpawningHighestYLevelAllowed())
			cancelEventTooLow(event, player);
		else if (y >= Settings.getSpawningLowestYLevelAllowed())
			cancelEventTooHigh(event, player);
	}

	private void cancelEventTooLow(CancellableTownyEvent event, Player player) {
		event.setCancelMessage(Translatable.of("spawn_points9000_msg_err_you_cannot_set_this_spawn_point_below",
				Settings.getSpawningLowestYLevelAllowed()).forLocale(player));
		event.setCancelled(true);
	}

	private void cancelEventTooHigh(CancellableTownyEvent event, Player player) {
		event.setCancelMessage(Translatable.of("spawn_points9000_msg_err_you_cannot_set_this_spawn_point_above",
				Settings.getSpawningLowestYLevelAllowed()).forLocale(player));
		event.setCancelled(true);
	}
}
