package net.runelite.client.plugins.slayernavigator;


public enum SlayerNavigatorCombatMethods {
    MELEE_PRAYER("Melee - Prayer"),
    MELEE_DEFENCE("Melee - Defence"),
    MAGIC_BURST("Magic - Burst"),
    MAGIC_SAFESPOT("Magic - Safespot"),
    RANGED_SAFESPOT("Ranged - Safespot");
//
//    * Melee - Prayer
//    * Melee - Defence
//    * Magic - Burst
//    * Magic - Safespot
//    * Ranged - Safespot

    private final String name;

    SlayerNavigatorCombatMethods(String name) {
        this.name = name;
    }

}
