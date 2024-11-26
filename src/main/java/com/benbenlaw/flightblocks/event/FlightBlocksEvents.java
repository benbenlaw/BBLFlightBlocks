package com.benbenlaw.flightblocks.event;

import com.benbenlaw.flightblocks.FlightBlocks;
import com.benbenlaw.flightblocks.block.entity.FlightBlockEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
@EventBusSubscriber(modid = FlightBlocks.MOD_ID)
public class FlightBlocksEvents {

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        Level level = event.getEntity().level();

        if (!level.isClientSide()) {
            for (FlightBlockEntity blockEntity : FlightBlockEntity.ACTIVE_BLOCKS) {
                AABB range = new AABB(blockEntity.getBlockPos()).inflate(FlightBlockEntity.RANGE);
                if (range.contains(player.position())) {
                    blockEntity.enableFlight(player);
                    break;
                }
            }
        }
    }
}
