/*
 * Copyright © 2004-2025 L2J DataPack
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
package com.l2jserver.datapack.instances.PailakaVarkaSilenosBarracks;

import static com.l2jserver.gameserver.enums.audio.Music.BS08_A_10000;
import static com.l2jserver.gameserver.util.Util.checkIfInRange;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.datapack.instances.AbstractInstance;
import com.l2jserver.datapack.quests.Q00144_PailakaInjuredDragon.Q00144_PailakaInjuredDragon;
import com.l2jserver.gameserver.GameTimeController;
import com.l2jserver.gameserver.instancemanager.InstanceManager;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.impl.character.npc.NpcEventReceived;
import com.l2jserver.gameserver.model.events.impl.character.npc.NpcSkillFinished;
import com.l2jserver.gameserver.model.events.impl.character.npc.attackable.FactionCall;
import com.l2jserver.gameserver.model.holders.SkillHolder;
import com.l2jserver.gameserver.model.instancezone.InstanceWorld;
import com.l2jserver.gameserver.network.serverpackets.SpecialCamera;

/**
 * Pailaka (Varka Silenos Barracks) instance zone.
 * @author Zoey76
 * @version 2.6.3.0
 */
public final class PailakaVarkaSilenosBarracks extends AbstractInstance {
	
	private final class PailakaVarkaSilenosBarracksWorld extends InstanceWorld {
		
	}
	
	private static final Logger LOG = LoggerFactory.getLogger(PailakaVarkaSilenosBarracks.class);
	
	// NPCs
	private static final int KETRA_ORC_SUPPORTER_2 = 32512;
	// Monsters
	private static final int LATANA = 18660;
	private static final int LATANA_SKILL_USE = 18661;
	private static final int INJURED_DRAGON_CAMERA_1 = 18603;
	private static final int INJURED_DRAGON_CAMERA_2 = 18604;
	private static final int INJURED_DRAGON_TARGET_1 = 18605;
	// private static final int INJURED_DRAGON_TARGET_2 = 18606;
	private static final int TELEPORT_TRAP = 18663;
	// Animals
	private static final int GRAZING_ANTELOPE = 18637;
	private static final int GRAZING_BANDERSNATCH = 18643;
	private static final int GRAZING_FLAVA = 18647;
	private static final int GRAZING_ELDER_ANTELOPE = 18651;
	// Guards
	private static final int VARKA_SILENOS_RECRUIT = 18635;
	private static final int VARKA_SILENOS_FOOTMAN = 18636;
	private static final int VARKA_SILENOS_SCOUT = 18638;
	private static final int VARKA_SILENOS_WARRIOR = 18642;
	private static final int VARKA_SILENOS_OFFICER = 18646;
	private static final int VARKA_SILENOS_GREAT_MAGUS = 18649;
	private static final int VARKA_SILENOS_GENERAL = 18650;
	private static final int VARKA_ELITE_GUARD = 18653;
	private static final int VARKA_COMMANDER = 18654;
	private static final int VARKA_HEAD_GUARD = 18655;
	private static final int PROPHET_GUARD = 18657;
	// Wizards
	private static final int VARKA_SILENOS_HUNTER = 18639;
	private static final int VARKA_SILENOS_SHAMAN = 18640;
	private static final int VARKA_SILENOS_PRIEST = 18641;
	private static final int VARKA_SILENOS_MEDIUM = 18644;
	private static final int VARKA_SILENOS_MAGUS = 18645;
	private static final int VARKA_SILENOS_SEER = 18648;
	private static final int VARKA_SILENOS_GREAT_SEER = 18652;
	private static final int VARKA_HEAD_MAGUS = 18656;
	private static final int DISCIPLE_OF_PROPHET = 18658;
	private static final int VARKA_PROPHET = 18659;
	// Skills
	private static final SkillHolder ANGER = new SkillHolder(5718, 1);
	private static final SkillHolder PRESENTATION_THE_RISE_OF_LATANA = new SkillHolder(5759, 1);
	private static final SkillHolder STUN = new SkillHolder(5716, 1);
	private static final SkillHolder ELECTRIC_FLAME = new SkillHolder(5715, 1);
	private static final SkillHolder FIRE_BREATH = new SkillHolder(5717, 1);
	private static final SkillHolder NPC_HEAL = new SkillHolder(4065, 7);
	// Drops
	private static final int PAILAKA_INSTANT_SHIELD = 13032;
	private static final int QUICK_HEALING_POTION = 13033;
	// Items
	private static final int WEAPON_UPGRADE_STAGE_1 = 13056;
	private static final int WEAPON_UPGRADE_STAGE_2 = 13057;
	// AI Parameters
	private static final double HP_ANGER_ACTIVATION = 0.3;
	// Locations
	private static final Location ENTRY_POINT = new Location(125738, -40933, -3770);
	// Misc
	private static final int INSTANCE_ID = 45;
	private static final List<String> SPAWN_GROUPS = List.of( //
		"pailaka_2316_npc", "pailaka_2316_boss", //
		"pailaka_2316_1m1", "pailaka_2316_1m2", "pailaka_2316_2m1", "pailaka_2316_2m2", //
		"pailaka_2316_3m1", "pailaka_2316_3m2", "pailaka_2316_4m1", "pailaka_2316_4m2", //
		"pailaka_2316_5m1", "pailaka_2316_5m2", "pailaka_2316_6m1", "pailaka_2316_6m2", //
		"pailaka_2316_7m1", "pailaka_2316_7m2", "pailaka_2316_8m1", "pailaka_2316_8m2", //
		"pailaka_2316_9m1", "pailaka_2316_9m2", "pailaka_2316_10m1", "pailaka_2316_10m2", //
		"pailaka_2316_animal_1", "pailaka_2316_animal_2", "pailaka_2316_animal_3", //
		"pailaka_2316_animal_4", "pailaka_2316_animal_5", "pailaka_2316_animal_6", "pailaka_2316_animal_7");
	
