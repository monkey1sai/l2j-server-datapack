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
package com.l2jserver.datapack.custom.service;

import static com.l2jserver.gameserver.config.Configuration.teleporterService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.datapack.custom.service.buffer.BufferService;
import com.l2jserver.datapack.custom.service.buffer.BufferServiceRepository;
import com.l2jserver.datapack.custom.service.teleporter.TeleporterService;
import com.l2jserver.datapack.custom.service.teleporter.model.TeleporterConfig;
import com.l2jserver.gameserver.config.Configuration;
import com.l2jserver.gameserver.scripting.ScriptLoader;

/**
 * Loader for {@link BufferService} and {@link TeleporterService}
 * @author Kita (Noé Caratini)
 */
public class ServiceScriptLoader implements ScriptLoader {
	private static final Logger LOG = LoggerFactory.getLogger(ServiceScriptLoader.class);
	
	@Override
	public void load() {
		if (!Configuration.bufferService().enable()) {
			LOG.info("Buffer service disabled.");
		} else {
			LOG.info("Loading buffer service");
			BufferServiceRepository.getInstance().getConfig().registerNpcs(BufferService.getInstance());
		}
		
		if (!teleporterService().enable()) {
			LOG.info("Teleporter service disabled.");
		} else {
			LOG.info("Loading teleporter service");
			try {
				new TeleporterService(new TeleporterConfig());
			} catch (Exception e) {
				LOG.error("Failed to load teleporter service!", e);
			}
		}
		
	}
}
