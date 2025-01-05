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

import java.util.List;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.gameserver.SevenSignsFestival;
import com.l2jserver.gameserver.enums.CategoryType;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.actor.L2Attackable;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.skills.Skill;

/**
 * ssq_event_basic_warrior
 * @author Charus
 * @version 2.6.3.0
 * @apiNote CLAN_ATTACKED, DESIRE_MANIPULATION missing
 */
public class SSQEventBasicWarrior extends AbstractNpcAI {
	// Npcs
	private static final int[] DAWN_NPCS = {
		18317, 18315, 18313, 18311, 18309, // SSQEventBomb
		18099, 18089, 18079, 18069, 18059  // SSQEventPartyLeader
	};
	
	private static final int[] DUSK_NPCS = {
		18307, 18305, 18303, 18301, 18299, // SSQEventBomb
		18049, 18039, 18029, 18019, 18009  // SSQEventPartyLeader
	};
	
	private static SevenSignsFestival _SSF;
	
	public SSQEventBasicWarrior(String name, int[] npcs) {
		super(name, "ai/group_template/festival");
		bindSpawn(DAWN_NPCS);
		bindSpawn(DUSK_NPCS);
		bindAttack(DAWN_NPCS);
		bindAttack(DUSK_NPCS);
		bindSkillSee(DAWN_NPCS);
		bindSkillSee(DUSK_NPCS);
		
		_SSF = SevenSignsFestival.getInstance();
	}
	
	@Override
	public String onSpawn(L2Npc npc) {
		startQuestTimer("2001", 10000, npc, null);
		npc.setScriptValue("i_ai1", 0);
		
		return super.onSpawn(npc);
	}
	
	@Override
	public String onTimerEvent(String timer, L2Npc npc, L2PcInstance player) {
		if (timer.equalsIgnoreCase("2001")) {
			if ((System.currentTimeMillis() / 1000) - npc.getScriptValue("sm.param2") > ((60 * 17) + 50)) {
				npc.deleteMe();
			} else {
				final L2Attackable mob = (L2Attackable) npc;
				if (mob.getMostHated() != null) {
					mob.clearAggroList();
				}
				startQuestTimer("2001", 10000, npc, null);
			}
		}
		
		return super.onTimerEvent(timer, npc, player);
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isSummon, Skill skill) {
		final int roomIndex = npc.getTemplate().getParameters().getInt("RoomIndex", 0);
		final String partType = npc.getTemplate().getParameters().getString("SSQPart", null);
		final int ssqPart = partType.equals("TWILIGHT") ? 1 : partType.equals("DAWN") ? 2 : 0;
		
		if (attacker.isInCategory(CategoryType.PET_GROUP)) {
			npc.setScriptValue("i_ai1", 1);
		}
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
		
		return super.onAttack(npc, attacker, damage, isSummon, skill);
	}
	
	@Override
	public String onSkillSee(L2Npc npc, L2PcInstance caster, Skill skill, List<L2Object> targets, boolean isSummon) {
		final int roomIndex = npc.getTemplate().getParameters().getInt("RoomIndex", 0);
		final String partType = npc.getTemplate().getParameters().getString("SSQPart", null);
		final int ssqPart = partType.equals("TWILIGHT") ? 1 : partType.equals("DAWN") ? 2 : 0;
		
		if (roomIndex != -1 && ssqPart != 0) {
			var party0 = _SSF.getPartyFromEventRoom(ssqPart, roomIndex - 1);
			var party1 = caster.getParty();
			if (party0 == null || party1 == null) {
				npc.setScriptValue("i_ai1", 1);
			} else {
				if (party0.getLeaderObjectId() != party1.getLeaderObjectId() && caster.isPlayer()) {
					npc.setScriptValue("i_ai1", 1);
				}
			}
		}
		
		return super.onSkillSee(npc, caster, skill, targets, isSummon);
	}
}