	public PailakaVarkaSilenosBarracks() {
		bindSpawn(LATANA, LATANA_SKILL_USE, INJURED_DRAGON_CAMERA_1, INJURED_DRAGON_CAMERA_2, INJURED_DRAGON_TARGET_1, TELEPORT_TRAP);
		bindSpawn(VARKA_SILENOS_HUNTER, VARKA_SILENOS_SHAMAN, VARKA_SILENOS_PRIEST, VARKA_SILENOS_MEDIUM, VARKA_SILENOS_MAGUS, VARKA_SILENOS_SEER, VARKA_SILENOS_GREAT_SEER, VARKA_HEAD_MAGUS, DISCIPLE_OF_PROPHET, VARKA_PROPHET);
		bindSeeCreature(LATANA, INJURED_DRAGON_CAMERA_1, TELEPORT_TRAP);
		bindSeeCreature(VARKA_SILENOS_HUNTER, VARKA_SILENOS_SHAMAN, VARKA_SILENOS_PRIEST, VARKA_SILENOS_MEDIUM, VARKA_SILENOS_MAGUS, VARKA_SILENOS_SEER, VARKA_SILENOS_GREAT_SEER, VARKA_HEAD_MAGUS, DISCIPLE_OF_PROPHET, VARKA_PROPHET);
		bindSpellFinished(LATANA);
		bindAttack(LATANA);
		bindAttack(VARKA_SILENOS_RECRUIT, VARKA_SILENOS_FOOTMAN, VARKA_SILENOS_SCOUT, VARKA_SILENOS_WARRIOR, VARKA_SILENOS_OFFICER, VARKA_SILENOS_GREAT_MAGUS, VARKA_SILENOS_GENERAL, VARKA_ELITE_GUARD, VARKA_COMMANDER, VARKA_HEAD_GUARD, PROPHET_GUARD);
		bindAttack(VARKA_SILENOS_HUNTER, VARKA_SILENOS_SHAMAN, VARKA_SILENOS_PRIEST, VARKA_SILENOS_MEDIUM, VARKA_SILENOS_MAGUS, VARKA_SILENOS_SEER, VARKA_SILENOS_GREAT_SEER, VARKA_HEAD_MAGUS, DISCIPLE_OF_PROPHET, VARKA_PROPHET);
		bindFactionCall(VARKA_SILENOS_RECRUIT, VARKA_SILENOS_FOOTMAN, VARKA_SILENOS_SCOUT, VARKA_SILENOS_WARRIOR, VARKA_SILENOS_OFFICER, VARKA_SILENOS_GREAT_MAGUS, VARKA_SILENOS_GENERAL, VARKA_ELITE_GUARD, VARKA_COMMANDER, VARKA_HEAD_GUARD, PROPHET_GUARD);
		bindKill(LATANA);
		bindKill(GRAZING_ANTELOPE, GRAZING_BANDERSNATCH, GRAZING_FLAVA, GRAZING_ELDER_ANTELOPE);
		bindEventReceived(LATANA, LATANA_SKILL_USE);
	}
	
