/*
 * Copyright © 2004-2024 L2J DataPack
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
package com.l2jserver.datapack.ai.group_template.festival;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.gameserver.GeoData;
import com.l2jserver.gameserver.SevenSignsFestival;
import com.l2jserver.gameserver.ai.CtrlIntention;
import com.l2jserver.gameserver.enums.CategoryType;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.skills.Skill;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.gameserver.util.Util;

/**
 * ssq_event_present_npc
 * @author Charus
 * @version 2.6.3.0
 * @apiNote @TODO CLAN_ATTACKED is missing
 */
public final class SSQEventPresentNpc extends AbstractNpcAI {
	
	// NPCs
	private final static int[] DAWN_NPCS = {
		18118, 18117, 18116, 18115, 18114
	};
	
	private final static int[] DUSK_NPCS = {
		18113, 18112, 18111,18110, 18109
	};
	
	private static final int Q00505_BLOODOFFERING = 505;
	
	private static final int FLEE_DISTANCE = 100;
	
	private static SevenSignsFestival _SSF;
	
	public SSQEventPresentNpc() {
		super(SSQEventPresentNpc.class.getSimpleName(), "ai/group_template/festival");
		bindSpawn(DAWN_NPCS);
		bindSpawn(DUSK_NPCS);
		bindAttack(DAWN_NPCS);
		bindAttack(DUSK_NPCS);
		bindKill(DAWN_NPCS);
		bindKill(DUSK_NPCS);
		
		_SSF = SevenSignsFestival.getInstance();
	}
	
	@Override
	public String onSpawn(L2Npc npc) {
		npc.setScriptValue("i_ai1", 0);
		startQuestTimer("1001", (2 * 60) * 1000, npc, null);
		
		return super.onSpawn(npc);
	}
	
