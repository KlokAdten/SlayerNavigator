package net.runelite.client.plugins.slayernavigator;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.chat.ChatClient;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.npcoverlay.HighlightedNpc;
import net.runelite.client.game.npcoverlay.NpcOverlayService;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.ColorUtil;
import net.runelite.http.api.chat.Task;

import java.awt.*;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@Slf4j
@PluginDescriptor(
	name = "Slayer Navigator"
)
public class SlayerNavigatorPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ChatClient chatClient;

	@Inject
	private SlayerNavigatorConfig config;

	@Inject
	private ItemManager itemManager;

	@Inject
	private NpcOverlayService npcOverlayService;

	//Fields
	net.runelite.http.api.chat.Task currentTask;
	private int totalTasksCompleted;
	private int totalMonstersSlayed;
	private List<NPC> targets = new ArrayList<>();
	private ArrayList monsterIds;

	private boolean taskCompletedConfirmed;


	@Override
	protected void startUp() throws Exception
			// TODO: whenever a slayer task is finished, our statistics should be updated
			// When sending data to our own server: https://github.com/runelite/plugin-hub/pull/2308/files#diff-c8736103d0c2e88bd584a8f286df6c92d90a9634b1af95802add104c22d78568R3
	{
		log.info("SlayerNavigator started!");

		try
		{
			currentTask = getSlayerTask();
		}
		catch (IOException ex)
		{
			log.debug("Failed to lookup slayer task", ex);
			// TODO: give message to user
			return;
		}


		npcOverlayService.registerHighlighter(isTarget);
//		TODO: fill in monsterIDs. query from db

	}

	// Adapted from SlayerPugin.java. This will mark the current monster of the current slayer task
	public final Function<NPC, HighlightedNpc> isTarget = (n) ->
	{

		Color color = config.selectTagColor();
		return HighlightedNpc.builder()
				.npc(n)
				.highlightColor(color)
				.fillColor(ColorUtil.colorWithAlpha(color, color.getAlpha() / 12))
				.hull(config.showTag())
				.build();
//		return null;
	};

	@Override
	protected void shutDown() throws Exception
	{
		npcOverlayService.unregisterHighlighter(isTarget);
		log.info("SlayerNavigator stopped!");
	}

	private Task getSlayerTask() throws IOException {
		return chatClient.getTask(client.getLocalPlayer().getName());
	}

	private void drawGraphics(){ // or something to do with overlay

		if (config.showAllTimeInfo()){
			log.debug("TODO: Show all time info");
			// use totalMonstersSlayed;
			// use totalTasksCompleted;
		}

		if (taskCompletedConfirmed){
			// Fetch preferred slayer master (or advised)
			// Check if our completed slayer task count is an important one. If yes, noify the user and give the highest available slayer master
			// hide taskInfoWidget
			// show taskCompletedWidget

			log.debug("TODO: Draw taskCompletedConfirmed");
		}
		// another else: we do currently not have a slayer task

		else { // we are on a slayer task
			// get image of monster task
			// get number of monsters left:
			int monstersLeft = currentTask.getAmount();

		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
//			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Example says " + config.showSkipAdvice(), null);
			log.debug(Arrays.toString(getSkillStatistics(Skill.SLAYER)));
		}
	}

	@Provides
	SlayerNavigatorConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(SlayerNavigatorConfig.class);
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged ev)
	{
		if (ev.getContainerId() == InventoryID.BANK.getId())
		{
			log.info("Parsing bank items");
			Item[] bankItems = getBankItems();
			log.info(Arrays.toString(bankItems));
			// now, mark them
			for (Item i : bankItems){
				if (i.getId() == 2355){
					// TODO: maybe use WidgetItemOverlay?
//					SpritePixels pixels = client.createItemSprite(i.getId(), 1, true, 1, 0, false, 1);
//					pixels.drawAt();
//					client.getItemSpriteCache();
					log.info("We are at a silver bar. Should draw");
				}
			}


//			https://static.runelite.net/api/runelite-api/net/runelite/api/SpritePixels.html#drawAt(int,int)
		}
	}

	@Subscribe
	public void onNpcSpawned(NpcSpawned npcSpawned)
	{
		NPC npc = npcSpawned.getNpc();
		if (isTargetMonster(npc))
		{
			targets.add(npc);
		}
	}

	private boolean isTargetMonster(NPC npc) {
		return monsterIds.contains(npc.getId());
	}

	@Subscribe
	public void onNpcDespawned(NpcDespawned npcDespawned)
	{
		NPC monster = npcDespawned.getNpc();
		if (isTargetMonster(monster)){
			currentTask.setAmount(currentTask.getAmount() - 1);
			totalMonstersSlayed += 1;
			// graphic would be automatically updated as we should call currentTask.getAmount() there
		}

		if (currentTask.getAmount() == 0){


			if (taskCompletedConfirmed){
				totalTasksCompleted += 1;

			}
		}
	}


	private void tagSlayerTaskMonster(){
		// To tag monsters: HighlightedNpc.java
		// Use NpcOverlayService

		// FROM SlayerPlugin.java:
//		chatCommandManager.registerCommandAsync(TASK_COMMAND_STRING, this::taskLookup, this::taskSubmit);
//		npcOverlayService.registerHighlighter(isTarget);






		//EXAMPLE
//		Color color = config.getTargetColor();
//		return HighlightedNpc.builder()
//				.npc(n)
//				.highlightColor(color)
//				.fillColor(ColorUtil.colorWithAlpha(color, color.getAlpha() / 12))
//				.hull(config.highlightHull())
//				.tile(config.highlightTile())
//				.outline(config.highlightOutline())
//				.build();
//
//	}

//		https://static.runelite.net/api/runelite-api/net/runelite/api/Actor.html#getConvexHull()
//		https://static.runelite.net/api/runelite-api/net/runelite/api/model/Jarvis.html
	}

	private int[] getSkillStatistics(Skill skill){
		// get level

//		https://static.runelite.net/api/runelite-api/net/runelite/api/Client.html#getRealSkillLevel(net.runelite.api.Skill)
//		parameter: https://static.runelite.net/api/runelite-api/net/runelite/api/Skill.html#SLAYER

		// get XP
//		https://static.runelite.net/api/runelite-api/net/runelite/api/Client.html#getSkillExperience(net.runelite.api.Skill)
//		parameter: https://static.runelite.net/api/runelite-api/net/runelite/api/Skill.html#SLAYER

		return new int[] {client.getRealSkillLevel(skill), client.getSkillExperience(skill)}; // {level, XP}

	}

	public void showBestGear(){
		// Get bank inventory
		Item[] items = getBankItems();
//		https://static.runelite.net/api/runelite-api/net/runelite/api/InventoryID.html#BANK
//		https://static.runelite.net/api/runelite-api/net/runelite/api/ItemContainer.html#getItems()

		// create switch case statement for which mode we are using

		// query corresponding database, and check against bank.
		// mark per category (i.e. helmet, legs) the top choice in your bank (if your bank is open). Also display list of best items to wear
	}

	private Item[] getBankItems() {
		// Only works if the bank is currently open. Should be called when opening bank
		ItemContainer bankContents = client.getItemContainer(InventoryID.BANK);
		if (bankContents != null){
			return bankContents.getItems();
		}
		else {
			log.error("Bank is unopened");
			return null;
		}

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



