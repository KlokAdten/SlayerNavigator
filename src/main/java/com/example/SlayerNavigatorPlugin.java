package net.runelite.client.plugins.slayernavigator;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Skill;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "Slayer Navigator"
)
public class SlayerNavigatorPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private SlayerNavigatorConfig config;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Example started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Example stopped!");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Example says " + config.showSkipAdvice(), null);
		}
	}

	@Provides
	SlayerNavigatorConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(SlayerNavigatorConfig.class);
	}

	public int[] getSkillStatistics(Skill skill){
		// get level
//		https://static.runelite.net/api/runelite-api/net/runelite/api/Client.html#getRealSkillLevel(net.runelite.api.Skill)
//		parameter: https://static.runelite.net/api/runelite-api/net/runelite/api/Skill.html#SLAYER

		// get XP

//		https://static.runelite.net/api/runelite-api/net/runelite/api/Client.html#getSkillExperience(net.runelite.api.Skill)
//		parameter: https://static.runelite.net/api/runelite-api/net/runelite/api/Skill.html#SLAYER

		return new int[] {0, 0}; // {level, XP}

	}

	public void showBestGear(){
		// Get bank inventory
//		https://static.runelite.net/api/runelite-api/net/runelite/api/InventoryID.html#BANK
//		https://static.runelite.net/api/runelite-api/net/runelite/api/ItemContainer.html#getItems()

		// create switch case statement for which mode we are using

		// query corresponding database, and check against bank.
		// mark per category (i.e. helmet, legs) the top choice in your bank (if your bank is open). Also display list of best items to wear
	}

	public void navigateToMonster(String monster){
//		String dbFilter = "";
		StringBuilder query = new StringBuilder("select locations from monsters where name=%s"); // bijvoorbeeld

		// get if wilderness is enabled
		if (config.avoidWilderness()){
			query.append(" and where location != wilderness");
		}

		// filtering for locations which allow the given combat method

		String finalQuery = String.format(query.toString(), monster)
		// query db for monster locations (filter for wilderness)


		// get if teleports are enabled.
		// if yes, check which teleports are available (which magic level, which spellbook)

		;
	}

	public void getSlayerTaskProgress(){
		// is probably the first thing we do after our plugin has started

		// get slayer task and the progress
		// display possible locations. enable user to select a location
		//
		;
	}

	public void createSkipAdvice(){
		if (config.showSkipAdvice()) {
			// calculate skip advice
			// we need a requirements table per slayer task
			;
		}
		else {
			;
			// remove display
		}
		;
	}
}
