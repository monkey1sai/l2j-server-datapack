/*
 * Copyright © 2004-2023 L2J DataPack
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
package com.l2jserver.datapack.ai.npc.FestivalWitch;

import java.util.HashMap;
import java.util.Map;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.gameserver.SevenSignsFestival;
import com.l2jserver.gameserver.instancemanager.ZoneManager;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2FestivalMonsterInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;
import com.l2jserver.gameserver.network.NpcStringId;
import com.l2jserver.gameserver.network.clientpackets.Say2;
import com.l2jserver.gameserver.network.serverpackets.PlaySound;

/**
 * ssq_main_event_sibyl
 * @author Charus
 */
public final class FestivalWitch extends AbstractNpcAI {
	
	// NPCs
	private static final int[] DAWN_NPCS = {
		31132,
		31133,
		31134,
		31135,
		31136
	};
	
	private static final int[] DUSK_NPCS = {
		31142,
		31143,
		31144,
		31145,
		31146
	};
	
	private static final int[][] ZONES = {
		{ 31136, 1721001 },
		{ 31135, 1721002 },
		{ 31134, 1721003 },
		{ 31133, 1721004 },
		{ 31132, 1721005 },
		{ 31146, 1720001 },
		{ 31145, 1720002 },
		{ 31144, 1720003 },
		{ 31143, 1720004 },
		{ 31142, 1720005 }
	};
	
	private static Map<Integer, Integer> _zoneList = new HashMap<>();
	
	private static SevenSignsFestival _SSF;
	
	public FestivalWitch() {
		super(FestivalWitch.class.getSimpleName(), "ai/npc");
		bindSpawn(DAWN_NPCS);
		bindSpawn(DUSK_NPCS);
		bindStartNpc(DAWN_NPCS);
		bindStartNpc(DUSK_NPCS);
		bindFirstTalk(DAWN_NPCS);
		bindFirstTalk(DUSK_NPCS);
		bindMenuSelected(DAWN_NPCS);
		bindMenuSelected(DUSK_NPCS);
		
		for (int[] zone : ZONES) {
			_zoneList.put(zone[0], zone[1]);
		}
		
		_SSF = SevenSignsFestival.getInstance();
	}
	
	@Override
	public String onSpawn(L2Npc npc) {
		npc.setScriptValue("i_ai0", System.currentTimeMillis());
		npc.setScriptValue("i_ai1", 0);
		npc.setScriptValue("i_ai3", System.currentTimeMillis());
		npc.setScriptValue("i_quest0", 0);
		npc.setScriptValue("i_quest1", 1);
		npc.setScriptValue("i_quest2", 0);
		npc.setScriptValue("i_quest3", 0);
		npc.setScriptValue("i_quest4", 1);
		npc.setScriptValue("i_ai2", 0);
		npc.setScriptValue("i_ai4", 1);
		npc.setScriptValue("sm.param1", 0);
		npc.setScriptValue("sm.flag", 0);
		startQuestTimer("3001", 3000, npc, null);
		npc.broadcastPacket(PlaySound.createSound("B06_S01"));
		startQuestTimer("3023", 20 * 1000, npc, null);
		
		return super.onSpawn(npc);
	}
	
