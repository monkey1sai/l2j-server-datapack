/*
 * Copyright © 2004-2023 L2J DataPack
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
package com.l2jserver.datapack.ai.npc.FestivalGuide;

import static com.l2jserver.gameserver.config.Configuration.sevenSigns;
import static com.l2jserver.gameserver.network.SystemMessageId.YOU_CAN_PROCEED_ONLY_WHEN_THE_INVENTORY_WEIGHT_IS_BELOW_80_PERCENT_AND_THE_QUANTITY_IS_BELOW_90_PERCENT;

import java.util.Calendar;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.gameserver.SevenSigns;
import com.l2jserver.gameserver.SevenSignsFestival;
import com.l2jserver.gameserver.enums.audio.Sound;
import com.l2jserver.gameserver.instancemanager.QuestManager;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.StatsSet;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerQuestAccepted;
import com.l2jserver.gameserver.model.holders.SkillHolder;
import com.l2jserver.gameserver.model.skills.Skill;
import com.l2jserver.gameserver.network.NpcStringId;
import com.l2jserver.gameserver.network.clientpackets.Say2;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * ssq_main_event_acolyte
 * @author Charus
 * @version 2.6.3.0
 */
public final class FestivalGuide extends AbstractNpcAI {
	
	// NPCs
	private static final int[] DAWN_NPCS = {
		31127,
		31128,
		31129,
		31130,
		31131
	};
	
	private static final int[] DUSK_NPCS = {
		31137,
		31138,
		31139,
		31140,
		31141
	};
	
	private static final SkillHolder SSQ_CANCEL = new SkillHolder(4334, 1);
	
	private static final int Q00255_TUTORIAL = 255;
	private static final int Q00505_BLOODOFFERING = 505;
	private static final int Q00635_INTOTHEDIMENSIONALRIFT = 635;
	
	private static final int BLOOD_OF_OFFERING = 5901;
	private static final int DIMENSIONAL_FRAGMENT = 7079;
	private static final int THE_STAFF = 214;
	
	private static final double WEIGHT_LIMIT = 0.80;
	private static final double QUEST_LIMIT = 0.90;
	private static final int MAX_QUEST_COUNT = 40;
	
	private static final int MAX_DISTANCE = 1500;
	
	private static SevenSigns _SS;
	private static SevenSignsFestival _SSF;
	
	public FestivalGuide() {
		super(FestivalGuide.class.getSimpleName(), "ai/npc");
		bindSpawn(DAWN_NPCS);
		bindSpawn(DUSK_NPCS);
		bindStartNpc(DAWN_NPCS);
		bindStartNpc(DUSK_NPCS);
		bindFirstTalk(DAWN_NPCS);
		bindFirstTalk(DUSK_NPCS);
		bindSpellFinished(DAWN_NPCS);
		bindSpellFinished(DUSK_NPCS);
		bindMenuSelected(DAWN_NPCS);
		bindMenuSelected(DUSK_NPCS);
		bindQuestAccepted(DAWN_NPCS);
		bindQuestAccepted(DUSK_NPCS);
		
		_SS = SevenSigns.getInstance();
		_SSF = SevenSignsFestival.getInstance();
	}
	
	@Override
	public String onSpawn(L2Npc npc) {
		npc.setScriptValue("i_ai0", 0);
		startQuestTimer("3001", 1000, npc, null);
		
		return super.onSpawn(npc);
	}
	
	@Override
	public String onTimerEvent(String timer, L2Npc npc, L2PcInstance player) {
		if (timer.equalsIgnoreCase("3001")) {
			final var shoutSysMsg = npc.getTemplate().getParameters().getInt("ShoutSysMsg", 0);
			final var sibylName = npc.getTemplate().getParameters().getString("SibylName", null);
			final var sibylSilhouette = npc.getTemplate().getParameters().getInt("SibylSilhouette", 0);
			
			final var sibylPosX = npc.getTemplate().getParameters().getInt("SibylPosX", 0);
			final var sibylPosY = npc.getTemplate().getParameters().getInt("SibylPosY", 0);
			final var sibylPosZ = npc.getTemplate().getParameters().getInt("SibylPosZ", 0);
			
			if (_SS.isCompetitionPeriod()) {
				final var i0 = Calendar.getInstance().get(Calendar.MINUTE);
				long i1 = _SS.getMilliToPeriodChange() / 1000;
				if ((i0 == 18 || i0 == 38 || i0 == 58) && i1 >= (18 * 60)) {
					if (npc.getScriptValue("i_ai0") < 1) {
						if (shoutSysMsg == 1) {
							broadcastNpcSay(npc, Say2.NPC_SHOUT, NpcStringId.THE_MAIN_EVENT_WILL_START_IN_2_MINUTES_PLEASE_REGISTER_NOW);
						}
						
						npc.setScriptValue("i_ai0", npc.getScriptValue("i_ai0") + 1);
					}
				} else {
					if ((i0 == 0 || i0 == 20 || i0 == 40) && i1 >= (18 * 60)) {
						if (npc.getScriptValue("i_ai0") < 1) {
							if (shoutSysMsg == 1) {
								broadcastNpcSay(npc, Say2.NPC_SHOUT, NpcStringId.THE_MAIN_EVENT_IS_NOW_STARTING);
							}
							
							npc.setScriptValue("i_ai0", npc.getScriptValue("i_ai0") + 1);
							
							if (sibylName != null) {
								addSpawnEx(sibylSilhouette, sibylPosX, sibylPosY, sibylPosZ, 0, false, 0, false, 0, 0, 0);
							}
						}
					} else {
						if (i0 == 13 || i0 == 33 || i0 == 53) {
							if (npc.getScriptValue("i_ai0") < 1) {
								if (shoutSysMsg == 1) {
									broadcastNpcSay(npc, Say2.NPC_SHOUT, NpcStringId.THE_MAIN_EVENT_WILL_CLOSE_IN_5_MINUTES);
								}
								
								npc.setScriptValue("i_ai0", npc.getScriptValue("i_ai0") + 1);
							}
						} else {
							if (i0 == 16 || i0 == 36 || i0 == 56) {
								if (npc.getScriptValue("i_ai0") < 1 && shoutSysMsg == 1) {
									if (i1 >= (20 * 60)) {
										broadcastNpcSay(npc, Say2.NPC_SHOUT, NpcStringId.THE_MAIN_EVENT_WILL_FINISH_IN_2_MINUTES_PLEASE_PREPARE_FOR_THE_NEXT_GAME);
									} else {
										broadcastNpcSay(npc, Say2.NPC_SHOUT, NpcStringId.THE_FESTIVAL_OF_DARKNESS_WILL_END_IN_TWO_MINUTES);
									}
									
									npc.setScriptValue("i_ai0", npc.getScriptValue("i_ai0") + 1);
								}
							} else {
								npc.setScriptValue("i_ai0", 0);
							}
						}
					}
				}
			}
			
			startQuestTimer("3001", 7000, npc, null);
		}
		
		return super.onTimerEvent(timer, npc, player);
	}
	
