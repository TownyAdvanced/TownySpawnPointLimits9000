This plugin stops players setting their spawn points outside of a configurable window of valid Y levels.

This includes: 
- town spawn points,
- town outpost spawn points, and 
- nation spawn points.

They will be denied when setting their spawn too low or too high.

## Installation:

1. Make sure that you have Towny 0.100.2.7 or newer installed.
2. Add the plugin to your plugins folder.
3. Start and Stop the server so that a `plugins\TownySpawnPointLimits9000\config.yml` is created.
4. Open the `plugins\TownySpawnPointLimits9000\config.yml` and set `spawning.y_limits.enabled` to true, then configure the `spawning.y_limits.lowest_y_allowed` and `spawning.y_limits.highest_y_allowed` values.
5. Start your server again.
