package io.github.townyadvanced.townyspawnointlimits9000;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;

import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import com.palmergames.bukkit.towny.event.CancellableTownyEvent;
import com.palmergames.bukkit.towny.event.TranslationLoadEvent;
import com.palmergames.bukkit.towny.event.nation.NationSetSpawnEvent;
import com.palmergames.bukkit.towny.event.town.TownPreSetHomeBlockEvent;
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
	public void onTownSetHomeblock(TownPreSetHomeBlockEvent event) {
		if (!Settings.isSpawnYLevelLimitingEnabled())
			return;
		testY(event, event.getPlayer(), event.getPlayer().getLocation().getY());
		if (isBadBiome(event.getPlayer().getLocation()))
			cancelForBiome(event, event.getPlayer());
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onTownSetSpawn(TownSetSpawnEvent event) {
		if (!Settings.isSpawnYLevelLimitingEnabled())
			return;
		testY(event, event.getPlayer(), event.getNewSpawn().getY());
		if (isBadBiome(event.getNewSpawn()))
			cancelForBiome(event, event.getPlayer());
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onTownSetOutpostSpawn(TownSetOutpostSpawnEvent event) {
		if (!Settings.isSpawnYLevelLimitingEnabled())
			return;
		testY(event, event.getPlayer(), event.getNewSpawn().getY());
		if (isBadBiome(event.getNewSpawn()))
			cancelForBiome(event, event.getPlayer());
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onNationSetSpawn(NationSetSpawnEvent event) {
		if (!Settings.isSpawnYLevelLimitingEnabled())
			return;
		testY(event, event.getPlayer(), event.getNewSpawn().getY());
		if (isBadBiome(event.getNewSpawn()))
			cancelForBiome(event, event.getPlayer());
	}

	private void testY(CancellableTownyEvent event, Player player, double y) {
		if (y <= Settings.getSpawningLowestYLevelAllowed())
			cancelEventTooLow(event, player);
		else if (y >= Settings.getSpawningHighestYLevelAllowed())
			cancelEventTooHigh(event, player);
	}

	private void cancelEventTooLow(CancellableTownyEvent event, Player player) {
		event.setCancelMessage(Translatable.of("spawn_points9000_msg_err_you_cannot_set_this_spawn_point_below",
				Settings.getSpawningLowestYLevelAllowed()).forLocale(player));
		event.setCancelled(true);
	}

	private void cancelEventTooHigh(CancellableTownyEvent event, Player player) {
		event.setCancelMessage(Translatable.of("spawn_points9000_msg_err_you_cannot_set_this_spawn_point_above",
				Settings.getSpawningHighestYLevelAllowed()).forLocale(player));
		event.setCancelled(true);
	}
	
	private boolean isBadBiome(Location loc) {
		if (Settings.getUnwantedBiomeNames().isEmpty())
			return false;

		Predicate<Biome> isUnwantedBiomePredicate = (biome) -> Settings.getUnwantedBiomeNames().contains(biome.name().toLowerCase(Locale.ROOT));
		return isUnwantedBiomePredicate.test(loc.getBlock().getBiome());
	}

	private void cancelForBiome(CancellableTownyEvent event, @NotNull Player player) {
		event.setCancelMessage(Translatable.of("spawn_points9000_msg_err_you_cannot_set_spawn_in_this_biome").forLocale(player));
		event.setCancelled(true);
	}
}
