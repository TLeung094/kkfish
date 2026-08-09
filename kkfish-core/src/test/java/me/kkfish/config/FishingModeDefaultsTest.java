package me.kkfish.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FishingModeDefaultsTest {

    @Test
    void missingAndInvalidModesFallBackToPlugin() {
        assertEquals("plugin", FishingModeDefaults.normalize(null));
        assertEquals("plugin", FishingModeDefaults.normalize(""));
        assertEquals("plugin", FishingModeDefaults.normalize("unknown"));
        assertEquals("plugin", FishingModeDefaults.normalize("plugin"));
        assertFalse(FishingModeDefaults.isVanillaDefault(null));
        assertFalse(FishingModeDefaults.isVanillaDefault("unknown"));
    }

    @Test
    void vanillaModeIsCaseInsensitive() {
        assertTrue(FishingModeDefaults.isVanillaDefault("vanilla"));
        assertTrue(FishingModeDefaults.isVanillaDefault("VANILLA"));
        assertTrue(FishingModeDefaults.isVanillaDefault(" Vanilla "));
    }

    @Test
    void migrationAddsPluginDefaultWithoutChangingExistingServers() {
        YamlConfiguration config = new YamlConfiguration();
        assertTrue(FishingModeDefaults.ensureDefaultMode(config));
        assertEquals("plugin", config.getString("mode-switch.default-mode"));
        assertFalse(FishingModeDefaults.ensureDefaultMode(config));
    }

    @Test
    void migrationPreservesExplicitVanillaDefault() {
        YamlConfiguration config = new YamlConfiguration();
        config.set("mode-switch.default-mode", "vanilla");
        assertFalse(FishingModeDefaults.ensureDefaultMode(config));
        assertEquals("vanilla", config.getString("mode-switch.default-mode"));
    }

    @Test
    void unoverriddenPlayerInheritsVanillaDefault() {
        assertTrue(FishingModeDefaults.resolve(false, null, "vanilla"));
    }

    @Test
    void runtimeOverrideTakesPrecedence() {
        assertFalse(FishingModeDefaults.resolve(false, Boolean.FALSE, "vanilla"));
        assertTrue(FishingModeDefaults.resolve(false, Boolean.TRUE, "plugin"));
    }

    @Test
    void disablingVanillaFishingForcesPluginMode() {
        assertFalse(FishingModeDefaults.resolve(true, Boolean.TRUE, "vanilla"));
        assertFalse(FishingModeDefaults.resolve(true, null, "vanilla"));
    }

    @Test
    void togglingInheritedVanillaDefaultCreatesPluginOverride() {
        boolean inheritedMode = FishingModeDefaults.resolve(false, null, "vanilla");
        boolean override = !inheritedMode;
        assertFalse(FishingModeDefaults.resolve(false, override, "vanilla"));
    }
}
