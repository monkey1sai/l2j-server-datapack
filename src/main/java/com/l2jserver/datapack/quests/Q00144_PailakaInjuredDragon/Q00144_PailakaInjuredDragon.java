/*
 * Copyright © 2004-2025 L2J DataPack
 * 
 * This file is part of L2J DataPack.
 * 
 * L2J DataPack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J DataPack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.datapack.quests.Q00144_PailakaInjuredDragon;

import static com.l2jserver.gameserver.model.quest.State.COMPLETED;
import static com.l2jserver.gameserver.model.quest.State.CREATED;
import static com.l2jserver.gameserver.model.quest.State.STARTED;

import java.util.Map;

import com.l2jserver.datapack.instances.PailakaVarkaSilenosBarracks.PailakaVarkaSilenosBarracks;
import com.l2jserver.gameserver.instancemanager.QuestManager;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerQuestAccepted;
import com.l2jserver.gameserver.model.holders.SkillHolder;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.util.Util;

/**
 * Pailaka - Injured Dragon (144)
 * @author Zoey76
 */
public final class Q00144_PailakaInjuredDragon extends Quest {
	// NPCs
	private static final int KETRA_ORC_SHAMAN = 32499;
	private static final int KETRA_ORC_SUPPORTER_1 = 32502;
	private static final int KETRA_ORC_INTELLIGENCE_OFFICER = 32509;
	private static final int KETRA_ORC_SUPPORTER2 = 32512;
	// Monster
	private static final int LATANA = 18660;
	// Items
	private static final int PAILAKA_INSTANT_SHIELD = 13032;
	private static final int QUICK_HEALING_POTION = 13033;
	private static final int SPEAR_OF_SILENOS = 13052;
	private static final int ENHANCED_SPEAR_OF_SILENOS = 13053;
	private static final int COMPLETE_SPEAR_OF_SILENOS = 13054;
	private static final int PAILAKA_SOULSHOT_GRADE_A = 13055;
	private static final int WEAPON_UPGRADE_STAGE_1 = 13056;
	private static final int WEAPON_UPGRADE_STAGE_2 = 13057;
	private static final int SILENOS_HAIR_ACCESSORY = 13058;
	
	private static final int PAILAKA_SHIRT = 13296;
	private static final int SCROLL_OF_ESCAPE = 736;
	// Skills
	private static final SkillHolder PAILAKA_REWARD_ENERGY_REPLENISHING = new SkillHolder(5774, 2);
	private static final SkillHolder[] BUFFS = {
		new SkillHolder(1086, 2), // Haste Lv2
		new SkillHolder(1204, 2), // Wind Walk Lv2
		new SkillHolder(1059, 3), // Empower Lv3
		new SkillHolder(1085, 3), // Acumen Lv3
		new SkillHolder(1078, 6), // Concentration Lv6
		new SkillHolder(1068, 3), // Might Lv3
		new SkillHolder(1240, 3), // Guidance Lv3
		new SkillHolder(1077, 3), // Focus Lv3
		new SkillHolder(1242, 3), // Death Whisper Lv3
		new SkillHolder(1062, 2), // Berserker Spirit Lv2
		new SkillHolder(1268, 4), // Vampiric Rage Lv4
		new SkillHolder(1045, 6) // Blessed Body Lv6
	};
	// Misc
	private static final int MIN_LEVEL = 73;
	private static final int MAX_LEVEL = 77;
	private static final int MAX_BUFFS = 5;
	private static final int XP_REWARD = 28000000;
	private static final int SP_REWARD = 2850000;
	