	@Override
	protected void onEnterInstance(L2PcInstance player, InstanceWorld world, boolean firstEntrance) {
		if (firstEntrance) {
			world.addAllowed(player.getObjectId());
			
			for (var name : SPAWN_GROUPS) {
				final var instance = InstanceManager.getInstance().getInstance(world.getInstanceId());
				instance.spawnGroup(name);
			}
		}
		teleportPlayer(player, ENTRY_POINT, world.getInstanceId(), false);
		LOG.info("Player {} created instance {}.", player, world.getInstanceId());
	}
	
	@Override
	public void onSpawn(L2Npc npc) {
		switch (npc.getId()) {
			case LATANA -> {
				// startQuestTimer("LATANA_1000", 10, npc, null);
				// TODO(Zoey76): Validate if Latana is static.
				npc.isMovementDisabled();
			}
			case LATANA_SKILL_USE -> {
				startQuestTimer("LATANA_SKILL_USE_1002", 10, npc, null);
				final var latana = npc.getVariables().getObject("param1", L2Npc.class);
				if (latana != null) {
					addAttackDesire(latana, (L2Character) latana.getTarget(), 10000000);
				}
			}
			case INJURED_DRAGON_CAMERA_1 -> {
				startQuestTimer("INJURED_DRAGON_CAMERA_1_3000", 10, npc, null);
			}
			case INJURED_DRAGON_CAMERA_2 -> {
				startQuestTimer("INJURED_DRAGON_CAMERA_2_1000", 1, npc, null);
			}
			case INJURED_DRAGON_TARGET_1 -> {
				startQuestTimer("INJURED_DRAGON_TARGET_1_1001", 10, npc, null);
			}
			case TELEPORT_TRAP -> {
				startQuestTimer("TELEPORT_TRAP_500", 1000, npc, null);
				npc.getVariables().set("GM_ID", 1);
			}
			case VARKA_SILENOS_HUNTER, VARKA_SILENOS_SHAMAN, VARKA_SILENOS_PRIEST, VARKA_SILENOS_MEDIUM, VARKA_SILENOS_MAGUS, VARKA_SILENOS_SEER, VARKA_SILENOS_GREAT_SEER, VARKA_HEAD_MAGUS, DISCIPLE_OF_PROPHET, VARKA_PROPHET -> {
				npc.setIsVisible(false);
				// startQuestTimer("WIZARD_1000", 1000, npc, null);
			}
		}
	}
	
