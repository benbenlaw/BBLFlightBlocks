package com.benbenlaw.flightblocks.event;

import com.benbenlaw.flightblocks.FlightBlocks;
import com.benbenlaw.flightblocks.block.entity.FlightBlockEntity;
import com.benbenlaw.flightblocks.config.StartupConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.HashSet;
import java.util.Set;

@EventBusSubscriber(modid = FlightBlocks.MOD_ID)
public class FlightBlockEventHandler {
        private static final Set<ServerPlayer> playersWithFlightEnabled = new HashSet<>();
        private static final int RANGE = StartupConfig.flightBlockRange.get();; // Define your flight block range here

        @SubscribeEvent
        public static void onPlayerTick(PlayerTickEvent.Post event) {
                Player player = event.getEntity();

                // Enable flight if player is within range of any flight block
                if (!player.level().isClientSide()) {
                    ServerLevel level = (ServerLevel) player.level();
                    if (isPlayerNearFlightBlock(player, level)) {
                        enableFlight(player);
                    } else {
                        disableFlight(player);
                    }

            }
        }

        @SubscribeEvent
        public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
            ServerPlayer player = (ServerPlayer) event.getEntity();
            Level newLevel = player.level();

            // When player changes dimensions, check if they should have flight enabled
            if (!newLevel.isClientSide()) {
                ServerLevel serverLevel = (ServerLevel) newLevel;
                if (isPlayerNearFlightBlock(player, serverLevel)) {
                    enableFlight(player);
                } else {
                    disableFlight(player);
                }
            }
        }

        private static boolean isPlayerNearFlightBlock(Player player, ServerLevel level) {
            // Check if the player is within range of any active flight blocks in the given level
            for (FlightBlockEntity blockEntity : FlightBlockEntity.ACTIVE_BLOCKS) {
                if (blockEntity.getLevel() == level) {
                    AABB range = new AABB(blockEntity.getBlockPos()).inflate(RANGE);
                    if (range.contains(player.position())) {
                        return true;
                    }
                }
            }
            return false;
        }

        private static void enableFlight(Player player) {
            if (!player.isCreative() && !player.getAbilities().mayfly) {
                player.getAbilities().mayfly = true;
                player.onUpdateAbilities();
                player.sendSystemMessage(Component.translatable("block.flightblocks.flight_block.enabled_flight").withStyle(ChatFormatting.GREEN));
                playersWithFlightEnabled.add((ServerPlayer) player);
            }
        }

        private static void disableFlight(Player player) {
            if (!player.isCreative() && player.getAbilities().mayfly) {
                player.getAbilities().mayfly = false;
                player.getAbilities().flying = false;
                player.onUpdateAbilities();
                player.sendSystemMessage(Component.translatable("block.flightblocks.flight_block.disabled_flight").withStyle(ChatFormatting.RED));
                playersWithFlightEnabled.remove(player);
            }
        }
    }

    /*

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

    @SubscribeEvent
    public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
    }

     */