	@Override
	public String onTimerEvent(String event, L2Npc npc, L2PcInstance player) {
		if (event.equalsIgnoreCase("1001")) {
			npc.deleteMe();
		}
		
		return super.onTimerEvent(event, npc, player);
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isSummon, Skill skill) {
		final int roomIndex = npc.getTemplate().getParameters().getInt("RoomIndex", 0);
		final String partType = npc.getTemplate().getParameters().getString("SSQPart", null);
		final int ssqPart = partType.equals("TWILIGHT") ? 1 : partType.equals("DAWN") ? 2 : 0;
		
		if (roomIndex != -1 && ssqPart != 0) {
			var party0 = _SSF.getPartyFromEventRoom(ssqPart, roomIndex - 1);
			var party1 = attacker.getParty();
			if (party0 == null || party1 == null) {
				npc.setScriptValue("i_ai1", 1);
			} else {
				if (party0.getLeaderObjectId() != party1.getLeaderObjectId() && attacker.isPlayer()) {
					npc.setScriptValue("i_ai1", 1);
				}
			}
		}
		if (skill != null) {
			if (skill.getId() != 0) {
				addFleeDesire(npc, attacker, isSummon);
			}
		} else {
			if (getRandom(100) < 30) {
				addFleeDesire(npc, attacker, isSummon);
			}
		}
		if (attacker.isInCategory(CategoryType.PET_GROUP)) {
			npc.setScriptValue("i_ai1", 1);
		}
		
		return super.onAttack(npc, attacker, damage, isSummon, skill);
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		final int roomIndex = npc.getTemplate().getParameters().getInt("RoomIndex", 0);
		final String partType = npc.getTemplate().getParameters().getString("SSQPart", null);
		final int ssqPart = partType.equals("TWILIGHT") ? 1 : partType.equals("DAWN") ? 2 : 0;
		
		if (npc.getScriptValue("i_ai1") == 1) {
			var c0 = killer.getActingPlayer();
			if (c0 != null) {
				if (c0.getParty() != null) {
					c0 = c0.getParty().getLeader();
				}
				
				final var party0 = _SSF.getPartyFromEventRoom(ssqPart, roomIndex - 1);
				final var c1 = party0.getLeader();
				if (c0 != null && c1 != null) {
					if (c0.getObjectId() == c1.getObjectId()) {
						if (haveMemo(c0, Q00505_BLOODOFFERING)) {
							int i1 = 3 + ((3 * 25) / 100);
							
							final double distance = npc.calculateDistance(c0, true, false);
							if (c0.isInCategory(CategoryType.WIZARD_GROUP)) {
								if (distance < 40) {
									i1 = i1 + ((i1 * 30) / 100);
								}
							} else {
								if (distance < 80) {
									i1 = i1 + ((i1 * 30) / 100);
								}	
							}
							
							if (c0.isInCategory(CategoryType.WIZARD_GROUP)) {
								if (getRandom(2) > 0) {
									i1 = i1 + ((i1 * 7) / 100);
								} else {
									i1 = i1 - ((i1 * 15) / 100);
								}
							} else {
								if (getRandom(2) > 0) {
									i1 = i1 + ((i1 * 15) / 100);
								} else {
									i1 = i1 - ((i1 * 7) / 100);
								}
							}
							
							SystemMessage smsg = SystemMessage.getSystemMessage(SystemMessageId.LEADER_OBTAINED_S2_OF_S1);
							smsg.addItemName(SevenSignsFestival.FESTIVAL_OFFERING_ID);
							smsg.addLong(i1);
							party0.broadcastToPartyMembers(c0, smsg);
							
							giveItems(c0, SevenSignsFestival.FESTIVAL_OFFERING_ID, i1);
						}
					}
				}
			}
		} else {
			var c0 = killer.getActingPlayer();
			if (c0 != null) {
				if (c0.getParty() != null) {
					c0 = c0.getParty().getLeader();
				}
				
				final var party0 = _SSF.getPartyFromEventRoom(ssqPart, roomIndex - 1);
				final var c1 = party0.getLeader();
				if (c0 != null && c1 != null) {
					if (c0.getObjectId() == c1.getObjectId()) {
						if (haveMemo(c0, Q00505_BLOODOFFERING)) {
							int i1 = 30 + ((30 * 25) / 100);
							
							final double distance = npc.calculateDistance(c0, true, false);
							if (c0.isInCategory(CategoryType.WIZARD_GROUP)) {
								if (distance < 40) {
									i1 = i1 + ((i1 * 30) / 100);
								}
							} else {
								if (distance < 80) {
									i1 = i1 + ((i1 * 30) / 100);
								}	
							}
							
							if (c0.isInCategory(CategoryType.WIZARD_GROUP)) {
								if (getRandom(2) > 0) {
									i1 = i1 + ((i1 * 7) / 100);
								} else {
									i1 = i1 - ((i1 * 15) / 100);
								}
							} else {
								if (getRandom(2) > 0) {
									i1 = i1 + ((i1 * 15) / 100);
								} else {
									i1 = i1 - ((i1 * 7) / 100);
								}
							}
							
							SystemMessage smsg = SystemMessage.getSystemMessage(SystemMessageId.LEADER_OBTAINED_S2_OF_S1);
							smsg.addItemName(SevenSignsFestival.FESTIVAL_OFFERING_ID);
							smsg.addLong(i1);
							party0.broadcastToPartyMembers(c0, smsg);
							
							giveItems(c0, SevenSignsFestival.FESTIVAL_OFFERING_ID, i1);
						}
					}
				}
			}
		}
		
		return super.onKill(npc, killer, isSummon);
	}
	
	private static void addFleeDesire(L2Npc npc, L2PcInstance attacker, boolean isSummon) {
		npc.disableCoreAI(true);
		npc.setRunning();
		
		final var summon = isSummon ? attacker.getSummon() : null;
		final var attackerLoc = summon == null ? attacker : summon;
		final double radians = Math.toRadians(Util.calculateAngleFrom(attackerLoc, npc));
		final int posX = (int) (npc.getX() + (FLEE_DISTANCE * Math.cos(radians)));
		final int posY = (int) (npc.getY() + (FLEE_DISTANCE * Math.sin(radians)));
		final int posZ = npc.getZ();
		
		final var destination = GeoData.getInstance().moveCheck(npc.getX(), npc.getY(), npc.getZ(), posX, posY, posZ, attacker.getInstanceId());
		npc.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, destination);
	}
}