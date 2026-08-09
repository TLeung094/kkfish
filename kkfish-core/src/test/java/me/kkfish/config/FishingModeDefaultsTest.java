package me.kkfish.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FishingModeDefaultsTest {

    @Test
    void missingAndInvalidModesFallBackToPlugin() {
        assertFalse(FishingModeDefaults.isVanillaDefault(null));
        assertFalse(FishingModeDefaults.isVanillaDefault(""));
        assertFalse(FishingModeDefaults.isVanillaDefault("unknown"));
        assertFalse(FishingModeDefaults.isVanillaDefault("plugin"));
    }

    @Test
    void vanillaModeIsCaseInsensitive() {
        assertTrue(FishingModeDefaults.isVanillaDefault("vanilla"));
        assertTrue(FishingModeDefaults.isVanillaDefault("VANILLA"));
        assertTrue(FishingModeDefaults.isVanillaDefault(" Vanilla "));
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