	@Override
	public String onTimerEvent(String timer, L2Npc npc, L2PcInstance player) {
		final var roomIndex = npc.getTemplate().getParameters().getInt("room_index", 0);
		final var partType = npc.getTemplate().getParameters().getString("part_type", null);
		final var ssqType = partType.equals("TWILIGHT") ? 1 : partType.equals("DAWN") ? 2 : 0;
		
		final var escapeX = npc.getTemplate().getParameters().getInt("escape_x", 0);
		final var escapeY = npc.getTemplate().getParameters().getInt("escape_y", 0);
		final var coordZ = npc.getTemplate().getParameters().getInt("coord_z", 0);
		
		final var battle11x = npc.getTemplate().getParameters().getInt("battle1_1x", 0);
		final var battle12x = npc.getTemplate().getParameters().getInt("battle1_2x", 0);
		final var battle13x = npc.getTemplate().getParameters().getInt("battle1_3x", 0);
		final var battle14x = npc.getTemplate().getParameters().getInt("battle1_4x", 0);
		final var battle11y = npc.getTemplate().getParameters().getInt("battle1_1y", 0);
		final var battle12y = npc.getTemplate().getParameters().getInt("battle1_2y", 0);
		final var battle13y = npc.getTemplate().getParameters().getInt("battle1_3y", 0);
		final var battle14y = npc.getTemplate().getParameters().getInt("battle1_4y", 0);
		
		final var battle21x = npc.getTemplate().getParameters().getInt("battle2_1x", 0);
		final var battle22x = npc.getTemplate().getParameters().getInt("battle2_2x", 0);
		final var battle23x = npc.getTemplate().getParameters().getInt("battle2_3x", 0);
		final var battle24x = npc.getTemplate().getParameters().getInt("battle2_4x", 0);
		final var battle21y = npc.getTemplate().getParameters().getInt("battle2_1y", 0);
		final var battle22y = npc.getTemplate().getParameters().getInt("battle2_2y", 0);
		final var battle23y = npc.getTemplate().getParameters().getInt("battle2_3y", 0);
		final var battle24y = npc.getTemplate().getParameters().getInt("battle2_4y", 0);
		
		final var stair1x = npc.getTemplate().getParameters().getInt("stair_1x", 0);
		final var stair2x = npc.getTemplate().getParameters().getInt("stair_2x", 0);
		final var stair3x = npc.getTemplate().getParameters().getInt("stair_3x", 0);
		final var stair4x = npc.getTemplate().getParameters().getInt("stair_4x", 0);
		final var stair1y = npc.getTemplate().getParameters().getInt("stair_1y", 0);
		final var stair2y = npc.getTemplate().getParameters().getInt("stair_2y", 0);
		final var stair3y = npc.getTemplate().getParameters().getInt("stair_3y", 0);
		final var stair4y = npc.getTemplate().getParameters().getInt("stair_4y", 0);
		
		final var battlePresentName = npc.getTemplate().getParameters().getInt("battle_present_name", 0);
		final var battleBombManName = npc.getTemplate().getParameters().getInt("battle_bomb_man_name", 0);
		final var battle1NpcName = npc.getTemplate().getParameters().getInt("battle1_npc_name", 0);
		final var battleSniperName = npc.getTemplate().getParameters().getInt("battle_sniper_name", 0);
		final var battleSlowTypeNameEx = npc.getTemplate().getParameters().getInt("battle_slow_type_name_ex", 0);
		final var battleSlowTypeName = npc.getTemplate().getParameters().getInt("battle_slow_type_name", 0);
		final var battleFastTypeNameEx = npc.getTemplate().getParameters().getInt("battle_fast_type_name_ex", 0);
		final var battleFastTypeName = npc.getTemplate().getParameters().getInt("battle_fast_type_name", 0);
		
		if (timer.equalsIgnoreCase("3001")) {
			if (((System.currentTimeMillis() - npc.getScriptValue("i_ai0")) / 1000) > ((60 * 17) + 45) && npc.getScriptValue("i_ai1") == 2) {
				npc.setScriptValue("i_ai1", 3);
				broadcastNpcSay(npc, Say2.NPC_SHOUT, NpcStringId.THAT_WILL_DO_ILL_MOVE_YOU_TO_THE_OUTSIDE_SOON);
			}
			if (((System.currentTimeMillis() - npc.getScriptValue("i_ai0")) / 1000) > (60 * 10) && npc.getScriptValue("i_ai1") == 1) {
				for (int i0 = 0; i0 < 10; i0++) {
					int i1 = getRandom(4);
					int i2 = npc.getX();
					int i3 = npc.getY();
					
					if (i1 == 0) {
						i2 = ((i2 + 200) + 50) - getRandom(100);
						i3 = ((i3 + 200) + 50) - getRandom(100);
					} else {
						if (i1 == 1) {
							i2 = ((i2 + 200) + 50) - getRandom(100);
							i3 = ((i3 - 200) + 50) - getRandom(100);
						} else {
							if (i1 == 2) {
								i2 = ((i2 - 200) + 50) - getRandom(100);
								i3 = ((i3 + 200) + 50) - getRandom(100);
							} else {
								i2 = ((i2 - 200) + 50) - getRandom(100);
								i3 = ((i3 - 200) + 50) - getRandom(100);
							}
						}
					}
					
					addSpawnEx(battlePresentName, i2, i3, coordZ, 0, false, 0, false, 0, 0, 0);
				}
				
				npc.setScriptValue("i_ai1", 2);
			}
			if (((System.currentTimeMillis() - npc.getScriptValue("i_ai0")) / 1000) > ((60 * 17) + 50)) {
				instantTeleportInMyTerritory(npc, escapeX, escapeY, coordZ);
				clearEventRoom(npc);
				
				_SSF.festivalEnd(ssqType, roomIndex - 1);
				
				npc.deleteMe();
				
				return null;
			}
			if (((System.currentTimeMillis() - npc.getScriptValue("i_ai0")) / 1000) > 10 && npc.getScriptValue("i_ai1") == 0) {
				npc.setScriptValue("i_ai1", 1);
				
				addSpawnEx(battle1NpcName, battle11x, battle11y, coordZ, 32768, false, 0, false, 1, npc.getScriptValue("i_ai0"), 0);
				addSpawnEx(battle1NpcName, battle12x, battle12y, coordZ, 32768, false, 0, false, 16, npc.getScriptValue("i_ai0"), 0);
				addSpawnEx(battle1NpcName, battle13x, battle13y, coordZ, 32768, false, 0, false, 256, npc.getScriptValue("i_ai0"), 0);
				addSpawnEx(battle1NpcName, battle14x, battle14y, coordZ, 32768, false, 0, false, 4096, npc.getScriptValue("i_ai0"), 0);
			}
			if (((System.currentTimeMillis() - npc.getScriptValue("i_ai0")) / 1000) > (60 * 5)) {
				long i0 = (System.currentTimeMillis() - npc.getScriptValue("i_ai0")) / 1000;
				if (i0 > (15 * 60)) {
					npc.setScriptValue("i_ai4", 4);
				} else {
					if (i0 > (10 * 60)) {
						npc.setScriptValue("i_ai4", 3);
					} else {
						if (i0 > (8 * 60)) {
							npc.setScriptValue("i_ai4", 4);
						} else {
							if (i0 > (6 * 60)) {
								npc.setScriptValue("i_ai4", 2);
							}
						}
					}
				}
				
				i0 = 0;
				if ((npc.getScriptValue("i_ai2") & 1) == 1) {
					i0 = i0 + 1;
				}
				if ((npc.getScriptValue("i_ai2") & 16) == 16) {
					i0 = i0 + 1;
				}
				if ((npc.getScriptValue("i_ai2") & 256) == 256) {
					i0 = i0 + 1;
				}
				if ((npc.getScriptValue("i_ai2") & 4096) == 4096) {
					i0 = i0 + 1;
				}
				if (i0 < npc.getScriptValue("i_ai4")) {
					int i2 = getRandom(1, 4);
					if (i2 == 1) {
						i2 = 1;
					} else {
						if (i2 == 2) {
							i2 = 16;
						} else {
							if (i2 == 3) {
								i2 = 256;
							} else {
								if (i2 == 4) {
									i2 = 4096;
								}
							}
						}
					}
					
					i0 = 0;
					for (int i1 = 1; i1 < 5; i1++) {
						if (i0 == 0 && (i2 & npc.getScriptValue("i_ai2")) == 0) {
							i0 = i2;
							npc.setScriptValue("i_ai2", npc.getScriptValue("i_ai2") + i0);
						} else {
							if (i2 == 1) {
								i2 = 16;
							} else {
								if (i2 == 16) {
									i2 = 256;
								} else {
									if (i2 == 256) {
										i2 = 4096;
									} else {
										if (i2 == 4096) {
											i2 = 1;
										}
									}
								}
							}
						}
					}
					
					long i1 = 3000 + (((60 * 18) - ((System.currentTimeMillis() - npc.getScriptValue("i_ai0")) / 1000)) * 10);
					if (i0 == 1) {
						startQuestTimer("3010", i1, npc, null);
					} else {
						if (i0 == 16) {
							startQuestTimer("3011", i1, npc, null);
						} else {
							if (i0 == 256) {
								startQuestTimer("3012", i1, npc, null);
							} else {
								if (i0 == 4096) {
									startQuestTimer("3013", i1, npc, null);
								}
							}
						}
					}
				}
				
				i0 = (System.currentTimeMillis() - npc.getScriptValue("i_ai0")) / 1000;
				if (i0 > (16 * 60)) {
					npc.setScriptValue("i_quest4", 4);
				} else {
					if (i0 > (13 * 60)) {
						npc.setScriptValue("i_quest4", 3);
					} else {
						if (i0 > (10 * 60)) {
							npc.setScriptValue("i_quest4", 2);
						} else {
							if (i0 > (8 * 60)) {
								npc.setScriptValue("i_quest4", 3);
							} else {
								if (i0 > (6 * 60)) {
									npc.setScriptValue("i_quest4", 2);
								}
							}
						}
					}
				}
				
				i0 = 0;
				if ((npc.getScriptValue("i_quest2") & 1) == 1) {
					i0 = i0 + 1;
				}
				if ((npc.getScriptValue("i_quest2") & 16) == 16) {
					i0 = i0 + 1;
				}
				if ((npc.getScriptValue("i_quest2") & 256) == 256) {
					i0 = i0 + 1;
				}
				if ((npc.getScriptValue("i_quest2") & 4096) == 4096) {
					i0 = i0 + 1;
				}
				
				if (i0 < npc.getScriptValue("i_quest4")) {
					int i2 = getRandom(1, 4);
					if (i2 == 1) {
						i2 = 1;
					} else {
						if (i2 == 2) {
							i2 = 16;
						} else {
							if (i2 == 3) {
								i2 = 256;
							} else {
								if (i2 == 4) {
									i2 = 4096;
								}
							}
						}
					}
					
					i0 = 0;
					for (int i1 = 1; i1 < 5; i1++) {
						if (i0 == 0 && (i2 & npc.getScriptValue("i_quest2")) == 0) {
							i0 = i2;
							npc.setScriptValue("i_quest2", npc.getScriptValue("i_quest2") + i0);
						} else {
							if (i2 == 1) {
								i2 = 16;
							} else {
								if (i2 == 16) {
									i2 = 256;
								} else {
									if (i2 == 256) {
										i2 = 4096;
									} else {
										if (i2 == 4096) {
											i2 = 1;
										}
									}
								}
							}
						}
					}
					
					long i1 = 3000 + (((60 * 18) - ((System.currentTimeMillis() - npc.getScriptValue("i_ai0")) / 1000)) * 10);
					if (i0 == 1) {
						startQuestTimer("3014", i1, npc, null);
					} else {
						if (i0 == 16) {
							startQuestTimer("3015", i1, npc, null);
						} else {
							if (i0 == 256) {
								startQuestTimer("3016", i1, npc, null);
							} else {
								if (i0 == 4096) {
									startQuestTimer("3017", i1, npc, null);
								}
							}
						}
					}
				}
				
				i0 = 0;
				if ((npc.getScriptValue("i_quest3") & 1) == 1) {
					i0 = i0 + 1;
				}
				if ((npc.getScriptValue("i_quest3") & 16) == 16) {
					i0 = i0 + 1;
				}
				if ((npc.getScriptValue("i_quest3") & 256) == 256) {
					i0 = i0 + 1;
				}
				if ((npc.getScriptValue("i_quest3") & 4096) == 4096) {
					i0 = i0 + 1;
				}
				
				if (i0 < npc.getScriptValue("i_quest4")) {
					int i2 = getRandom(1, 4);
					if (i2 == 1) {
						i2 = 1;
					} else {
						if (i2 == 2) {
							i2 = 16;
						} else {
							if (i2 == 3) {
								i2 = 256;
							} else {
								if (i2 == 4) {
									i2 = 4096;
								}
							}
						}
					}
					
					i0 = 0;
					for (int i1 = 1; i1 < 5; i1++) {
						if (i0 == 0 && (i2 & npc.getScriptValue("i_quest3")) == 0) {
							i0 = i2;
							npc.setScriptValue("i_quest3", npc.getScriptValue("i_quest3") + i0);
						} else {
							if (i2 == 1) {
								i2 = 16;
							} else {
								if (i2 == 16) {
									i2 = 256;
								} else {
									if (i2 == 256) {
										i2 = 4096;
									} else {
										if (i2 == 4096) {
											i2 = 1;
										}
									}
								}
							}
						}
					}
					
					long i1 = 3000 + (((60 * 18) - ((System.currentTimeMillis() - npc.getScriptValue("i_ai0")) / 1000)) * 10);
					if (i0 == 1) {
						startQuestTimer("3018", i1, npc, null);
					} else {
						if (i0 == 16) {
							startQuestTimer("3019", i1, npc, null);
						} else {
							if (i0 == 256) {
								startQuestTimer("3020", i1, npc, null);
							} else {
								if (i0 == 4096) {
									startQuestTimer("3021", i1, npc, null);
								}
							}
						}
					}
				}
				
				if (npc.getScriptValue("sm.flag") == 0) {
					if (getRandom(100) < 10) {
						addSpawnEx(battleBombManName, (npc.getX() + 250) + getRandom(100), (npc.getY() + 250) + getRandom(100), npc.getZ(), 0, false, 0, false, npc.getLocation().getX(), npc.getScriptValue("i_ai0"), 0);
						npc.setScriptValue("sm.flag", System.currentTimeMillis());
					} else {
						if (((System.currentTimeMillis() - npc.getScriptValue("sm.flag")) / 1000) > 260 && getRandom(100) < 30) {
							addSpawnEx(battleBombManName, (npc.getX() + 250) + getRandom(100), (npc.getY() + 250) + getRandom(100), npc.getZ(), 0, false, 0, false, npc.getLocation().getX(), npc.getScriptValue("i_ai0"), 0);
							npc.setScriptValue("sm.flag", System.currentTimeMillis());
						}
					}
				}
			}
			
			long i0 = (System.currentTimeMillis() - npc.getScriptValue("i_ai0")) / 1000;
			if (i0 > (14 * 60)) {
				npc.setScriptValue("i_quest1", 4);
			} else {
				if (i0 > (12 * 60)) {
					npc.setScriptValue("i_quest1", 3);
				} else {
					if (i0 > (10 * 60)) {
						npc.setScriptValue("i_quest1", 2);
					} else {
						if (i0 > (8 * 60)) {
							npc.setScriptValue("i_quest1", 3);
						} else {
							if (i0 > (6 * 60)) {
								npc.setScriptValue("i_quest1", 2);
							}
						}
					}
				}
			}
			
			i0 = 0;
			if ((npc.getScriptValue("i_quest0") & 1) == 1) {
				i0 = i0 + 1;
			}
			if ((npc.getScriptValue("i_quest0") & 16) == 16) {
				i0 = i0 + 1;
			}
			if ((npc.getScriptValue("i_quest0") & 256) == 256) {
				i0 = i0 + 1;
			}
			if ((npc.getScriptValue("i_quest0") & 4096) == 4096) {
				i0 = i0 + 1;
			}
			
			if (i0 < npc.getScriptValue("i_quest1")) {
				int i2 = getRandom(1, 4);
				if (i2 == 1) {
					i2 = 1;
				} else {
					if (i2 == 2) {
						i2 = 16;
					} else {
						if (i2 == 3) {
							i2 = 256;
						} else {
							if (i2 == 4) {
								i2 = 4096;
							}
						}
					}
				}
				
				i0 = 0;
				for (int i1 = 1; i1 < 5; i1++) {
					if (i0 == 0 && (i2 & npc.getScriptValue("i_quest0")) == 0) {
						i0 = i2;
						npc.setScriptValue("i_quest0", npc.getScriptValue("i_quest0") + i0);
					} else {
						if (i2 == 1) {
							i2 = 16;
						} else {
							if (i2 == 16) {
								i2 = 256;
							} else {
								if (i2 == 256) {
									i2 = 4096;
								} else {
									if (i2 == 4096) {
										i2 = 1;
									}
								}
							}
						}
					}
				}
				
				long i1 = 3000 + (((60 * 18) - ((System.currentTimeMillis() - npc.getScriptValue("i_ai0")) / 1000)) * 10);
				if (i0 == 1) {
					startQuestTimer("3006", i1, npc, null);
				} else {
					if (i0 == 16) {
						startQuestTimer("3007", i1, npc, null);
					} else {
						if (i0 == 256) {
							startQuestTimer("3008", i1, npc, null);
						} else {
							if (i0 == 4096) {
								startQuestTimer("3009", i1, npc, null);
							}
						}
					}
				}
			}
			
			startQuestTimer("3001", 3000, npc, null);
		}
		
		if (timer.equalsIgnoreCase("3002")) {
			addSpawnEx(battle1NpcName, battle11x, battle11y, coordZ, 32768, false, 0, false, 1, npc.getScriptValue("i_ai0"), 0);
		}
		if (timer.equalsIgnoreCase("3003")) {
			addSpawnEx(battle1NpcName, battle12x, battle12y, coordZ, 32768, false, 0, false, 16, npc.getScriptValue("i_ai0"), 0);
		}
		if (timer.equalsIgnoreCase("3004")) {
			addSpawnEx(battle1NpcName, battle13x, battle13y, coordZ, 32768, false, 0, false, 256, npc.getScriptValue("i_ai0"), 0);
		}
		if (timer.equalsIgnoreCase("3005")) {
			addSpawnEx(battle1NpcName, battle14x, battle14y, coordZ, 32768, false, 0, false, 4096, npc.getScriptValue("i_ai0"), 0);
		}
		if (timer.equalsIgnoreCase("3006")) {
			addSpawnEx(battleSniperName, stair1x, stair1y, coordZ, 32768, false, 0, false, 1, npc.getScriptValue("i_ai0"), 0);
		}
		if (timer.equalsIgnoreCase("3007")) {
			addSpawnEx(battleSniperName, stair2x, stair2y, coordZ, 32768, false, 0, false, 16, npc.getScriptValue("i_ai0"), 0);
		}
		if (timer.equalsIgnoreCase("3008")) {
			addSpawnEx(battleSniperName, stair3x, stair3y, coordZ, 32768, false, 0, false, 256, npc.getScriptValue("i_ai0"), 0);
		}
		if (timer.equalsIgnoreCase("3009")) {
			addSpawnEx(battleSniperName, stair4x, stair4y, coordZ, 32768, false, 0, false, 4096, npc.getScriptValue("i_ai0"), 0);
		}
		var i0 = ((((1.000000 * (System.currentTimeMillis() / 1000)) - npc.getScriptValue("i_ai0")) / (60 * 20)) * 100);
		if (i0 >= getRandom(100)) {
			i0 = 1;
		} else {
			i0 = 0;
		}
		if (Integer.valueOf(timer) >= 3010 && Integer.valueOf(timer) <= 3013) {
			if (i0 == 1) {
				i0 = battleSlowTypeNameEx;
			} else {
				i0 = battleSlowTypeName;
			}
			
			if (timer.equalsIgnoreCase("3010")) {
				addSpawnEx((int) i0, (battle21x - 80) + getRandom(160), (battle21y - 80) + getRandom(160), coordZ, 32768, false, 0, false, npc.getLocation().getX(), npc.getScriptValue("i_ai0"), 1);
			}
			if (timer.equalsIgnoreCase("3011")) {
				addSpawnEx((int) i0, (battle22x - 80) + getRandom(160), (battle22y - 80) + getRandom(160), coordZ, 32768, false, 0, false, npc.getLocation().getX(), npc.getScriptValue("i_ai0"), 16);
			}
			if (timer.equalsIgnoreCase("3012")) {
				addSpawnEx((int) i0, (battle23x - 80) + getRandom(160), (battle23y - 80) + getRandom(160), coordZ, 32768, false, 0, false, npc.getLocation().getX(), npc.getScriptValue("i_ai0"), 256);
			}
			if (timer.equalsIgnoreCase("3013")) {
				addSpawnEx((int) i0, (battle24x - 80) + getRandom(160), (battle24y - 80) + getRandom(160), coordZ, 32768, false, 0, false, npc.getLocation().getX(), npc.getScriptValue("i_ai0"), 4096);
			}
		}
		
		if (Integer.valueOf(timer) >= 3014 && Integer.valueOf(timer) <= 3021) {
			if (i0 == 1) {
				i0 = battleFastTypeNameEx;
			} else {
				i0 = battleFastTypeName;
			}
			
			if (timer.equalsIgnoreCase("3014")) {
				addSpawnEx((int) i0, (battle21x - 80) + getRandom(160), (battle21y - 80) + getRandom(160), coordZ, 32768, false, 0, false, npc.getLocation().getX(), npc.getScriptValue("i_ai0"), 1);
			}
			if (timer.equalsIgnoreCase("3015")) {
				addSpawnEx((int) i0, (battle22x - 80) + getRandom(160), (battle22y - 80) + getRandom(160), coordZ, 32768, false, 0, false, npc.getLocation().getX(), npc.getScriptValue("i_ai0"), 16);
			}
			if (timer.equalsIgnoreCase("3016")) {
				addSpawnEx((int) i0, (battle23x - 80) + getRandom(160), (battle23y - 80) + getRandom(160), coordZ, 32768, false, 0, false, npc.getLocation().getX(), npc.getScriptValue("i_ai0"), 256);
			}
			if (timer.equalsIgnoreCase("3017")) {
				addSpawnEx((int) i0, (battle24x - 80) + getRandom(160), (battle24y - 80) + getRandom(160), coordZ, 32768, false, 0, false, npc.getLocation().getX(), npc.getScriptValue("i_ai0"), 4096);
			}
			if (timer.equalsIgnoreCase("3018")) {
				addSpawnEx((int) i0, (battle21x - 80) + getRandom(160), (battle21y - 80) + getRandom(160), coordZ, 32768, false, 0, false, npc.getLocation().getX(), npc.getScriptValue("i_ai0"), 268435457);
			}
			if (timer.equalsIgnoreCase("3019")) {
				addSpawnEx((int) i0, (battle22x - 80) + getRandom(160), (battle22y - 80) + getRandom(160), coordZ, 32768, false, 0, false, npc.getLocation().getX(), npc.getScriptValue("i_ai0"), 268435472);
			}
			if (timer.equalsIgnoreCase("3020")) {
				addSpawnEx((int) i0, (battle23x - 80) + getRandom(160), (battle23y - 80) + getRandom(160), coordZ, 32768, false, 0, false, npc.getLocation().getX(), npc.getScriptValue("i_ai0"), 268435712);
			}
			if (timer.equalsIgnoreCase("3021")) {
				addSpawnEx((int) i0, (battle24x - 80) + getRandom(160), (battle24y - 80) + getRandom(160), coordZ, 32768, false, 0, false, npc.getLocation().getX(), npc.getScriptValue("i_ai0"), 268439552);
			}
		}
		
		if (timer.equalsIgnoreCase("3022")) {
			instantTeleportInMyTerritory(npc, escapeX, escapeY, coordZ);
			//ClearEventRoom(room_index, part_type, 0);
		}
		
		if (timer.equalsIgnoreCase("3023")) {
			npc.broadcastPacket(PlaySound.createSound("B06_S01"));
			startQuestTimer("3024", 180 * 1000, npc, null);
		}
		
		if (timer.equalsIgnoreCase("3024")) {
			npc.broadcastPacket(PlaySound.createSound("B07_S01"));
			startQuestTimer("3025", 122 * 1000, npc, null);
		}
		
		if (timer.equalsIgnoreCase("3025")) {
			npc.broadcastPacket(PlaySound.createSound("B07_S01"));
			startQuestTimer("3026", 122 * 1000, npc, null);
		}
		
		if (timer.equalsIgnoreCase("3026")) {
			npc.broadcastPacket(PlaySound.createSound("B07_S01"));
			startQuestTimer("3027", 122 * 1000, npc, null);
		}
		
		if (timer.equalsIgnoreCase("3027")) {
			npc.broadcastPacket(PlaySound.createSound("B07_S01"));
			startQuestTimer("3028", 180 * 1000, npc, null);
		}
		
		if (timer.equalsIgnoreCase("3028")) {
			npc.broadcastPacket(PlaySound.createSound("B06_F"));
			startQuestTimer("3029", 120 * 1000, npc, null);
		}
		
		if (timer.equalsIgnoreCase("3029")) {
			npc.broadcastPacket(PlaySound.createSound("B06_F"));
		}
		
		npc.broadcastEvent("10007", 1000, null);
		
		return super.onTimerEvent(timer, npc, player);
	}
	
