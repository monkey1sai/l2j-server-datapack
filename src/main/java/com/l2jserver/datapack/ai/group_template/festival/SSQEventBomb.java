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

import com.l2jserver.gameserver.SevenSignsFestival;
import com.l2jserver.gameserver.enums.CategoryType;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.skills.Skill;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;

/**
 * ssq_event_bomb
 * @author Charus
 * @version 2.6.3.0
 */
public class SSQEventBomb extends SSQEventBasicWarrior {
	// Npcs
	private static final int[] NPCS = {
		18317, 18315, 18313, 18311, 18309, // DAWN
		18307, 18305, 18303, 18301, 18299  // DUSK
	};
	
	private static final int Q00505_BLOODOFFERING = 505;
	
	private static SevenSignsFestival _SSF;
	
	public SSQEventBomb() {
		this(SSQPartyLeader.class.getSimpleName(), NPCS);
		bindAttack(NPCS);
		bindKill(NPCS);
		bindMoveFinished(NPCS);
		bindSpellFinished(NPCS);
		bindEventReceived(NPCS);
		
		_SSF = SevenSignsFestival.getInstance();
	}
	
	public SSQEventBomb(String name, int[] npcs) {
		super(name, npcs);
	}
	
	@Override
	public String onEventReceived(String eventName, L2Npc sender, L2Npc receiver, L2Object reference) {
		if (eventName.equalsIgnoreCase("10007")) {
			addMoveToDesire(receiver, sender.getLocation(), 50);
		}
		
		return super.onEventReceived(eventName, sender, receiver, reference);
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
	
	@Override
	public void onMoveFinished(L2Npc npc) {
		final var ddMagic = npc.getTemplate().getParameters().getSkillHolder("DDMagic");
		if (npc.checkDoCastConditions(ddMagic.getSkill())) {
			npc.doCast(ddMagic);
		}
	}
	
	@Override
	public String onSpellFinished(L2Npc npc, L2PcInstance player, Skill skill) {
		npc.doDie(npc);
		
		return super.onSpellFinished(npc, player, skill);
	}
}