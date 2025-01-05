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

import com.l2jserver.gameserver.enums.CategoryType;
import com.l2jserver.gameserver.model.actor.L2Attackable;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2MonsterInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.SkillHolder;
import com.l2jserver.gameserver.model.skills.Skill;

/**
 * ssq_party_leader
 * @author Charus
 * @version 2.6.3.0
 */
public class SSQPartyLeader extends SSQEventBasicWarrior {
	
	// Npcs
	private static final int[] NPCS = {
		18099, 18089, 18079, 18069, 18059, // SSQEventPartyLeader
		18049, 18039, 18029, 18019, 18009  // SSQEventPartyLeader
	};
	
	public SSQPartyLeader() {
		this(SSQPartyLeader.class.getSimpleName(), NPCS);
		bindSpawn(NPCS);
		bindAttack(NPCS);
	}
	
	public SSQPartyLeader(String name, int[] npc) {
		super(name, npc);
	}
	
	@Override
	public String onSpawn(L2Npc npc) {
		npc.setScriptValue("sm.weight_point", 10);
		((L2MonsterInstance) npc).getMinionList().spawnMinions(npc.getTemplate().getParameters().getMinionList("Privates"));
		
		return super.onSpawn(npc);
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isSummon, Skill skill) {
		if (!attacker.isPlayer() || attacker.isInCategory(CategoryType.SUMMON_NPC_GROUP)) {
			if (damage == 0) {
				damage = 1;
			}
			
			addAttackDesire(npc, attacker, (long) ((1.000000 * damage) / (npc.getLevel() + 7)) * 100);
		}
		final L2Attackable mob = (L2Attackable) npc;
		if (mob.getMostHated() != null) {
			var s_dryad_root11 = new SkillHolder(1201, 1);
			if (s_dryad_root11.getSkill().getAbnormalLvl() > 0 && npc.calculateDistance(mob.getMostHated(), true, false) > 40) {
				if (attacker.isPlayer() || attacker.isInCategory(CategoryType.SUMMON_NPC_GROUP)) {
					if (damage == 0)
					{
						damage = 1;
					}
					
					addAttackDesire(npc, attacker, (long) ((1.000000 * damage) / (npc.getLevel() + 7)) * 100);
				}
			}
		}
		
		return super.onAttack(npc, attacker, damage, isSummon, skill);
	}
}