	@Override
	public String onEvent(String event, L2Npc npc, L2PcInstance player) {
		switch (event) {
			case "enter" -> {
				enterInstance(player, new PailakaVarkaSilenosBarracksWorld(), "PailakaVarkaSilenosBarracks.xml", INSTANCE_ID);
			}
			case "LATANA_500" -> {
				addSpawn(INJURED_DRAGON_TARGET_1, 105465, -41817, -1768, 0, false, 0, false, npc.getInstanceId());
				startQuestTimer("LATANA_501", 3000, npc, player);
			}
			case "LATANA_501" -> {
				// TODO(Zoey76): npc.addEffectActionDesire(npc, 0, 91 * 1000 / 30, 10000);
				startQuestTimer("LATANA_502", 3000, npc, player);
			}
			case "LATANA_502" -> {
				// TODO(Zoey76): player is null at this point.
				player = npc.getVariables().getPlayer("c_ai1");
				addSkillCastDesire(npc, player, PRESENTATION_THE_RISE_OF_LATANA, 500);
				startQuestTimer("LATANA_503", 9700, npc, player);
			}
			case "LATANA_503" -> {
				// TODO(Zoey76): player is null at this point.
				addSkillCastDesire(npc, player, STUN, 5000);
				startQuestTimer("LATANA_504", 6030, npc, player);
			}
			case "LATANA_504" -> {
				startQuestTimer("LATANA_505", 4000, npc, player);
			}
			case "LATANA_505" -> {
				startQuestTimer("LATANA_2000", 1000, npc, player);
			}
			case "LATANA_600" -> {
				addSpawn(INJURED_DRAGON_TARGET_1, 105465, -41817, -1768, 0, false, 0, false, npc.getInstanceId());
				startQuestTimer("LATANA_602", 2000, npc, player);
			}
			case "LATANA_602" -> {
				addSkillCastDesire(npc, player, ELECTRIC_FLAME, 50000);
				startQuestTimer("LATANA_603", 2500, npc, player);
			}
			case "LATANA_603" -> {
				addSkillCastDesire(npc, player, STUN, 50000);
				startQuestTimer("LATANA_604", 6030, npc, player);
			}
			case "LATANA_604" -> {
				startQuestTimer("LATANA_2000", 6000, npc, player);
			}
			case "LATANA_1000" -> {
				startQuestTimer("LATANA_1000", 30000, npc, player);
			}
			case "LATANA_2000" -> {
				if (player == null) {
					startQuestTimer("LATANA_2000", 3000, npc, null);
				} else {
					if (checkIfInRange(100, npc, player, false)) {
						if (getRandom(100) < 30) {
							addSkillCastDesire(npc, player, ELECTRIC_FLAME, 500000);
						} else {
							addAttackDesire(npc, player, 1000);
						}
					} else if (getRandom(100) < 50) {
						// TODO(Zoey76): Implement check.
						// if (npc.inMyTerritory(player)) {
						final var latanSkillUse = addSpawn(LATANA_SKILL_USE, player, false, 0, false, player.getInstanceId());
						latanSkillUse.getVariables().set("param1", npc);
						// }
					} else {
						addSkillCastDesire(npc, player, FIRE_BREATH, 500000);
					}
					startQuestTimer("LATANA_2000", 6000, npc, player);
				}
			}
			case "LATANA_4000" -> {
				npc.getVariables().set("i_ai0", false);
			}
			case "LATANA_9000" -> {
				addSpawn(KETRA_ORC_SUPPORTER_2, npc.getX() + 100, npc.getY() + 100, npc.getZ(), 0, false, 0, false, npc.getInstanceId());
				npc.decayMe();
			}
			case "LATANA_SKILL_USE_1002" -> {
				npc.broadcastScriptEvent("SCE_BOSS_2ND_SKILL", 2000, npc.getObjectId());
				startQuestTimer("LATANA_SKILL_USE_2002", 5000, npc, player);
			}
			case "LATANA_SKILL_USE_2002" -> {
				// TODO(Zoey76): Check if we should use npc.doDie(null) or npc.decayMe()
				npc.decayMe();
			}
			// Wizards
			case "WIZARD_1000" -> {
				// TODO(Zoey76): npc.lookNeighbor(1000);
				startQuestTimer("WIZARD_1000", 1000, npc, null);
			}
			case "WIZARD_2000" -> {
				final var elapsedTime = GameTimeController.getInstance().getGameTicks() - npc.getVariables().getInt("i_ai2", 0);
				final var delay = (getRandom(3) + 1) * (getRandom(6) + 1) * 5000;
				if (elapsedTime > delay && npc.getVariables().getBoolean("i_ai3", false)) {
					npc.setIsVisible(true);
					npc.getVariables().set("i_ai3", true);
					startQuestTimer("WIZARD_3000", 5000, npc, player);
				} else {
					startQuestTimer("WIZARD_2000", 5000, npc, player);
				}
			}
			case "WIZARD_3000" -> {
				final var skill = npc.getTemplate().getParameters().getSkillHolder(getRandomBoolean() ? "Skill01_ID" : "Skill02_ID");
				addSkillCastDesire(npc, player, skill, 500000);
				startQuestTimer("WIZARD_3000", 10000, npc, player);
			}
			// Cameras
			case "INJURED_DRAGON_CAMERA_1_1000" -> {
				player.sendPacket(new SpecialCamera(npc, 600, 200, 5, 0, 15000, 10000, -10, 8, 1, 1, 1));
				startQuestTimer("INJURED_DRAGON_CAMERA_1_1001", 2000, npc, player);
			}
			case "INJURED_DRAGON_CAMERA_1_1001" -> {
				player.sendPacket(new SpecialCamera(npc, 400, 200, 5, 4000, 15000, 10000, -10, 8, 1, 1, 0));
				startQuestTimer("INJURED_DRAGON_CAMERA_1_1002", 4000, npc, player);
			}
			case "INJURED_DRAGON_CAMERA_1_1002" -> {
				player.sendPacket(new SpecialCamera(npc, 300, 195, 4, 1500, 15000, 10000, -5, 10, 1, 1, 0));
				startQuestTimer("INJURED_DRAGON_CAMERA_1_1003", 1700, npc, player);
			}
			case "INJURED_DRAGON_CAMERA_1_1003" -> {
				player.sendPacket(new SpecialCamera(npc, 130, 2, 5, 0, 15000, 10000, 0, 0, 1, 0, 1));
				startQuestTimer("INJURED_DRAGON_CAMERA_1_1004", 2000, npc, player);
			}
			case "INJURED_DRAGON_CAMERA_1_1004" -> {
				player.sendPacket(new SpecialCamera(npc, 220, 0, 4, 800, 15000, 10000, 5, 10, 1, 0, 0));
				startQuestTimer("INJURED_DRAGON_CAMERA_1_1005", 2000, npc, player);
			}
			case "INJURED_DRAGON_CAMERA_1_1005" -> {
				player.sendPacket(new SpecialCamera(npc, 250, 185, 5, 4000, 15000, 10000, -5, 10, 1, 1, 0));
				startQuestTimer("INJURED_DRAGON_CAMERA_1_1006", 4000, npc, player);
			}
			case "INJURED_DRAGON_CAMERA_1_1006" -> {
				player.sendPacket(new SpecialCamera(npc, 200, 0, 5, 2000, 15000, 10000, 0, 25, 1, 0, 0));
				startQuestTimer("INJURED_DRAGON_CAMERA_1_1007", 4530, npc, player);
			}
			case "INJURED_DRAGON_CAMERA_1_1007" -> {
				player.sendPacket(new SpecialCamera(npc, 300, -3, 5, 3500, 15000, 6000, 0, 6, 1, 0, 0));
				startQuestTimer("INJURED_DRAGON_CAMERA_1_9999", 10000, npc, player);
			}
			case "INJURED_DRAGON_CAMERA_1_2000" -> {
				player.sendPacket(new SpecialCamera(npc, 250, 0, 6, 0, 15000, 10000, 2, 0, 1, 0, 1));
				startQuestTimer("INJURED_DRAGON_CAMERA_1_2001", 2000, npc, player);
			}
			case "INJURED_DRAGON_CAMERA_1_2001" -> {
				player.sendPacket(new SpecialCamera(npc, 230, 0, 5, 2000, 15000, 10000, 0, 0, 1, 0, 0));
				startQuestTimer("INJURED_DRAGON_CAMERA_1_2002", 2500, npc, player);
			}
			case "INJURED_DRAGON_CAMERA_1_2002" -> {
				player.sendPacket(new SpecialCamera(npc, 180, 175, 2, 1500, 15000, 10000, 0, 10, 1, 1, 0));
				startQuestTimer("INJURED_DRAGON_CAMERA_1_2003", 1500, npc, player);
			}
			case "INJURED_DRAGON_CAMERA_1_2003" -> {
				player.sendPacket(new SpecialCamera(npc, 300, 180, 5, 1500, 15000, 3000, 0, 6, 1, 1, 0));
				startQuestTimer("INJURED_DRAGON_CAMERA_1_9999", 3000, npc, player);
			}
			case "INJURED_DRAGON_CAMERA_1_9999" -> {
				npc.decayMe();
			}
			case "INJURED_DRAGON_CAMERA_1_3000" -> {
				// TODO(Zoey76): npc.lookNeighbor(2000);
				startQuestTimer("INJURED_DRAGON_CAMERA_1_3000", 10000, npc, player);
			}
			case "INJURED_DRAGON_CAMERA_2_1000" -> {
				player.sendPacket(new SpecialCamera(npc, 450, 200, 3, 0, 15000, 10000, -15, 20, 1, 1, 1));
				startQuestTimer("INJURED_DRAGON_CAMERA_2_1001", 100, npc, player);
			}
			case "INJURED_DRAGON_CAMERA_2_1001" -> {
				player.sendPacket(new SpecialCamera(npc, 350, 200, 5, 5600, 15000, 10000, -15, 10, 1, 1, 0));
				startQuestTimer("INJURED_DRAGON_CAMERA_2_1002", 5600, npc, player);
			}
			case "INJURED_DRAGON_CAMERA_2_1002" -> {
				player.sendPacket(new SpecialCamera(npc, 360, 200, 5, 1000, 15000, 2000, -15, 10, 1, 1, 0));
				startQuestTimer("INJURED_DRAGON_CAMERA_2_9999", 10000, npc, player);
			}
			case "INJURED_DRAGON_CAMERA_2_9999" -> {
				npc.decayMe();
			}
			// Targets
			case "INJURED_DRAGON_TARGET_1_1001" -> {
				npc.broadcastScriptEvent("SCE_TARGET_SPAWN", 4000, npc.getObjectId());
				startQuestTimer("INJURED_DRAGON_TARGET_1_3000", 60000, npc, player);
			}
			case "INJURED_DRAGON_TARGET_1_3000" -> {
				npc.decayMe();
			}
			// Trap
			case "TELEPORT_TRAP_500" -> {
				// TODO(Zoey76): npc.lookNeighbor(2000);
				startQuestTimer("TELEPORT_TRAP_500", 1000, npc, null);
			}
			case "TELEPORT_TRAP_1000" -> {
				final var gmId = npc.getVariables().getInt("GM_ID");
				final var c_ai0 = npc.getVariables().getPlayer("c_ai0");
				switch (gmId) {
					case 2316001, 2316002, 2316003 -> {
						// if (npc.getGlobalMap(2316001) == 1 && npc.getGlobalMap(2316002) == 1 && npc.getGlobalMap(2316003) == 1) {
						if (c_ai0 != null) {
							giveItems(c_ai0, WEAPON_UPGRADE_STAGE_1, 1);
						}
						npc.getVariables().set("GM_ID", 0);
						// }
					}
					case 2316007, 2316008 -> {
						// if (npc.getGlobalMap(2316007) == 1 && npc.getGlobalMap(2316008) == 1) {
						if (c_ai0 != null) {
							giveItems(c_ai0, WEAPON_UPGRADE_STAGE_2, 1);
						}
						npc.getVariables().set("GM_ID", 0);
						// }
					}
				}
				startQuestTimer("TELEPORT_TRAP_2000", 1000, npc, null);
			}
			case "TELEPORT_TRAP_2000" -> {
				npc.decayMe();
			}
		}
		return null;
	}
	
