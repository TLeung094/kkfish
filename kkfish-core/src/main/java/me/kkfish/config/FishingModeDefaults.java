package me.kkfish.config;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * Resolves and migrates the configurable default fishing mode.
 */
public final class FishingModeDefaults {

    private FishingModeDefaults() {
    }

    /**
     * Returns true only when the configured mode is "vanilla". Missing and
     * invalid values intentionally fall back to plugin mode for compatibility.
     */
    public static boolean isVanillaDefault(String configuredMode) {
        return "vanilla".equals(normalize(configuredMode));
    }

    /**
     * Normalizes a configured mode. Only "plugin" and "vanilla" are valid;
     * missing or invalid values resolve to "plugin".
     */
    public static String normalize(String configuredMode) {
        if (configuredMode == null) {
            return "plugin";
        }
        String normalized = configuredMode.trim().toLowerCase(java.util.Locale.ROOT);
        return "vanilla".equals(normalized) ? "vanilla" : "plugin";
    }

    /**
     * Adds the new setting to existing installations without changing their
     * established behaviour. Bundled configs may explicitly opt into vanilla.
     *
     * @return true when the configuration was changed
     */
    public static boolean ensureDefaultMode(FileConfiguration config) {
        if (config.contains("mode-switch.default-mode")) {
            return false;
        }
        config.set("mode-switch.default-mode", "plugin");
        return true;
    }

    /**
     * Resolves the effective player mode. Disabling vanilla fishing always
     * forces plugin mode; otherwise a runtime override wins over the default.
     */
    public static boolean resolve(boolean vanillaFishingDisabled, Boolean runtimeOverride,
                                  String configuredDefaultMode) {
        if (vanillaFishingDisabled) {
            return false;
        }
        if (runtimeOverride != null) {
            return runtimeOverride;
        }
        return isVanillaDefault(configuredDefaultMode);
    }
}
