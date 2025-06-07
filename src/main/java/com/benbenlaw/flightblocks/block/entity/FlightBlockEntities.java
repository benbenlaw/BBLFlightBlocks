package com.benbenlaw.flightblocks.block.entity;

import com.benbenlaw.flightblocks.FlightBlocks;
import com.benbenlaw.flightblocks.block.FlightBlock;
import com.benbenlaw.flightblocks.block.FlightBlocksBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.function.Supplier;

public class FlightBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, FlightBlocks.MOD_ID);

    public static final Supplier<BlockEntityType<FlightBlockEntity>> FLIGHT_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("flight_block_entity",
                    () -> new BlockEntityType<>(FlightBlockEntity::new,FlightBlocksBlocks.FLIGHT_BLOCK.get()));


}