	@Override
	public void onEventReceived(NpcEventReceived event) {
		switch (event.eventName()) {
			// Sender: Latana Skill Use
			// Receiver: Latana
			case "SCE_BOSS_2ND_SKILL" -> {
				if (event.receiver().getId() == LATANA) {
					// TODO(Zoey76): Target might be null at this point.
					final var player = event.sender().getTarget().getActingPlayer();
					if ((player != null) && checkIfInRange(900, event.sender(), player, false)) {
						addSkillCastDesire(event.sender(), player, STUN, 50000);
					}
				}
			}
			// Sender: Injured Dragon Target 1
			// Receiver: Latana
			case "SCE_TARGET_SPAWN" -> {
				if (event.receiver().getId() == LATANA) {
					// TODO(Zoey76): Target might be null at this point.
					final var player = event.sender().getTarget().getActingPlayer();
					event.receiver().getVariables().set("c_ai1", player);
				}
			}
			// Sender: Camera 1
			// Receiver: Camera 1
			case "SCE_RATANA_CAMERA_START" -> {
				if (event.receiver().getId() == INJURED_DRAGON_CAMERA_1) {
					switch (event.arg1()) {
						case 1 -> {
							startQuestTimer("INJURED_DRAGON_CAMERA_1_1000", 10, event.sender(), null);
						}
						case 2 -> {
							startQuestTimer("INJURED_DRAGON_CAMERA_1_2000", 10, event.sender(), null);
						}
					}
				}
			}
			// Sender: ?
			// Receiver: Teleport Trap
			case "SCE_REMOVE_AREA" -> {
				final var npc = event.receiver();
				if (npc.getId() == TELEPORT_TRAP) {
					// TODO(Zoey76): npc.areaSetOnOffEx(AreaName, 0, InstantZoneGetId());
					startQuestTimer("TELEPORT_TRAP_1000", 1000, npc, null);
					npc.dropItem(null, PAILAKA_INSTANT_SHIELD, getRandom(10) + 1);
					npc.dropItem(null, QUICK_HEALING_POTION, getRandom(10) + 1);
				}
			}
		}
	}
	