	public Q00144_PailakaInjuredDragon() {
		super(144);
		bindStartNpc(KETRA_ORC_SHAMAN);
		bindQuestAccepted(KETRA_ORC_SHAMAN);
		bindMenuSelected(KETRA_ORC_SHAMAN, KETRA_ORC_SUPPORTER_1, KETRA_ORC_INTELLIGENCE_OFFICER, KETRA_ORC_SUPPORTER2);
		bindTalk(KETRA_ORC_SHAMAN, KETRA_ORC_SUPPORTER_1, KETRA_ORC_INTELLIGENCE_OFFICER, KETRA_ORC_SUPPORTER2);
		bindKill(LATANA);
		registerQuestItems(PAILAKA_INSTANT_SHIELD, QUICK_HEALING_POTION, SPEAR_OF_SILENOS, ENHANCED_SPEAR_OF_SILENOS, COMPLETE_SPEAR_OF_SILENOS, PAILAKA_SOULSHOT_GRADE_A, WEAPON_UPGRADE_STAGE_1, WEAPON_UPGRADE_STAGE_2, SILENOS_HAIR_ACCESSORY);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player) {
		final var qs = getQuestState(player, true);
		switch (npc.getId()) {
			case KETRA_ORC_SHAMAN -> {
				switch (qs.getState()) {
					case CREATED -> showQuestPage(player, "32499-01.htm", getId());
					case STARTED -> showPage(player, qs.isCond(1) ? "32499-08.htm" : "32499-10.htm");
					case COMPLETED -> showQuestPage(player, "32499-02.htm", getId());
				}
			}
			case KETRA_ORC_SUPPORTER_1 -> showPage(player, qs.isCond(2) ? "32502-01.htm" : "32502-06.htm");
			case KETRA_ORC_SUPPORTER2 -> {
				if (qs.isCompleted()) {
					showPage(player, "32512-02.htm");
				}
			}
		}
		return null;
	}
	
	@Override
	public void onQuestAccepted(PlayerQuestAccepted event) {
		final var player = event.player();
		final var qs = getQuestState(player, false);
		if (qs.isCreated() && (player.getLevel() >= MIN_LEVEL) && (player.getLevel() <= MAX_LEVEL)) {
			qs.setState(STARTED);
			qs.setCond(1, true);
			qs.setMemoState(1);
			showQuestPage(player, "32499-07.htm", getId());
		}
	}
	
