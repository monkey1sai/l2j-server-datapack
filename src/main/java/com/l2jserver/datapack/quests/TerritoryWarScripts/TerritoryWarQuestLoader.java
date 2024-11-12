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
package com.l2jserver.datapack.quests.TerritoryWarScripts;

import com.l2jserver.gameserver.scripting.ScriptLoader;

/**
 * Loader for territory war quests
 * @author Kita (Noé Caratini)
 * @see TerritoryWarSuperClass
 */
public class TerritoryWarQuestLoader implements ScriptLoader.QuestScriptLoader {
	
	@Override
	public void load() {
		// initialize superclass
		new TerritoryWarSuperClass(-1, TerritoryWarSuperClass.class.getSimpleName(), "Territory War Superclass");
		
		// initialize subclasses
		// "For The Sake" quests
		final var gludio = new Q00717_ForTheSakeOfTheTerritoryGludio();
		TerritoryWarSuperClass._forTheSakeScripts.put(gludio.TERRITORY_ID, gludio);
		final var dion = new Q00718_ForTheSakeOfTheTerritoryDion();
		TerritoryWarSuperClass._forTheSakeScripts.put(dion.TERRITORY_ID, dion);
		final var giran = new Q00719_ForTheSakeOfTheTerritoryGiran();
		TerritoryWarSuperClass._forTheSakeScripts.put(giran.TERRITORY_ID, giran);
		final var oren = new Q00720_ForTheSakeOfTheTerritoryOren();
		TerritoryWarSuperClass._forTheSakeScripts.put(oren.TERRITORY_ID, oren);
		final var aden = new Q00721_ForTheSakeOfTheTerritoryAden();
		TerritoryWarSuperClass._forTheSakeScripts.put(aden.TERRITORY_ID, aden);
		final var innadril = new Q00722_ForTheSakeOfTheTerritoryInnadril();
		TerritoryWarSuperClass._forTheSakeScripts.put(innadril.TERRITORY_ID, innadril);
		final var goddard = new Q00723_ForTheSakeOfTheTerritoryGoddard();
		TerritoryWarSuperClass._forTheSakeScripts.put(goddard.TERRITORY_ID, goddard);
		final var rune = new Q00724_ForTheSakeOfTheTerritoryRune();
		TerritoryWarSuperClass._forTheSakeScripts.put(rune.TERRITORY_ID, rune);
		final var schuttgart = new Q00725_ForTheSakeOfTheTerritorySchuttgart();
		TerritoryWarSuperClass._forTheSakeScripts.put(schuttgart.TERRITORY_ID, schuttgart);
		// "Protect the" quests
		final var catapult = new Q00729_ProtectTheTerritoryCatapult();
		TerritoryWarSuperClass._protectTheScripts.put(catapult.getId(), catapult);
		final var supplies = new Q00730_ProtectTheSuppliesSafe();
		TerritoryWarSuperClass._protectTheScripts.put(supplies.getId(), supplies);
		final var military = new Q00731_ProtectTheMilitaryAssociationLeader();
		TerritoryWarSuperClass._protectTheScripts.put(military.getId(), military);
		final var religious = new Q00732_ProtectTheReligiousAssociationLeader();
		TerritoryWarSuperClass._protectTheScripts.put(religious.getId(), religious);
		final var economic = new Q00733_ProtectTheEconomicAssociationLeader();
		TerritoryWarSuperClass._protectTheScripts.put(economic.getId(), economic);
		// "Kill" quests
		final var knights = new Q00734_PierceThroughAShield();
		for (int i : knights.CLASS_IDS) {
			TerritoryWarSuperClass._killTheScripts.put(i, knights);
		}
		final var warriors = new Q00735_MakeSpearsDull();
		for (int i : warriors.CLASS_IDS) {
			TerritoryWarSuperClass._killTheScripts.put(i, warriors);
		}
		final var wizards = new Q00736_WeakenTheMagic();
		for (int i : wizards.CLASS_IDS) {
			TerritoryWarSuperClass._killTheScripts.put(i, wizards);
		}
		final var priests = new Q00737_DenyBlessings();
		for (int i : priests.CLASS_IDS) {
			TerritoryWarSuperClass._killTheScripts.put(i, priests);
		}
		final var keys = new Q00738_DestroyKeyTargets();
		for (int i : keys.CLASS_IDS) {
			TerritoryWarSuperClass._killTheScripts.put(i, keys);
		}
	}
}