	@Override
	public void onSeeCreature(L2Npc npc, L2Character creature) {
		switch (npc.getId()) {
			case LATANA -> {
				if (!npc.getVariables().getBoolean("i_ai2", false) && creature.isPlayer()) {
					final var player = creature.getActingPlayer();
					final var qs = player.getQuestState(Q00144_PailakaInjuredDragon.class.getSimpleName());
					if (qs == null) {
						return;
					}
					
					if (qs.isCond(4)) {
						startQuestTimer("LATANA_9000", 1000, npc, player);
						npc.getVariables().set("i_ai0", true);
					} else {
						playSound(player, BS08_A_10000);
						npc.broadcastScriptEvent("SCE_RATANA_CAMERA_START", 4000, 1);
						startQuestTimer("LATANA_500", 1000, npc, player);
						npc.getVariables().set("i_ai2", true);
					}
				}
			}
			case INJURED_DRAGON_CAMERA_1 -> {
				if (creature.isPlayer() && !npc.getVariables().getBoolean("i_ai0", false)) {
					npc.getVariables().set("i_ai0", true);
				}
			}
			case TELEPORT_TRAP -> {
				if (creature.isPlayer()) {
					npc.getVariables().set("c_ai0", creature);
				}
			}
			case VARKA_SILENOS_HUNTER, VARKA_SILENOS_SHAMAN, VARKA_SILENOS_PRIEST, VARKA_SILENOS_MEDIUM, VARKA_SILENOS_MAGUS, VARKA_SILENOS_SEER, VARKA_SILENOS_GREAT_SEER, VARKA_HEAD_MAGUS, DISCIPLE_OF_PROPHET, VARKA_PROPHET -> {
				if (creature.isPlayable()) {
					if (npc.getVariables().getBoolean("i_ai0", false)) {
						startQuestTimer("WIZARD_2000", 1000, npc, creature.getActingPlayer());
						npc.getVariables().set("i_ai0", true);
						npc.getVariables().set("i_ai2", GameTimeController.getInstance().getGameTicks());
					}
				} else {
					if (creature.getMaxHp() * 0.7 > creature.getCurrentHp()) {
						if (getRandom(100) < 10) {
							addSkillCastDesire(npc, creature, NPC_HEAL, 500000);
						}
					}
				}
			}
		}
	}
	
