package com.benbenlaw.flightblocks.block.entity;

import com.benbenlaw.flightblocks.config.StartupConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.extensions.IPlayerExtension;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FlightBlockEntity extends BlockEntity {

    private static final int RANGE = StartupConfig.flightBlockRange.get();
    private final Set<ServerPlayer> enabledPlayers = new HashSet<>();
    private static final Set<FlightBlockEntity> ACTIVE_BLOCKS = new HashSet<>();

    public FlightBlockEntity(BlockPos pos, BlockState state) {
        super(FlightBlockEntities.FLIGHT_BLOCK_ENTITY.get(), pos, state);
        ACTIVE_BLOCKS.add(this);
    }

    public void tick() {
        Level level = this.getLevel();
        assert level != null;
        //int activeBlocks = ACTIVE_BLOCKS.size();
        //System.out.println(activeBlocks);

        if (!level.isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) level;
            AABB aabb = new AABB(this.worldPosition).inflate(RANGE);
            List<ServerPlayer> playersInRange = serverLevel.getEntitiesOfClass(ServerPlayer.class, aabb);

            for (ServerPlayer player : playersInRange) {
                if (!enabledPlayers.contains(player)) {
                    enableFlight(player);
                    enabledPlayers.add(player);
                }
            }

            enabledPlayers.removeIf(player -> {
                if (!playersInRange.contains(player)) {
                    if (!isPlayerInRangeOfAnyBlock(player)) {
                        disableFlight(player);
                    }
                    return true;
                }
                return false;
            });
        }
    }

    private boolean isPlayerInRangeOfAnyBlock(ServerPlayer player) {
        for (FlightBlockEntity blockEntity : ACTIVE_BLOCKS) {
            if (blockEntity != this) {
                AABB blockRange = new AABB(blockEntity.getBlockPos()).inflate(RANGE);
                if (blockRange.contains(player.position())) {
                    return true;
                }
            }
        }
        return false;
    }


    private void enableFlight(ServerPlayer player) {
        if (!player.isCreative()) {
            if (!player.getAbilities().mayfly) {
                player.getAbilities().mayfly = true;
                player.onUpdateAbilities();
                player.sendSystemMessage(Component.translatable(
                        "block.flightblocks.flight_block.enabled_flight").withStyle(ChatFormatting.GREEN));
            }
        }
    }

    private void disableFlight(ServerPlayer player) {
        if (!player.isCreative()) {
            if (player.getAbilities().mayfly) {
                player.getAbilities().mayfly = false;
                player.getAbilities().flying = false;
                player.onUpdateAbilities();
                player.sendSystemMessage(Component.translatable(
                        "block.flightblocks.flight_block.disabled_flight").withStyle(ChatFormatting.RED));
            }
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        for (ServerPlayer player : enabledPlayers) {
            disableFlight(player);
        }
        ACTIVE_BLOCKS.remove(this);
    }

    public InteractionResult onRightClick(ServerPlayer player) {
        Level level = this.getLevel();
        if (level != null && !level.isClientSide()) {
            showFlightRange((ServerLevel) level, player);
            player.sendSystemMessage(Component.translatable("block.flightblocks.flight_block.range").append(String.valueOf(RANGE)));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    private void showFlightRange(ServerLevel level, ServerPlayer player) {
        AABB range = new AABB(this.worldPosition).inflate(RANGE, RANGE, RANGE);

        double minX = range.minX;
        double minY = range.minY;
        double minZ = range.minZ;
        double maxX = range.maxX;
        double maxY = range.maxY;
        double maxZ = range.maxZ;

        for (double x = minX; x < maxX; x++) {
            for (double y = minY; y < maxY; y++) {
                for (double z = minZ; z < maxZ; z++) {
                    if (x == minX || x == maxX - 1 || y == minY || y == maxY - 1 || z == minZ || z == maxZ - 1) {
                        level.sendParticles(
                                player,
                                ParticleTypes.END_ROD,
                                true,
                                x + 0.5, y + 0.5, z + 0.5,
                                1,
                                0.0, 0.0, 0.0,
                                0.0
                        );
                    }
                }
            }
        }
    }
}