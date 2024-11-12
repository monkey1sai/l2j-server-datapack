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
package com.l2jserver.datapack.vehicles;

import com.l2jserver.gameserver.instancemanager.BoatManager;
import com.l2jserver.gameserver.scripting.ScriptLoader;

/**
 * Loader for boat scripts
 * @author Kita (Noé Caratini)
 */
public class BoatLoader implements ScriptLoader {
	
	@Override
	public void load() {
		final var talkingGludinBoat = BoatManager.getInstance().getNewBoat(1, -96622, 261660, -3610, 32768);
		if (talkingGludinBoat != null) {
			talkingGludinBoat.registerEngine(new BoatTalkingGludin(talkingGludinBoat));
			talkingGludinBoat.runEngine(180000);
			BoatManager.getInstance().dockShip(BoatManager.TALKING_ISLAND, true);
		}
		
		final var giranTalkingBoat = BoatManager.getInstance().getNewBoat(2, 48950, 190613, -3610, 60800);
		if (giranTalkingBoat != null) {
			giranTalkingBoat.registerEngine(new BoatGiranTalking(giranTalkingBoat));
			giranTalkingBoat.runEngine(180000);
		}
		
		final var innadrilTourBoat = BoatManager.getInstance().getNewBoat(4, 111264, 226240, -3610, 32768);
		if (innadrilTourBoat != null) {
			innadrilTourBoat.registerEngine(new BoatInnadrilTour(innadrilTourBoat));
			innadrilTourBoat.runEngine(180000);
		}
		
		final var gludinRuneBoat = BoatManager.getInstance().getNewBoat(3, -95686, 150514, -3610, 16723);
		if (gludinRuneBoat != null) {
			gludinRuneBoat.registerEngine(new BoatGludinRune(gludinRuneBoat));
			gludinRuneBoat.runEngine(180000);
			BoatManager.getInstance().dockShip(BoatManager.GLUDIN_HARBOR, true);
		}
		
		final var runePrimevalBoat = BoatManager.getInstance().getNewBoat(5, 34381, -37680, -3610, 40785);
		if (runePrimevalBoat != null) {
			runePrimevalBoat.registerEngine(new BoatRunePrimeval(runePrimevalBoat));
			runePrimevalBoat.runEngine(180000);
			BoatManager.getInstance().dockShip(BoatManager.RUNE_HARBOR, true);
		}
	}
}
