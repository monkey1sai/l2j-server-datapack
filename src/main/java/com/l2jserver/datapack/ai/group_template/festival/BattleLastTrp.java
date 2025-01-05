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

/**
 * SSQ Event Last Trp Ex
 * @author Charus
 * @version 2.6.3.0
 */
public final class BattleLastTrp extends AbstractNpcAI {
	
	//Npcs
	private static final int[] DAWN_NPCS = {
		18107, 18097, 18087, 18077, 18067
	};
	
	private static final int[] DUSK_NPCS = {
		18057, 18047, 18037, 18027, 18017
	};
	
	public BattleLastTrp() {
		super(BattleLastTrp.class.getSimpleName(), "ai/group_template/festival");
	}
}