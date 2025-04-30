package com.andrewgaming.aputils;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(AndrewsDatapackUtilities.MOD_ID)
public class AndrewsDatapackUtilities {
    public static final String MOD_ID = "andrewspackutilities"; // It's better to define MOD_ID directly here
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public AndrewsDatapackUtilities(IEventBus eventBus) {
        SetupCommands.registerCommands(); // Register commands via event bus
        LOGGER.info("Loaded Andrew's Pack Utilities on side: {}", FMLLoader.getDist());
    }
}