	@Override
	public void onSpellFinished(NpcSkillFinished event) {
		switch (event.npc().getId()) {
			case LATANA -> {
				addAttackDesire(event.npc(), event.player(), 100);
			}
		}
	}
	
	@Override
	public void onAttack(L2Npc npc, L2PcInstance player, int damage, boolean isSummon) {
		switch (npc.getId()) {
			case LATANA -> {
				if (!npc.getVariables().getBoolean("i_ai2", false)) {
					final var qs = player.getQuestState(Q00144_PailakaInjuredDragon.class.getSimpleName());
					if (qs == null) {
						return;
					}
					
					if (qs.isCond(4)) {
						startQuestTimer("LATANA_9000", 1000, npc, player);
						npc.getVariables().set("i_ai0", true);
					} else {
						playSound(player, BS08_A_10000);
						npc.broadcastScriptEvent("SCE_RATANA_CAMERA_START", 4000, 2);
						startQuestTimer("LATANA_600", 1000, npc, player);
						npc.getVariables().set("i_ai2", true);
					}
				}
				
				if ((npc.getCurrentHp() < (npc.getMaxHp() * HP_ANGER_ACTIVATION)) && !npc.getVariables().getBoolean("i_ai0", false)) {
					addSkillCastDesire(npc, player, ANGER, 50000000);
					npc.getVariables().set("i_ai0", true);
					startQuestTimer("LATANA_4000", 120000, npc, player);
				}
			}
			case VARKA_SILENOS_RECRUIT, VARKA_SILENOS_FOOTMAN, VARKA_SILENOS_SCOUT, VARKA_SILENOS_WARRIOR, VARKA_SILENOS_OFFICER, VARKA_SILENOS_GREAT_MAGUS, VARKA_SILENOS_GENERAL, VARKA_ELITE_GUARD, VARKA_COMMANDER, VARKA_HEAD_GUARD, PROPHET_GUARD -> {
				wizardAction(npc, player);
			}
		}
	}
	
