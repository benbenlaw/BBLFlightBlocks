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
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@EventBusSubscriber(modid = FlightBlocks.MOD_ID)
public class FlightBlockEventHandler {
    public static final Set<ServerPlayer> playersWithFlightEnabled = new HashSet<>();
    private static final int RANGE = StartupConfig.flightBlockRange.get();; // Define your flight block range here

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {

        Player player = event.getEntity();

        // Enable flight if player is within range of any flight block
        if (!player.level().isClientSide()) {
            ServerLevel level = (ServerLevel) player.level();

            if (level.getGameTime() % 20 == 0) { // Check every second
                if (isPlayerNearFlightBlock(player, level)) {
                    enableFlight(player);
                } else {
                    disableFlight(player);
                }
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
        if (!player.isCreative() && !player.isSpectator() && !player.getAbilities().mayfly) {
            player.addTag("flight_blocks_flight");
            player.getAbilities().mayfly = true;
            player.onUpdateAbilities();
            player.sendSystemMessage(Component.translatable("block.flightblocks.flight_block.enabled_flight").withStyle(ChatFormatting.GREEN));
            playersWithFlightEnabled.add((ServerPlayer) player);
        }
    }

    private static void disableFlight(Player player) {
        if (!player.isCreative() && !player.isSpectator() && player.getAbilities().mayfly && player.getTags().contains("flight_blocks_flight")) {
            player.removeTag("flight_blocks_flight");
            player.getAbilities().mayfly = false;
            player.onUpdateAbilities();
            player.sendSystemMessage(Component.translatable("block.flightblocks.flight_block.disabled_flight").withStyle(ChatFormatting.RED));
            playersWithFlightEnabled.remove(player);
        }
    }
}


