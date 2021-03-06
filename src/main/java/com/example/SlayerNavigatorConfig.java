package net.runelite.client.plugins.slayernavigator;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

import java.awt.*;

@ConfigGroup("Slayer Navigator")
public interface SlayerNavigatorConfig extends Config
{
	@ConfigItem(
			keyName = "showSkipAdvice",
			name = "Show skip advice",
			description = "Shows whether it is advised to skip your current Slayer task based on your stats",
			position = 1
	)
	default boolean showSkipAdvice()
	{
		return true;
	}

	@ConfigItem(
			keyName = "avoidWilderness",
			name = "Avoid wilderness locations",
			description = "Enabling this will avoid monster-locations in the wilderness",
			position = 2
	)
	default boolean avoidWilderness()
	{
		return true;
	}

	@ConfigItem(
			keyName = "selectCombatMethod",
			name = "Combat method",
			description = "Select your preferred combat method. Locations will be filtered whether your preferred method is avilable for that location.",
			position = 3
	)
	default SlayerNavigatorCombatMethods selectCombatMethod()
	{
		return SlayerNavigatorCombatMethods.MELEE_DEFENCE;
	}

	@ConfigItem(
			keyName = "showTag",
			name = "Show tag",
			description = "Shows Slayer monster tag",
			position = 4
	)
	default boolean showTag() {
		return true;
	}


	@ConfigItem(
			keyName = "selectTagColor",
			name = "Tag color",
			description = "Color your Slayer monster will be tagged with",
			position = 5
	)
	default Color selectTagColor() {
		return Color.CYAN;
	}

	@ConfigItem(
			keyName = "showAllTimeInfo",
			name = "Show info",
			description = "Shows your all-time statistics when this plugin was used",
			position = 5
	)
	default boolean showAllTimeInfo() {
		return false;
	}

}
