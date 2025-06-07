package com.benbenlaw.flightblocks.block.entity;

import com.benbenlaw.flightblocks.config.StartupConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.benbenlaw.flightblocks.event.FlightBlockEventHandler.playersWithFlightEnabled;

public class FlightBlockEntity extends BlockEntity {

    public static final int RANGE = StartupConfig.flightBlockRange.get();
    private final Set<ServerPlayer> enabledPlayers = new HashSet<>();
    public static final Set<FlightBlockEntity> ACTIVE_BLOCKS = new HashSet<>();
    private boolean showRange = false;

    public FlightBlockEntity(BlockPos pos, BlockState state) {
        super(FlightBlockEntities.FLIGHT_BLOCK_ENTITY.get(), pos, state);
        ACTIVE_BLOCKS.add(this);
    }

    public void tick() {
        assert level != null;
        if (!level.isClientSide()) {
            if (showRange) {
                ServerLevel serverLevel = (ServerLevel) level;

                if (level.getGameTime() % 40 == 0) {
                    for (ServerPlayer player : playersWithFlightEnabled) {
                        showFlightRangeOutline(serverLevel, player);
                    }
                }
            }
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        ACTIVE_BLOCKS.remove(this);
    }

    public void onRightClick(ServerPlayer player) {

        if (showRange) {
            showRange = false;
            Level level = this.getLevel();
            if (level != null && !level.isClientSide()) {
                //showFlightRange((ServerLevel) level, player); //old
                showFlightRangeOutline((ServerLevel) level, player);
            }
        } else {
            showRange = true;
            player.sendSystemMessage(Component.translatable("block.flightblocks.flight_block.range", RANGE));
        }
    }

    private void showFlightRangeOutline(ServerLevel level, ServerPlayer player) {
        AABB range = new AABB(this.worldPosition).inflate(RANGE, RANGE, RANGE);

        int minX = (int) Math.floor(range.minX);
        int minY = (int) Math.floor(range.minY);
        int minZ = (int) Math.floor(range.minZ);
        int maxX = (int) Math.ceil(range.maxX);
        int maxY = (int) Math.ceil(range.maxY);
        int maxZ = (int) Math.ceil(range.maxZ);

        // Particle count density - adjust for performance/visuals
        int step = 1;

        // Along X edges (4 edges)
        for (int x = minX; x <= maxX; x += step) {
            spawnParticle(level, player, x + 0.5, minY + 0.5, minZ + 0.5);
            spawnParticle(level, player, x + 0.5, minY + 0.5, maxZ + 0.5);
            spawnParticle(level, player, x + 0.5, maxY + 0.5, minZ + 0.5);
            spawnParticle(level, player, x + 0.5, maxY + 0.5, maxZ + 0.5);
        }

        // Along Y edges (4 edges)
        for (int y = minY; y <= maxY; y += step) {
            spawnParticle(level, player, minX + 0.5, y + 0.5, minZ + 0.5);
            spawnParticle(level, player, minX + 0.5, y + 0.5, maxZ + 0.5);
            spawnParticle(level, player, maxX + 0.5, y + 0.5, minZ + 0.5);
            spawnParticle(level, player, maxX + 0.5, y + 0.5, maxZ + 0.5);
        }

        // Along Z edges (4 edges)
        for (int z = minZ; z <= maxZ; z += step) {
            spawnParticle(level, player, minX + 0.5, minY + 0.5, z + 0.5);
            spawnParticle(level, player, minX + 0.5, maxY + 0.5, z + 0.5);
            spawnParticle(level, player, maxX + 0.5, minY + 0.5, z + 0.5);
            spawnParticle(level, player, maxX + 0.5, maxY + 0.5, z + 0.5);
        }
    }

    private void spawnParticle(ServerLevel level, ServerPlayer player, double x, double y, double z) {
        ClientboundLevelParticlesPacket packet = new ClientboundLevelParticlesPacket(
                ParticleTypes.END_ROD,
                true,
                true,
                x + 0.5, y + 0.5, z + 0.5,
                1,
                0.0f, 0.0f, 0.0f,
                1
        );

        player.connection.send(packet);

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

                        ClientboundLevelParticlesPacket packet = new ClientboundLevelParticlesPacket(
                                ParticleTypes.END_ROD,
                                true,
                                true,
                                x + 0.5, y + 0.5, z + 0.5,
                                1,
                                0.0f, 0.0f, 0.0f,
                                1
                        );

                        player.connection.send(packet);

                    }
                }
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        compoundTag.putBoolean("showRange", showRange);
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        showRange = compoundTag.getBooleanOr("showRange", false);
        super.loadAdditional(compoundTag, provider);
    }
}
