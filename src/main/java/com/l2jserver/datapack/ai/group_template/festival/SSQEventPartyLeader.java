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
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.skills.Skill;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;

/**
 * ssq_event_party_leader
 * @author Charus
 * @version 2.6.3.0
 * @apiNote TODO PARTY_ATTACKED, PARTY_DIED missing
 */
public final class SSQEventPartyLeader extends SSQPartyLeader {
	
	// Npcs
	private static final int[] NPCS = {
		18099, 18089, 18079, 18069, 18059, // DAWN
		18049, 18039, 18029, 18019, 18009  // DUSK
	};
	
	private static final int Q00505_BLOODOFFERING = 505;
	
	private static SevenSignsFestival _SSF;
	
	public SSQEventPartyLeader() {
		this(SSQEventPartyLeader.class.getSimpleName(), NPCS);
		bindSpawn(NPCS);
		bindSeeCreature(NPCS);
		bindAttack(NPCS);
		bindKill(NPCS);
		
		_SSF = SevenSignsFestival.getInstance();
	}
	
	public SSQEventPartyLeader(String name, int[] npcs) {
		super(name, npcs);
	}
	
	@Override
	public String onSpawn(L2Npc npc) {
		npc.setScriptValue("sm.flag", npc.getScriptValue("sm.param1"));
		long i0 = (60 * 5) - ((System.currentTimeMillis() / 1000) - npc.getScriptValue("sm.param2"));
		if (i0 < 1) {
			npc.deleteMe();
			
			return super.onSpawn(npc); 
		}
		
		startQuestTimer("2000", i0 * 1000, npc, null);
		startQuestTimer("2002", 5000, npc, null);
		
		return super.onSpawn(npc);
	}
	
	@Override
	public String onSeeCreature(L2Npc npc, L2Character creature, boolean isSummon) {
		if (!npc.isMoving()) {
			if (!creature.isPlayer() && !creature.isInCategory(CategoryType.SUMMON_NPC_GROUP)) {
				return super.onSeeCreature(npc, creature, isSummon);
			}
			/*if (myself.GetLifeTime() > SeeCreatureAttackerTime && myself.InMyTerritory(myself.sm))
			{
				myself.AddAttackDesire(creature, @AMT_MOVE_TO_TARGET, 200);
			}*/
		}
		
		return super.onSeeCreature(npc, creature, isSummon);
	}
	
	@Override
	public String onTimerEvent(String timer, L2Npc npc, L2PcInstance player) {
		if (timer.equalsIgnoreCase("2000")) {
			if (!npc.isMoving()) {
				npc.deleteMe();
			}
		}
		if (timer.equalsIgnoreCase("2002")) {
			//myself.LookNeighbor(500);
			startQuestTimer("2002", 5000, npc, null);
		}
		
		return super.onTimerEvent(timer, npc, player);
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isSummon, Skill skill) {
		if (getRandom(100) < 10 && npc.calculateDistance(attacker, true, false) > 100) {
			final var ddMagic = npc.getTemplate().getParameters().getSkillHolder("DDMagic");
			if (npc.checkDoCastConditions(ddMagic.getSkill())) {
				npc.doCast(ddMagic);
			}
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
							int i1 = 15 + ((15 * 25) / 100);
							
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
}