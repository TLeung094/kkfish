package me.kkfish.config;

/**
 * Resolves the effective fishing mode from the server default and an optional
 * per-player runtime override.
 */
public final class FishingModeDefaults {

    private FishingModeDefaults() {
    }

    /**
     * Returns true only when the configured mode is "vanilla". Missing and
     * invalid values intentionally fall back to plugin mode for compatibility.
     */
    public static boolean isVanillaDefault(String configuredMode) {
        return configuredMode != null && "vanilla".equalsIgnoreCase(configuredMode.trim());
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