	@Override
	public void onFactionCall(FactionCall event) {
		switch (event.npc().getId()) {
			case VARKA_SILENOS_RECRUIT, VARKA_SILENOS_FOOTMAN, VARKA_SILENOS_SCOUT, VARKA_SILENOS_WARRIOR, VARKA_SILENOS_OFFICER, VARKA_SILENOS_GREAT_MAGUS, VARKA_SILENOS_GENERAL, VARKA_ELITE_GUARD, VARKA_COMMANDER, VARKA_HEAD_GUARD, PROPHET_GUARD -> {
				wizardAction(event.npc(), event.attacker());
			}
		}
	}
	
	@Override
	public void onKill(L2Npc npc, L2PcInstance killer, boolean isSummon) {
		switch (npc.getId()) {
			case LATANA -> {
				addSpawn(INJURED_DRAGON_CAMERA_2, 105974, -41794, -1784, 32768, false, 0, false, killer.getInstanceId());
				addSpawn(KETRA_ORC_SUPPORTER_2, killer.getX() + 100, killer.getY() + 100, killer.getZ(), 0, false, 0, false, killer.getInstanceId());
			}
			case GRAZING_ANTELOPE, GRAZING_BANDERSNATCH, GRAZING_FLAVA, GRAZING_ELDER_ANTELOPE -> {
				npc.dropItem(killer, getRandomBoolean() ? PAILAKA_INSTANT_SHIELD : QUICK_HEALING_POTION, getRandom(10) + 1);
			}
		}
	}
	
	private void wizardAction(L2Npc npc, L2PcInstance attacker) {
		if (npc.calculateDistance(attacker, false, false) <= 40) {
			addAttackDesire(npc, attacker, 100);
		} else if (getRandom(100) < 33) {
			final var skill = npc.getTemplate().getParameters().getSkillHolder(getRandomBoolean() ? "Skill01_ID" : "Skill02_ID");
			if (skill != null) {
				addSkillCastDesire(npc, attacker, skill, 500000);
			}
		}
	}
}
