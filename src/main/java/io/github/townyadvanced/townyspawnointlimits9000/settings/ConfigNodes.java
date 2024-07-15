package io.github.townyadvanced.townyspawnointlimits9000.settings;

public enum ConfigNodes {
	
	VERSION_HEADER("version", "", ""),
	VERSION(
			"version.version",
			"",
			"# This is the current version.  Please do not edit."),
	SPAWNING_ROOT("spawning.y_limits","",""),
	SPAWNING_Y_LIMITS_ROOT("spawning.y_limits","",""),
	SPAWNING_Y_LIMITS_ENABLED("spawning.y_limits.enabled",
			"false",
			"",
			"# Should towns, nations be limited in how low they can set a spawn point, or an outpost spawn point?"),
	SPAWNING_Y_LIMITS_LOWEST_Y_ALLOWED("spawning.y_limits.lowest_y_allowed",
			"0",
			"",
			"# When limiting is configured, what is the lowest y level allowed?"),
	SPAWNING_Y_LIMITS_HIGHEST_Y_ALLOWED("spawning.y_limits.highest_y_allowed",
			"100",
			"",
			"# When limiting is configured, what is the highest y level allowed?"),
	BIOMES_ROOT("biomes","",""),
	BIOMES_BAD_BIOMES("biomes.bad_biomes",
			"",
			"",
			"# A comma separated list of biomes that spawns cannot be set in. ie: the_end,end_barrens,end_highlands,end_midlands,small_end_islands");
	
	private final String Root;
	private final String Default;
	private String[] comments;

	ConfigNodes(String root, String def, String... comments) {

		this.Root = root;
		this.Default = def;
		this.comments = comments;
	}

	/**
	 * Retrieves the root for a config option
	 *
	 * @return The root for a config option
	 */
	public String getRoot() {

		return Root;
	}

	/**
	 * Retrieves the default value for a config path
	 *
	 * @return The default value for a config path
	 */
	public String getDefault() {

		return Default;
	}

	/**
	 * Retrieves the comment for a config path
	 *
	 * @return The comments for a config path
	 */
	public String[] getComments() {

		if (comments != null) {
			return comments;
		}

		String[] comments = new String[1];
		comments[0] = "";
		return comments;
	}

}