	@Override
	public void onMenuSelected(PlayerMenuSelected event) {
		final var player = event.player();
		final var npc = event.npc();
		
		final var ask = event.ask();
		final var reply = event.reply();
		
		final var roomIndex = npc.getTemplate().getParameters().getInt("RoomIndex", 0);
		final var partType = npc.getTemplate().getParameters().getString("part_type", null);
		final var ssqType = partType.equals("TWILIGHT") ? 1 : partType.equals("DAWN") ? 2 : 0;
		
		switch (ask) {
			case Q00505_BLOODOFFERING -> {
				if (player.getInventory().getSize(true) >= (player.getQuestInventoryLimit() * QUEST_LIMIT) || player.getInventory().getSize(false) >= (player.getInventoryLimit() * WEIGHT_LIMIT) || player.getCurrentLoad() >= (player.getMaxLoad() * WEIGHT_LIMIT)) {
					player.sendPacket(YOU_CAN_PROCEED_ONLY_WHEN_THE_INVENTORY_WEIGHT_IS_BELOW_80_PERCENT_AND_THE_QUANTITY_IS_BELOW_90_PERCENT);
					return;
				}
				
				switch (reply) {
					case 1 -> { // participate request
						final var cal = Calendar.getInstance();
						int i3 = cal.get(Calendar.MINUTE);
						if (!_SS.isCompetitionPeriod()) {
							showPage(player, "ssq_main_event_acolyte_q0505_02.htm");
							return;
						}
						
						if ((i3 >= 0 && i3 < 18) || (i3 >= 20 && i3 < 38) || (i3 >= 40 && i3 < 58) || (_SS.getMilliToPeriodChange() / 1000) <= 120) {
							showPage(player, "ssq_main_event_acolyte_q0505_22.htm");
							return;
						}
						
						showPage(player, getParticipateHtml(npc.getId()));
					}
					case 2 -> { // register score
						final var party0 = player.getParty();
						if (party0 != null) {
							final var q505 = QuestManager.getInstance().getQuest(Q00505_BLOODOFFERING);
							final var qs505 = q505.getQuestState(player, true);
							
							for (L2PcInstance i0 : party0.getMembers()) {
								final var q505i0 = QuestManager.getInstance().getQuest(Q00505_BLOODOFFERING);
								final var qs505i0 = q505i0.getQuestState(i0, true);
								
								if (qs505.getMemoState() != qs505i0.getMemoState()) {
									return;
								}
							}
						}
						
						if (_SS.getPlayerCabal(player.getObjectId()) != ssqType) {
							showPage(player, "ssq_main_event_acolyte_q0505_08.htm");
						} else {
							if (!_SS.isCompetitionPeriod()) {
								showPage(player, "ssq_main_event_acolyte_q0505_02.htm");
							} else {
								if (party0 == null || party0.getLeaderObjectId() != player.getObjectId()) {
									showPage(player, "ssq_main_event_acolyte_q0505_10.htm");
								} else {
									final var q505 = QuestManager.getInstance().getQuest(Q00505_BLOODOFFERING);
									final var qs505 = q505.getQuestState(player, true);
									if ((((System.currentTimeMillis() / 1000) - qs505.getMemoState()) / 60) > 40) {
										showPage(player, "ssq_main_event_acolyte_q0505_11.htm");
									} else {
										if (qs505.getMemoStateEx(1) != roomIndex - 1) {
											showPage(player, "ssq_main_event_acolyte_q0505_28.htm");
										} else {
											if (_SSF.getHighestScore(ssqType, roomIndex - 1) > getQuestItemsCount(player, BLOOD_OF_OFFERING)) {
												for (L2PcInstance pc : party0.getMembers()) {
													final var qs505pc = q505.getQuestState(pc, true);
													qs505pc.exitQuest(true, false);
												}
												
												long i2 = getQuestItemsCount(player, BLOOD_OF_OFFERING);
												switch (npc.getId()) {
													case 31127, 31128, 31129, 31130, 31137, 31138, 31139, 31140 -> {
														broadcastNpcSay(npc, Say2.SHOUT, NpcStringId.S1_HAS_WON_THE_MAIN_EVENT_FOR_PLAYERS_UNDER_LEVEL_S2_AND_EARNED_S3_POINTS, player.getName(), fStringLevel(npc.getId()), String.valueOf(i2));
													}
													case 31131, 31141 -> {
														broadcastNpcSay(npc, Say2.SHOUT, NpcStringId.S1_HAS_EARNED_S2_POINTS_IN_THE_MAIN_EVENT_FOR_UNLIMITED_LEVELS, player.getName(), String.valueOf(i2));
													}
												}
												
												if (i2 >= 3000) {
													showPage(player, "ssq_main_event_acolyte_q0505_12a.htm");
												} else {
													showPage(player, "ssq_main_event_acolyte_q0505_12.htm");
												}
												
												takeItems(player, BLOOD_OF_OFFERING, getQuestItemsCount(player, BLOOD_OF_OFFERING));
											} else {
												for (L2PcInstance pc : party0.getMembers()) {
													final var qs505pc = q505.getQuestState(pc, true);
													qs505pc.exitQuest(true, false);
												}
												
												long i2 = getQuestItemsCount(player, BLOOD_OF_OFFERING);
												switch (npc.getId()) {
													case 31127, 31128, 31129, 31130, 31137, 31138, 31139, 31140 -> {
														broadcastNpcSay(npc, Say2.SHOUT, NpcStringId.S1_HAS_WON_THE_MAIN_EVENT_FOR_PLAYERS_UNDER_LEVEL_S2_AND_EARNED_S3_POINTS, player.getName(), fStringLevel(npc.getId()), String.valueOf(i2));
													}
													case 31131, 31141 -> {
														broadcastNpcSay(npc, Say2.SHOUT, NpcStringId.S1_HAS_EARNED_S2_POINTS_IN_THE_MAIN_EVENT_FOR_UNLIMITED_LEVELS, player.getName(), String.valueOf(i2));
													}
												}
												
												if (i2 >= 3000) {
													showPage(player, "ssq_main_event_acolyte_q0505_13a.htm");
												} else {
													showPage(player, "ssq_main_event_acolyte_q0505_13.htm");
												}
												
												_SSF.addTimeAttackRecord(ssqType, roomIndex - 1, party0, i2);
												playSound(player, Sound.SKILLSOUND_JEWEL_CELEBRATE);
												takeItems(player, BLOOD_OF_OFFERING, getQuestItemsCount(player, BLOOD_OF_OFFERING));
											}
										}
									}
								}
							}
						}
					}
					case 3 -> { // high scores
						String html = getHtm(player.getHtmlPrefix(), "ssq_main_event_acolyte_q0505_14.htm");
						
						for (int i0 = 1; i0 < 4; i0++) {
							int i1 = 0;
							if (i0 == 1) {
								i1 = 2;
							} else {
								if (i0 == 2) {
									i1 = 1;
								} else {
									if (i0 == 3) {
										i1 = 0;
										final StatsSet s0 = _SSF.getOverallHighestScoreData(roomIndex - 1);
										if (s0 != null) {
											if (s0.getString("cabal").equals("dusk")) {
												html = html.replace("<?Winner?>", "Dusk");
											} else {
												if (s0.getString("cabal").equals("dawn")) {
													html = html.replace("<?Winner?>", "Dawn");
												}
											}
											
											long i2 = Long.valueOf(s0.getString("date"));
											if (i2 == 0) {
												html = html.replace("<?date" + i0 + "?>", "No record exists");
											} else {
												final var cal = Calendar.getInstance();
												cal.setTimeInMillis(i2);
												html = html.replace("<?date" + i0 + "?>", cal.get(Calendar.YEAR) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.DAY_OF_MONTH));
											}
											
											html = html.replace("<?score" + i0 + "?>", String.valueOf(s0.getInt("score")));
											
											String[] members = s0.getString("members").split(",");
											int count = members.length;
											if (count > 0) {
												for (int i = 1; i <= count; i++) {
													String comma = i != count ? ", " : "";
													String name = members[i - 1] + comma;
													html = html.replace("<?player" + i0 + "" + i + "?>", name);
												}
											}
											for (int i = count + 1; i < 10; i++) {
												html = html.replace("<?player" + i0 + "" + i + "?>", "");
											}
										} else {
											html = html.replace("<?Winner?>", "");
											html = html.replace("<?date" + i0 + "?>", "No record exists");
											html = html.replace("<?score" + i0 + "?>", String.valueOf(0));
											
											for (int i = 1; i < 10; i++) {
												html = html.replace("<?player" + i0 + "" + i + "?>", "");
											}
										}
									}
								}
							}
							
							if (i1 == 1 || i1 == 2) {
								final var s0 = _SSF.getHighestScoreData(i1, roomIndex - 1);
								long i2 = Long.valueOf(s0.getString("date"));
								if (i2 == 0) {
									html = html.replace("<?date" + i0 + "?>", "No record exists");
								} else {
									final var cal = Calendar.getInstance();
									cal.setTimeInMillis(i2);
									html = html.replace("<?date" + i0 + "?>", cal.get(Calendar.YEAR) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.DAY_OF_MONTH));
								}
								
								html = html.replace("<?score" + i0 + "?>", String.valueOf(s0.getInt("score")));
								
								String[] members = s0.getString("members").split(",");
								int count = members.length;
								if (count > 0) {
									for (int i = 1; i <= count; i++) {
										String comma = i != count ? ", " : "";
										String name = members[i - 1] + comma;
										html = html.replace("<?player" + i0 + "" + i + "?>", name);
									}
								}
								for (int i = count + 1; i < 10; i++) {
									html = html.replace("<?player" + i0 + "" + i + "?>", "");
								}
							}
						}
						
						player.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
					}
					case 4 -> { // festival status
						String html = getHtm(player.getHtmlPrefix(), "ssq_main_event_acolyte_q0505_15.htm");
						
						for (int i0 = 1; i0 < 6; i0++) {
							int i1 = 1000311 + i0;
							html = html.replace("<?Room" + i0 + "?>", fStringRoom(i1));
							
							int s0 = _SSF.getHighestScore(SevenSigns.CABAL_DUSK, i0 - 1);
							html = html.replace("<?Score" + i0 + "1?>", String.valueOf(s0));
							int s1 = _SSF.getHighestScore(SevenSigns.CABAL_DAWN, i0 - 1);
							html = html.replace("<?Score" + i0 + "2?>", String.valueOf(s1));
							
							if (s0 > s1) {
								html = html.replace("<?Winner" + i0 + "?>", "Dusk");
							} else {
								if (s0 < s1) {
									html = html.replace("<?Winner" + i0 + "?>", "Dawn");
								} else {
									html = html.replace("<?Winner" + i0 + "?>", "Draw");
								}
							}
						}
						
						player.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
					}
					case 6 -> { // what is my bonus
						if (!_SS.isSealValidationPeriod()) {
							showPage(player, "ssq_main_event_acolyte_q0505_23.htm");
							return;
						}
						if (_SS.getPlayerCabal(player.getObjectId()) != ssqType) {
							showPage(player, "ssq_main_event_acolyte_q0505_08.htm");
							return;
						}
						if (_SS.getPlayerCabal(player.getObjectId()) != _SS.getCabalHighestScore()) {
							showPage(player, "ssq_main_event_acolyte_q0505_24.htm");
							return;
						}
						if (!_SSF.isWinnerOfTimeAttackEvent(player, roomIndex - 1)) {
							showPage(player, "ssq_main_event_acolyte_q0505_17.htm");
							return;
						}
						if (_SSF.getTimeAttackRewardFlag(player, roomIndex - 1)) {
							showPage(player, "ssq_main_event_acolyte_q0505_19.htm");
							return;
						}
						
						final var i0 = _SSF.getHighestScoreData(_SS.getPlayerCabal(player.getObjectId()), roomIndex - 1);
						int i1 = _SSF.getAccumulatedBonus(roomIndex - 1) / i0.getString("members").split(",").length;
						_SSF.giveTimeAttackReward(player, roomIndex - 1, i1);
						player.setFame(player.getFame() + 1080);
						
						showPage(player, "ssq_main_event_acolyte_q0505_18.htm");
					}
					case 7 -> { // participate with blue stones
						final var party0 = player.getParty();
						if (party0 != null) {
							for (L2PcInstance c0 : party0.getMembers()) {
								if (_SS.getPlayerCabal(c0.getObjectId()) != ssqType) {
									String html = getHtm(player.getHtmlPrefix(), "ssq_main_event_acolyte_q0505_01.htm");
									html = html.replace("<?name?>", c0.getName());
									player.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
									return;
								}
								
								if (c0.getLevel() > getMinLevel(npc.getId())) {
									String html = getHtm(player.getHtmlPrefix(), fStringLevelHtml(npc.getId()));
									html = html.replace("<?name?>", c0.getName());
									player.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
									return;
								}
							}
						}
						
						if (!_SS.isCompetitionPeriod()) {
							showPage(player, "ssq_main_event_acolyte_q0505_02.htm");
						} else {
							if (party0 == null || party0.getLeaderObjectId() != player.getObjectId()) {
								showPage(player, "ssq_main_event_acolyte_q0505_03.htm");
							} else {
								final var q505 = QuestManager.getInstance().getQuest(Q00505_BLOODOFFERING);
								final var qs505 = q505.getQuestState(player, true);
								qs505.setMemoStateEx(1, getBlueStoneCount(npc.getId()));
								
								String html = getHtm(player.getHtmlPrefix(), "ssq_main_event_acolyte_q0505_06.htm");
								html = html.replace("<?quest_id?>", String.valueOf(Q00505_BLOODOFFERING));
								player.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
							}
						}
					}
					case 8 -> { // participate with green stones
						final var party0 = player.getParty();
						if (party0 != null) {
							for (L2PcInstance c0 : party0.getMembers()) {
								if (_SS.getPlayerCabal(c0.getObjectId()) != ssqType) {
									String html = getHtm(player.getHtmlPrefix(), "ssq_main_event_acolyte_q0505_01.htm");
									html = html.replace("<?name?>", c0.getName());
									player.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
									return;
								}
								
								if (c0.getLevel() > getMinLevel(npc.getId())) {
									String html = getHtm(player.getHtmlPrefix(), fStringLevelHtml(npc.getId()));
									html = html.replace("<?name?>", c0.getName());
									player.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
									return;
								}
							}
						}
						
						if (!_SS.isCompetitionPeriod()) {
							showPage(player, "ssq_main_event_acolyte_q0505_02.htm");
						} else {
							if (party0 == null || party0.getLeaderObjectId() != player.getObjectId()) {
								showPage(player, "ssq_main_event_acolyte_q0505_03.htm");
							} else {
								final var q505 = QuestManager.getInstance().getQuest(Q00505_BLOODOFFERING);
								final var qs505 = q505.getQuestState(player, true);
								qs505.setMemoStateEx(1, getGreenStoneCount(npc.getId()));
								
								String html = getHtm(player.getHtmlPrefix(), "ssq_main_event_acolyte_q0505_06.htm");
								html = html.replace("<?quest_id?>", String.valueOf(Q00505_BLOODOFFERING));
								player.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
							}
						}
					}
					case 9 -> { // participate with red stones
						final var party0 = player.getParty();
						if (party0 != null) {
							for (L2PcInstance c0 : party0.getMembers()) {
								if (_SS.getPlayerCabal(c0.getObjectId()) != ssqType) {
									String html = getHtm(player.getHtmlPrefix(), "ssq_main_event_acolyte_q0505_01.htm");
									html = html.replace("<?name?>", c0.getName());
									player.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
									return;
								}
								
								if (c0.getLevel() > getMinLevel(npc.getId())) {
									String html = getHtm(player.getHtmlPrefix(), fStringLevelHtml(npc.getId()));
									html = html.replace("<?name?>", c0.getName());
									player.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
									return;
								}
							}
						}
						
						if (!_SS.isCompetitionPeriod()) {
							showPage(player, "ssq_main_event_acolyte_q0505_02.htm");
						} else {
							if (party0 == null || party0.getLeaderObjectId() != player.getObjectId()) {
								showPage(player, "ssq_main_event_acolyte_q0505_03.htm");
							} else {
								final var q505 = QuestManager.getInstance().getQuest(Q00505_BLOODOFFERING);
								final var qs505 = q505.getQuestState(player, true);
								qs505.setMemoStateEx(1, getRedStoneCount(npc.getId()));
								
								String html = getHtm(player.getHtmlPrefix(), "ssq_main_event_acolyte_q0505_06.htm");
								html = html.replace("<?quest_id?>", String.valueOf(Q00505_BLOODOFFERING));
								player.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
							}
						}
					}
					case 10 -> { // back
						String html = getHtm(player.getHtmlPrefix(), fnHi(npc.getId()));
						
						final var cal = Calendar.getInstance();
						int i0 = cal.get(Calendar.MINUTE);
						if (i0 == 58 || i0 == 18 || i0 == 38 || i0 == 59 || i0 == 19 || i0 == 39) {
							html = html.replace("<?minute?>", "0 minute");
						} else {
							int i1;
							if (i0 > 39) {
								i1 = 58 - i0;
							} else {
								if (i0 > 19) {
									i1 = 38 - i0;
								} else {
									i1 = 18 - i0;
								}
							}
							html = html.replace("<?minute?>", i1 + " Minute");
						}
						
						player.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
					}
					case 11 -> { // accumulated bonus
						String html = getHtm(player.getHtmlPrefix(), "ssq_main_event_acolyte_q0505_27.htm");
						for (int i0 = 1; i0 < 6; i0++) {
							int i1 = 1000311 + i0;
							html = html.replace("<?Room" + i0 + "?>", fStringRoom(i1));
							i1 = _SSF.getAccumulatedBonus(i0 - 1);
							html = html.replace("<?Money" + i0 + "?>", String.valueOf(i1));
						}
						
						player.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
					}
					case 12 -> { // low score, teleport to dimensional rift
						long i3 = System.currentTimeMillis() / 1000;
						
						final var party0 = player.getParty();
						if (party0 != null) {
							int i2 = 0;
							
							for (L2PcInstance c1 : party0.getMembers()) {
								if (c1.getAllActiveQuests().size() > MAX_QUEST_COUNT && !haveMemo(c1, Q00635_INTOTHEDIMENSIONALRIFT)) {
									i2 = 0;
									
									String html = getHtm(player.getHtmlPrefix(), "ssq_main_event_acolyte_q0505_13e.htm");
									html = html.replace("<?name?>", c1.getName());
									player.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
								} else {
									i2 = 1;
								}
							}
							
							if (i2 == 1) {
								for (L2PcInstance c1 : party0.getMembers()) {
									final var q635 = QuestManager.getInstance().getQuest(Q00635_INTOTHEDIMENSIONALRIFT);
									final var qs635 = q635.getQuestState(c1, true);
									
									qs635.setMemoState(2);
									qs635.setMemoStateEx(1, i3);
									
									c1.teleToLocation(new Location(-114796, -179334, -6752), 1000);
								}
							}
							
							showPage(player, "ssq_main_event_acolyte_q0505_13b.htm");
						}
					}
					case 13 -> { // low score, stay at festival
						final var party0 = player.getParty();
						if (party0 != null) {
							for (L2PcInstance c1 : party0.getMembers()) {
								giveItems(c1, DIMENSIONAL_FRAGMENT, getFragmentsReward(npc.getId()));
							}
							
							showPage(player, "ssq_main_event_acolyte_q0505_13c.htm");
						}
					}
					case 14 -> { // dimensional rift
						player.teleToLocation(-114796, -179334, -6752);
						showPage(player, "ssq_main_event_acolyte_q0505_13d.htm");
					}
				}
			}
			case 507 -> {
				if (player.getInventory().getSize(true) >= (player.getQuestInventoryLimit() * QUEST_LIMIT) || player.getInventory().getSize(false) >= (player.getInventoryLimit() * WEIGHT_LIMIT) || player.getCurrentLoad() >= (player.getMaxLoad() * WEIGHT_LIMIT)) {
					player.sendPacket(YOU_CAN_PROCEED_ONLY_WHEN_THE_INVENTORY_WEIGHT_IS_BELOW_80_PERCENT_AND_THE_QUANTITY_IS_BELOW_90_PERCENT);
					return;
				}
				
				switch (reply) {
					case 2 -> { // return to village request
						showPage(player, "ssq_main_event_acolyte_q0507_28.htm");
					}
					case 3 -> { // return to village teleport
						if (haveMemo(player, Q00505_BLOODOFFERING)) {
							final var q505 = QuestManager.getInstance().getQuest(Q00505_BLOODOFFERING);
							final var qs505 = q505.getQuestState(player, true);
							qs505.exitQuest(true, false);
						}
						
						takeItems(player, BLOOD_OF_OFFERING, getQuestItemsCount(player, BLOOD_OF_OFFERING));
						
						final var q255 = QuestManager.getInstance().getQuest(Q00255_TUTORIAL);
						final var qs255 = q255.getQuestState(player, true);
						
						int i0 = qs255.getMemoStateEx(1);
						int i1 = i0 % 10000;
						int i2 = ((i0 - i1) + 5) / 10000;
						
						if (i1 >= 95 && i1 < 195) {
							if (partType.equalsIgnoreCase("DAWN")) {
								switch (getRandom(1)) {
									case 0 -> {
										player.teleToLocation(-80542, 150315, -3040);
									}
									case 1 -> {
										player.teleToLocation(-80602, 150352, -3040);
									}
								}
							} else {
								switch (getRandom(1)) {
									case 0 -> {
										player.teleToLocation(-82340, 151575, -3120);
									}
									case 1 -> {
										player.teleToLocation(-82392, 151584, -3120);
									}
								}
							}
							return;
						}
						if (i1 >= 195 && i1 < 295) {
							if (partType.equalsIgnoreCase("DAWN")) {
								switch (getRandom(1)) {
									case 0 -> {
										player.teleToLocation(-13996, 121413, -2984);
									}
									case 1 -> {
										player.teleToLocation(-13998, 121479, -2984);
									}
								}
							} else {
								switch (getRandom(1)) {
									case 0 -> {
										player.teleToLocation(-14727, 124002, -3112);
									}
									case 1 -> {
										player.teleToLocation(-14727, 124012, -3112);
									}
								}
							}
							return;
						}
						if (i1 >= 295 && i1 < 395) {
							if (partType.equalsIgnoreCase("DAWN")) {
								switch (getRandom(1)) {
									case 0 -> {
										player.teleToLocation(16320, 142915, -2696);
									}
									case 1 -> {
										player.teleToLocation(16383, 142899, -2696);
									}
								}
							} else {
								switch (getRandom(1)) {
									case 0 -> {
										player.teleToLocation(18501, 144673, -3056);
									}
									case 1 -> {
										player.teleToLocation(18523, 144624, -3056);
									}
								}
							}
							return;
						}
						if (i1 >= 395 && i1 < 495) {
							if (partType.equalsIgnoreCase("DAWN")) {
								switch (getRandom(1)) {
									case 0 -> {
										player.teleToLocation(83312, 149236, -3400);
									}
									case 1 -> {
										player.teleToLocation(83313, 149304, -3400);
									}
								}
							} else {
								switch (getRandom(1)) {
									case 0 -> {
										player.teleToLocation(81572, 148580, -3464);
									}
									case 1 -> {
										player.teleToLocation(81571, 148641, -3464);
									}
								}
							}
							return;
						}
						if (i1 >= 495 && i1 < 595) {
							if (partType.equalsIgnoreCase("DAWN")) {
								switch (getRandom(1)) {
									case 0 -> {
										player.teleToLocation(111359, 220959, -3544);
									}
									case 1 -> {
										player.teleToLocation(111411, 220955, -3544);
									}
								}
							} else {
								switch (getRandom(1)) {
									case 0 -> {
										player.teleToLocation(112441, 220149, -3544);
									}
									case 1 -> {
										player.teleToLocation(112452, 220204, -3592);
									}
								}
							}
							return;
						}
						if (i1 >= 595 && i1 < 695) {
							if (partType.equalsIgnoreCase("DAWN")) {
								switch (getRandom(1)) {
									case 0 -> {
										player.teleToLocation(83057, 53983, -1488);
									}
									case 1 -> {
										player.teleToLocation(83069, 54043, -1488);
									}
								}
							} else {
								switch (getRandom(1)) {
									case 0 -> {
										player.teleToLocation(82842, 54613, -1520);
									}
									case 1 -> {
										player.teleToLocation(82791, 54616, -1520);
									}
								}
							}
							return;
						}
						if (i1 >= 695 && i1 < 795) {
							if (partType.equalsIgnoreCase("DAWN")) {
								switch (getRandom(1)) {
									case 0 -> {
										player.teleToLocation(146955, 26690, -2200);
									}
									case 1 -> {
										player.teleToLocation(147015, 26689, -2200);
									}
								}
							} else {
								switch (getRandom(1)) {
									case 0 -> {
										player.teleToLocation(147528, 28899, -2264);
									}
									case 1 -> {
										player.teleToLocation(147528, 28962, -2264);
									}
								}
							}
							return;
						}
						if (i1 >= 795 && i1 < 895) {
							if (partType.equalsIgnoreCase("DAWN")) {
								switch (getRandom(1)) {
									case 0 -> {
										player.teleToLocation(115206, 74775, -2600);
									}
									case 1 -> {
										player.teleToLocation(115174, 74722, -2608);
									}
								}
							} else {
								switch (getRandom(1)) {
									case 0 -> {
										player.teleToLocation(116651, 77512, -2688);
									}
									case 1 -> {
										player.teleToLocation(116597, 77539, -2688);
									}
								}
							}
							return;
						}
						if (i1 >= 995 && i1 < 1095) {
							if (partType.equalsIgnoreCase("DAWN")) {
								player.teleToLocation(148326, -55533, -2776);
							} else {
								player.teleToLocation(149968, -56645, -2976);
							}
							return;
						}
						if (i1 >= 1095 && i1 < 1195) {
							if (partType.equalsIgnoreCase("DAWN")) {
								player.teleToLocation(45605, -50360, -792);
							} else {
								player.teleToLocation(44505, -48331, -792);
							}
							return;
						}
						if (i1 >= 1195 && i1 < 1295) {
							if (partType.equalsIgnoreCase("DAWN")) {
								player.teleToLocation(86730, -143148, -1336);
							} else {
								player.teleToLocation(85048, -142046, -1536);
							}
							return;
						}
						
						if (i2 == 1) {
							player.teleToLocation(-41443, 210030, -5080);
						} else {
							if (i2 == 2) {
								player.teleToLocation(-53034, -250421, -7935);
							} else {
								if (i2 == 3) {
									player.teleToLocation(45160, 123605, -5408);
								} else {
									if (i2 == 4) {
										player.teleToLocation(46488, 170184, -4976);
									} else {
										if (i2 == 5) {
											player.teleToLocation(111521, 173905, -5432);
										} else {
											if (i2 == 6) {
												player.teleToLocation(-20395, -250930, -8191);
											} else {
												if (i2 == 7) {
													player.teleToLocation(-21482, 77253, -5168);
												} else {
													if (i2 == 8) {
														player.teleToLocation(140688, 79565, -5424);
													} else {
														if (i2 == 9) {
															player.teleToLocation(-52007, 78986, -4736);
														} else {
															if (i2 == 10) {
																player.teleToLocation(118547, 132669, -4824);
															} else {
																if (i2 == 11) {
																	player.teleToLocation(172562, -17730, -4896);
																} else {
																	if (i2 == 12) {
																		player.teleToLocation(83344, 209110, -5432);
																	} else {
																		if (i2 == 13) {
																			player.teleToLocation(-19154, 13415, -4896);
																		} else {
																			if (i2 == 14) {
																				player.teleToLocation(12747, -248614, -9607);
																			} else {
																				if (i1 < 95) {
																					showPage(player, "ssq_main_event_acolyte_q0507_29.htm");
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	public void onQuestAccepted(PlayerQuestAccepted event) {
		final var player = event.player();
		final var npc = event.npc();
		
		final var questId = event.questId();
		
		final var roomIndex = npc.getTemplate().getParameters().getInt("RoomIndex", 0);
		final var partType = npc.getTemplate().getParameters().getString("part_type", null);
		final var ssqType = partType.equals("TWILIGHT") ? 1 : partType.equals("DAWN") ? 2 : 0;
		
		if (questId == Q00505_BLOODOFFERING) {
			if (player.getInventory().getSize(true) >= (player.getQuestInventoryLimit() * QUEST_LIMIT) || player.getInventory().getSize(false) >= (player.getInventoryLimit() * WEIGHT_LIMIT) || player.getCurrentLoad() >= (player.getMaxLoad() * WEIGHT_LIMIT)) {
				player.sendPacket(YOU_CAN_PROCEED_ONLY_WHEN_THE_INVENTORY_WEIGHT_IS_BELOW_80_PERCENT_AND_THE_QUANTITY_IS_BELOW_90_PERCENT);
				return;
			}
			
			final var party0 = player.getParty();
			if (party0 != null) {
				final var c0 = party0.getLeader();
				int i1 = party0.getMemberCount();
				
				for (L2PcInstance c1 : party0.getMembers()) {
					final var c2 = c1.getSummon();
					
					takeItems(c1, BLOOD_OF_OFFERING, getQuestItemsCount(c1, BLOOD_OF_OFFERING));
					
					if (_SS.getPlayerCabal(c1.getObjectId()) != ssqType) {
						final var q505 = QuestManager.getInstance().getQuest(Q00505_BLOODOFFERING);
						final var qs505 = q505.getQuestState(player, true);
						qs505.exitQuest(true, false);
						
						String html = getHtm(player.getHtmlPrefix(), "ssq_main_event_acolyte_q0505_01.htm");
						html = html.replace("<?name?>", c1.getName());
						player.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
						return;
					}
					
					if (c1.getLevel() > getMinLevel(npc.getId())) {
						final var q505 = QuestManager.getInstance().getQuest(Q00505_BLOODOFFERING);
						final var qs505 = q505.getQuestState(player, true);
						qs505.exitQuest(true, false);
						
						String html = getHtm(player.getHtmlPrefix(), fStringLevelHtml(npc.getId()));
						html = html.replace("<?name?>", c1.getName());
						player.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
						return;
					}
					
					if (c1.getAllActiveQuests().size() > (MAX_QUEST_COUNT - 1)) {
						final var q505 = QuestManager.getInstance().getQuest(Q00505_BLOODOFFERING);
						final var qs505 = q505.getQuestState(player, true);
						qs505.exitQuest(true, false);
						
						String html = getHtm(player.getHtmlPrefix(), "ssq_main_event_acolyte_q0505_04e.htm");
						html = html.replace("<?name?>", c1.getName());
						player.sendPacket(new NpcHtmlMessage(npc.getObjectId(), html));
						return;
					}
					
					if (c2 != null) {
						final var q505 = QuestManager.getInstance().getQuest(Q00505_BLOODOFFERING);
						final var qs505 = q505.getQuestState(player, true);
						qs505.exitQuest(true, false);
						
						showPage(player, "ssq_main_event_acolyte_q0505_31.htm");
						return;
					}
				}
				
				final var cal = Calendar.getInstance();
				int i0 = cal.get(Calendar.MINUTE);
				if ((i0 >= 0 && i0 < 18) || (i0 >= 20 && i0 < 38) || (i0 >= 40 && i0 < 58) || (_SS.getMilliToPeriodChange() / 1000) <= 120) {
					final var q505 = QuestManager.getInstance().getQuest(Q00505_BLOODOFFERING);
					final var qs505 = q505.getQuestState(player, true);
					qs505.exitQuest(true, false);
					
					showPage(player, "ssq_main_event_acolyte_q0505_22.htm");
					return;
				}
				
				long time = System.currentTimeMillis() / 1000;
				i0 = Math.toIntExact(time);
				if ((i1 < sevenSigns().getFestivalMinPlayer()) && !hasQuestItems(player, THE_STAFF)) {
					final var q505 = QuestManager.getInstance().getQuest(Q00505_BLOODOFFERING);
					final var qs505 = q505.getQuestState(player, true);
					qs505.exitQuest(true, false);
					
					showPage(player, "ssq_main_event_acolyte_q0505_30.htm");
					return;
				}
				
				if (player.getAllActiveQuests().size() < MAX_QUEST_COUNT) {
					if (_SS.getPlayerCabal(player.getObjectId()) == ssqType && _SS.isCompetitionPeriod() && c0.getObjectId() == player.getObjectId()) {
						final var q505 = QuestManager.getInstance().getQuest(Q00505_BLOODOFFERING);
						final var qs505 = q505.getQuestState(player, true);
						
						if (qs505.getMemoStateEx(1) == getBlueStoneCount(npc.getId())) {
							if (getQuestItemsCount(player, SevenSigns.SEAL_STONE_BLUE_ID) < getBlueStoneCount(npc.getId())) {
								showPage(player, "ssq_main_event_acolyte_q0505_21a.htm");
								return;
							}
						}
						
						if (qs505.getMemoStateEx(1) == getGreenStoneCount(npc.getId())) {
							if (getQuestItemsCount(player, SevenSigns.SEAL_STONE_GREEN_ID) < getGreenStoneCount(npc.getId())) {
								showPage(player, "ssq_main_event_acolyte_q0505_21a.htm");
								return;
							}
						}
						
						if (qs505.getMemoStateEx(1) == getRedStoneCount(npc.getId())) {
							if (getQuestItemsCount(player, SevenSigns.SEAL_STONE_RED_ID) < getRedStoneCount(npc.getId())) {
								showPage(player, "ssq_main_event_acolyte_q0505_21a.htm");
								return;
							}
						}
						
						if (_SSF.checkEventRoom(ssqType, roomIndex - 1)) {
							if (qs505.getMemoStateEx(1) == getBlueStoneCount(npc.getId())) {
								if (getQuestItemsCount(player, SevenSigns.SEAL_STONE_BLUE_ID) < getBlueStoneCount(npc.getId())) {
									showPage(player, "ssq_main_event_acolyte_q0505_21a.htm");
									return;
								}
								
								takeItems(player, SevenSigns.SEAL_STONE_BLUE_ID, getBlueStoneCount(npc.getId()));
							}
							
							if (qs505.getMemoStateEx(1) == getGreenStoneCount(npc.getId())) {
								if (getQuestItemsCount(player, SevenSigns.SEAL_STONE_GREEN_ID) < getGreenStoneCount(npc.getId())) {
									showPage(player, "ssq_main_event_acolyte_q0505_21a.htm");
									return;
								}
								
								takeItems(player, SevenSigns.SEAL_STONE_GREEN_ID, getGreenStoneCount(npc.getId()));
							}
							
							if (qs505.getMemoStateEx(1) == getRedStoneCount(npc.getId())) {
								if (getQuestItemsCount(player, SevenSigns.SEAL_STONE_RED_ID) < getRedStoneCount(npc.getId())) {
									showPage(player, "ssq_main_event_acolyte_q0505_21a.htm");
									return;
								}
								
								takeItems(player, SevenSigns.SEAL_STONE_RED_ID, getRedStoneCount(npc.getId()));
							}
							
							for (L2PcInstance c1 : party0.getMembers()) {
								final var q505c1 = QuestManager.getInstance().getQuest(Q00505_BLOODOFFERING);
								final var qs505c1 = q505c1.getQuestState(c1, true);
								
								qs505c1.startQuest();
								qs505c1.setMemoState(i0);
							}
							
							qs505.setMemoStateEx(1, roomIndex - 1);
							
							_SSF.addPartyToEventRoom(ssqType, roomIndex - 1, party0);
							if (npc.checkDoCastConditions(SSQ_CANCEL.getSkill())) {
								npc.setTarget(player);
								npc.doCast(SSQ_CANCEL);
							}
							_SSF.addTimeAttackFee(roomIndex - 1, getTimeAttackFee(npc.getId()));
						} else {
							showPage(player, "ssq_main_event_acolyte_q0505_05.htm");
						}
					}
				} else {
					showPage(player, "data/html/fullquest.html");
				}
			}
			return;
		}
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player) {
		String html = getHtm(player.getHtmlPrefix(), fnHi(npc.getId()));
		
		final var cal = Calendar.getInstance();
		int i0 = cal.get(Calendar.MINUTE);
		if (i0 == 58 || i0 == 18 || i0 == 38 || i0 == 59 || i0 == 19 || i0 == 39) {
			html = html.replace("<?minute?>", "0 minute");
		} else {
			int i1;
			if (i0 > 39) {
				i1 = 58 - i0;
			} else {
				if (i0 > 19) {
					i1 = 38 - i0;
				} else {
					i1 = 18 - i0;
				}
			}
			html = html.replace("<?minute?>", i1 + " Minute");
		}
		
		return html;
	}
	
	@Override
	public String onSpellFinished(L2Npc npc, L2PcInstance player, Skill skill) {
		final int sibylPosX = npc.getTemplate().getParameters().getInt("SibylPosX", 0);
		final int sibylPosY = npc.getTemplate().getParameters().getInt("SibylPosY", 0);
		final int sibylPosZ = npc.getTemplate().getParameters().getInt("SibylPosZ", 0);
		
		final var party0 = player.getParty();
		if (party0 != null) {
			for (L2PcInstance pc : party0.getMembers()) {
				if (npc.calculateDistance(pc, true, false) < MAX_DISTANCE) {
					pc.teleToLocation(new Location(sibylPosX, sibylPosY, sibylPosZ));
				}
			}
		}
		
		return super.onSpellFinished(npc, player, skill);
	}
	
	private static final String fnHi(int npcId) {
		return switch (npcId) {
			case 31127 -> "dawn_acolyte1001.htm";
			case 31128 -> "dawn_acolyte2001.htm";
			case 31129 -> "dawn_acolyte3001.htm";
			case 31130 -> "dawn_acolyte4001.htm";
			case 31131 -> "dawn_acolyte5001.htm";
			case 31137 -> "dusk_acolyte1001.htm";
			case 31138 -> "dusk_acolyte2001.htm";
			case 31139 -> "dusk_acolyte3001.htm";
			case 31140 -> "dusk_acolyte4001.htm";
			case 31141 -> "dusk_acolyte5001.htm";
			default -> null;
		};
	}
	
	private static final String fStringRoom(int id) {
		return switch (id) {
			case 1000312 -> "Level 31 or lower";
			case 1000313 -> "Level 42 or lower";
			case 1000314 -> "Level 53 or lower";
			case 1000315 -> "Level 64 or lower";
			case 1000316 -> "No Level Limit";
			default -> null;
		};
	}
	
	private static final String getParticipateHtml(int npcId) {
		return switch (npcId) {
			case 31127 -> "ssq_main_event_acolyte_q0505_20f.htm";
			case 31128 -> "ssq_main_event_acolyte_q0505_20g.htm";
			case 31129 -> "ssq_main_event_acolyte_q0505_20h.htm";
			case 31130 -> "ssq_main_event_acolyte_q0505_20i.htm";
			case 31131 -> "ssq_main_event_acolyte_q0505_20j.htm";
			case 31137 -> "ssq_main_event_acolyte_q0505_20a.htm";
			case 31138 -> "ssq_main_event_acolyte_q0505_20b.htm";
			case 31139 -> "ssq_main_event_acolyte_q0505_20c.htm";
			case 31140 -> "ssq_main_event_acolyte_q0505_20d.htm";
			case 31141 -> "ssq_main_event_acolyte_q0505_20e.htm";
			default -> null;
		};
	}
	
	private static final String fStringLevel(int npcId) {
		return switch (npcId) {
			case 31127, 31137 -> "32";
			case 31128, 31138 -> "43";
			case 31129, 31139 -> "54";
			case 31130, 31140 -> "65";
			default -> null;
		};
	}
	
	private static final String fStringLevelHtml(int npcId) {
		return switch (npcId) {
			case 31127, 31137 -> "ssq_main_event_acolyte_q0505_04a.htm";
			case 31128, 31138 -> "ssq_main_event_acolyte_q0505_04b.htm";
			case 31129, 31139 -> "ssq_main_event_acolyte_q0505_04c.htm";
			case 31130, 31140 -> "ssq_main_event_acolyte_q0505_04d.htm";
			default -> null;
		};
	}
	
	private static final int getMinLevel(int npcId) {
		return switch (npcId) {
			case 31127, 31137 -> 31;
			case 31128, 31138 -> 42;
			case 31129, 31139 -> 53;
			case 31130, 31140 -> 64;
			case 31131, 31141 -> 99;
			default -> 0;
		};
	}
	
	private static final int getBlueStoneCount(int npcId) {
		return switch (npcId) {
			case 31127, 31137 -> 900;
			case 31128, 31138 -> 1500;
			case 31129, 31139 -> 3000;
			case 31130, 31140 -> 4500;
			case 31131, 31141 -> 6000;
			default -> 0;
		};
	}
	
	private static final int getGreenStoneCount(int npcId) {
		return switch (npcId) {
			case 31127, 31137 -> 540;
			case 31128, 31138 -> 900;
			case 31129, 31139 -> 1800;
			case 31130, 31140 -> 2700;
			case 31131, 31141 -> 3600;
			default -> 0;
		};
	}
	
	private static final int getRedStoneCount(int npcId) {
		return switch (npcId) {
			case 31127, 31137 -> 270;
			case 31128, 31138 -> 450;
			case 31129, 31139 -> 900;
			case 31130, 31140 -> 1350;
			case 31131, 31141 -> 1800;
			default -> 0;
		};
	}
	
	private static final int getTimeAttackFee(int npcId) {
		return switch (npcId) {
			case 31127, 31137 -> 2700;
			case 31128, 31138 -> 4500;
			case 31129, 31139 -> 9000;
			case 31130, 31140 -> 13500;
			case 31131, 31141 -> 18000;
			default -> 0;
		};
	}
	
	private static final int getFragmentsReward(int npcId) {
		return switch (npcId) {
			case 31127, 31137 -> 4;
			case 31128, 31138 -> 5;
			case 31129, 31139 -> 6;
			case 31130, 31140 -> 6;
			case 31131, 31141 -> 7;
			default -> 0;
		};
	}
}