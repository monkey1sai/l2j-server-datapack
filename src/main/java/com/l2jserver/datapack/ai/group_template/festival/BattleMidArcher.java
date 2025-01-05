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
 * SSQ Event Mid Archer
 * @author Charus
 * @version 2.6.3.0
 */
public final class BattleMidArcher extends AbstractNpcAI {
	
	//Npcs
	private static final int[] DAWN_NPCS = {
		18106, 18096, 18086, 18076, 18066
	};
	
	private static final int[] DUSK_NPCS = {
		18056, 18046, 18036, 18026, 18016
	};
	
	public BattleMidArcher() {
		super(BattleMidArcher.class.getSimpleName(), "ai/group_template/festival");
	}
}