	@Override
	public void onMenuSelected(PlayerMenuSelected event) {
		final var player = event.player();
		final var npc = event.npc();
		
		final var ask = event.ask();
		final var reply = event.reply();
		
		final var escapeX = npc.getTemplate().getParameters().getInt("escape_x", 0);
		final var escapeY = npc.getTemplate().getParameters().getInt("escape_y", 0);
		final var coordZ = npc.getTemplate().getParameters().getInt("coord_z", 0);
		
		switch (ask) {
			case 505 -> {
				switch (reply) {
					case 1 -> {
						final var party = player.getParty();
						if (party == null) {
							player.teleToLocation(escapeX, escapeY, coordZ);
						} else {
							if (party.getLeaderObjectId() != player.getObjectId()) {
								showPage(player, "ssq_main_event_sibyl_q0505_04.htm");
							} else {
								showPage(player, "ssq_main_event_sibyl_q0505_01.htm");
							}
						}
					}
					case 2 -> {
						final var party = player.getParty();
						if (party == null || party.getLeaderObjectId() != player.getObjectId()) {
							showPage(player, "ssq_main_event_sibyl_q0505_04.htm");
						} else {
							showPage(player, "ssq_main_event_sibyl_q0505_02.htm");
						}
					}
					case 3 -> {
						startQuestTimer("3022", 7000, npc, null);
						npc.broadcastEvent("10015", 1500, npc);
						showPage(player, "ssq_main_event_sibyl_q0505_05.htm");
					}
					case 4 -> {
						if (((System.currentTimeMillis() - npc.getScriptValue("i_ai3")) / 1000) < 10 || npc.getScriptValue("sm.param1") >= 5) {
							showPage(player, "ssq_main_event_sibyl_q0505_03.htm");
						} else {
							final var battleSupporterNameEx = npc.getTemplate().getParameters().getInt("battle_supporter_name_ex", 0);
							final var battleSupporterName = npc.getTemplate().getParameters().getInt("battle_supporter_name", 0);
							
							var i0 = ((npc.getLocation().getX()) - 500) + getRandom(1000);
							var i1 = ((npc.getLocation().getY()) - 500) + getRandom(1000);
							
							if (getRandom(100) < 40) {
								addSpawnEx(battleSupporterName, i0, i1, coordZ, 32768, false, 0, false, 0, npc.getScriptValue("i_ai0"), 0);
							} else {
								addSpawnEx(battleSupporterNameEx, i0, i1, coordZ, 32768, false, 0, false, 0, npc.getScriptValue("i_ai0"), 0);
							}
							
							npc.setScriptValue("sm.param1", npc.getScriptValue("sm.param1") + 1);
							npc.setScriptValue("i_ai3", System.currentTimeMillis());
						}
					}
				}
			}
		}
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player) {
		return switch (npc.getId()) {
			case 31132 -> "dawn_sibyl1001.htm";
			case 31133 -> "dawn_sibyl2001.htm";
			case 31134 -> "dawn_sibyl3001.htm";
			case 31135 -> "dawn_sibyl4001.htm";
			case 31136 -> "dawn_sibyl5001.htm";
			case 31142 -> "dusk_sibyl1001.htm";
			case 31143 -> "dusk_sibyl2001.htm";
			case 31144 -> "dusk_sibyl3001.htm";
			case 31145 -> "dusk_sibyl4001.htm";
			case 31146 -> "dusk_sibyl5001.htm";
			default -> null;
		};
	}
	
	private static void instantTeleportInMyTerritory(L2Npc npc, int escapeX, int escapeY, int coordZ) {
		final var zone = ZoneManager.getInstance().getZoneById(_zoneList.get(npc.getId()));
		for (L2PcInstance pc : zone.getPlayersInside()) {
			if (pc != null) {
				pc.teleToLocation(new Location(escapeX, escapeY, coordZ), 100);
			}
		}
	}
	
	private static void clearEventRoom(L2Npc npc) {
		final var zone = ZoneManager.getInstance().getZoneById(_zoneList.get(npc.getId()));
		for (L2Character chr : zone.getCharactersInside()) {
			if (chr != null && chr instanceof L2FestivalMonsterInstance) {
				chr.deleteMe();
			}
		}
	}
}