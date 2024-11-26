package com.benbenlaw.flightblocks;


import com.benbenlaw.flightblocks.block.FlightBlock;
import com.benbenlaw.flightblocks.block.FlightBlocksBlocks;
import com.benbenlaw.flightblocks.block.entity.FlightBlockEntities;
import com.benbenlaw.flightblocks.block.entity.FlightBlockEntity;
import com.benbenlaw.flightblocks.config.StartupConfig;
import com.benbenlaw.flightblocks.item.FlightBlocksItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.apache.logging.log4j.LogManager;

@Mod(FlightBlocks.MOD_ID)
public class FlightBlocks{

    public static final String MOD_ID = "flightblocks";
    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();

    public FlightBlocks(final IEventBus eventBus, final ModContainer modContainer) {

        FlightBlocksItems.ITEMS.register(eventBus);
        FlightBlocksBlocks.BLOCKS.register(eventBus);
        FlightBlockEntities.BLOCK_ENTITIES.register(eventBus);

        modContainer.registerConfig(ModConfig.Type.STARTUP, StartupConfig.SPEC, "bbl/flightblocks/startup.toml");

        eventBus.addListener(this::addItemToCreativeTab);
    }

    private void addItemToCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(FlightBlocksBlocks.FLIGHT_BLOCK.get());
        }
    }

}