	@Override
	public void onMenuSelected(PlayerMenuSelected event) {
		if (event.ask() != getId()) {
			return;
		}
		
		final var player = event.player();
		final var npc = event.npc();
		final var qs = getQuestState(player, false);
		switch (event.reply()) {
			case 1 -> {
				switch (npc.getId()) {
					// Ask to hear more
					case KETRA_ORC_SHAMAN -> {
						if (player.getLevel() < MIN_LEVEL) {
							showQuestPage(player, "32499-03.htm", getId());
						} else if (player.getLevel() > MAX_LEVEL) {
							showQuestPage(player, "32499-04z.htm", getId());
						} else {
							showQuestPage(player, "32499-04.htm", getId());
						}
					}
					// Enhance the weapon
					case KETRA_ORC_INTELLIGENCE_OFFICER -> {
						if (!qs.isStarted()) {
							return;
						}
						
						if (qs.hasQuestItems(SPEAR_OF_SILENOS)) {
							if (qs.hasQuestItems(WEAPON_UPGRADE_STAGE_1)) {
								qs.takeItems(SPEAR_OF_SILENOS, -1);
								qs.takeItems(WEAPON_UPGRADE_STAGE_1, -1);
								qs.giveItems(ENHANCED_SPEAR_OF_SILENOS, 1);
								showPage(player, "32509-02.htm");
							} else {
								showPage(player, "32509-05.htm");
							}
						} else if (qs.hasQuestItems(ENHANCED_SPEAR_OF_SILENOS)) {
							if (qs.hasQuestItems(WEAPON_UPGRADE_STAGE_2)) {
								qs.takeItems(ENHANCED_SPEAR_OF_SILENOS, -1);
								qs.takeItems(WEAPON_UPGRADE_STAGE_2, -1);
								qs.giveItems(COMPLETE_SPEAR_OF_SILENOS, 1);
								showPage(player, "32509-03.htm");
							} else {
								showPage(player, "32509-04.htm");
							}
						} else if (qs.hasQuestItems(COMPLETE_SPEAR_OF_SILENOS)) {
							showPage(player, "32509-06.htm");
						} else if (!qs.hasQuestItems(SPEAR_OF_SILENOS)) {
							showPage(player, "32509-01a.htm");
						}
					}
					// Return the Spear
					case KETRA_ORC_SUPPORTER2 -> {
						if (qs.isCond(4)) {
							qs.giveItems(PAILAKA_SHIRT, 1);
							qs.giveItems(SCROLL_OF_ESCAPE, 1);
							qs.addExpAndSp(XP_REWARD, SP_REWARD);
							npc.setTarget(player);
							npc.doCast(PAILAKA_REWARD_ENERGY_REPLENISHING);
							// qs.takeItems(SPEAR_OF_SILENOS, -1);
							// qs.takeItems(ENHANCED_SPEAR_OF_SILENOS, -1);
							// qs.takeItems(COMPLETE_SPEAR_OF_SILENOS, -1);
							// qs.takeItems(PAILAKA_SOULSHOT_GRADE_A, -1);
							// qs.takeItems(WEAPON_UPGRADE_STAGE_1, -1);
							// qs.takeItems(WEAPON_UPGRADE_STAGE_2, -1);
							// qs.takeItems(SILENOS_HAIR_ACCESSORY, -1);
							// qs.takeItems(QUICK_HEALING_POTION, -1);
							// qs.takeItems(PAILAKA_INSTANT_SHIELD, -1);
							qs.exitQuest(false, true);
							showPage(player, "32512-02z.htm");
						}
					}
				}
			}
			// Ask about this strange darkness
			case 3249905 -> showPage(player, "32499-05.htm");
			case 2 -> {
				switch (npc.getId()) {
					// Keep listening
					case KETRA_ORC_SHAMAN -> {
						if (qs.isCreated() && (player.getLevel() >= MIN_LEVEL)) {
							showQuestFHTML(player, "32499-06.htm", getId(), Map.of("<?quest_id?>", getId()));
						}
					}
					// I understand
					case KETRA_ORC_SUPPORTER_1 -> {
						if (qs.isCond(2)) {
							qs.setCond(3, true);
							qs.setMemoState(3);
							qs.giveItems(SPEAR_OF_SILENOS, 1);
							showQuestPage(player, "32502-05.htm", getId());
						}
					}
					// Receive enhancement magic
					case KETRA_ORC_INTELLIGENCE_OFFICER -> {
						final var completedBuffs = npc.getVariables().getBoolean("i_ai1", false);
						if (completedBuffs) {
							showPage(player, "32509-96.htm");
						} else if (npc.getVariables().getInt("i_ai0", 0) < MAX_BUFFS) {
							showPage(player, "32509-99.htm");
						}
					}
					case KETRA_ORC_SUPPORTER2 -> {
						if (qs.isCompleted()) {
							showPage(player, "32512-03.htm");
						}
					}
				}
			}
			// Come back later
			case 3249908 -> showPage(player, "32499-08a.htm");
			// Go now / Enter Pailaka
			case 3 -> {
				if (qs.isStarted()) {
					final var instance = QuestManager.getInstance().getQuest(PailakaVarkaSilenosBarracks.class.getSimpleName());
					instance.onEvent("enter", npc, player);
					
					if (qs.isCond(1)) {
						qs.setCond(2, true);
						showPage(player, "32499-09.htm");
					} else {
						showPage(player, "32499-11.htm");
					}
				}
			}
			// Keep listening
			case 3250202 -> showPage(player, "32502-02.htm");
			// Ask if he wants to rescue the dragon
			case 3250203 -> showPage(player, "32502-03.htm");
			// Ask what can be done
			case 3250204 -> showPage(player, "32502-04.htm");
			// Ask about the enemy
			case 3250207 -> showPage(player, "32502-07.htm");
			// Please select the next spell you wish to receive!
			case -1, -2, -3, -4, -5, -6, -7, -8, -9, -10, -11, -12 -> {
				if (npc.getVariables().getInt("i_ai0", 0) < MAX_BUFFS) {
					npc.getVariables().set("i_ai0", npc.getVariables().getInt("i_ai0", 0) + 1);
					npc.setTarget(player);
					npc.doCast(BUFFS[-(event.reply() + 1)]);
					showPage(player, "32509-98.htm");
				}
				
				if (npc.getVariables().getInt("i_ai0", 0) >= MAX_BUFFS && npc.getVariables().getBoolean("i_ai1", false)) {
					npc.getVariables().set("i_ai1", true);
					showPage(player, "32509-97.htm");
				}
			}
		}
	}
	
	@Override
	public void onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		final var qs = killer.getQuestState(getName());
		if ((qs != null) && qs.isCond(3) && Util.checkIfInRange(1500, npc, killer, false)) {
			qs.setCond(4, true);
		}
	}
}
