package com.benbenlaw.flightblocks.item;

import com.benbenlaw.flightblocks.FlightBlocks;
import com.benbenlaw.flightblocks.block.FlightBlocksBlocks;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class FlightBlocksItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(FlightBlocks.MOD_ID);

    public static final DeferredItem<Item> FLIGHT_BLOCK = ITEMS.registerItem(
            "flight_block",
            props -> new FlightBlockItem(FlightBlocksBlocks.FLIGHT_BLOCK.get(), props),
            new Item.Properties()
    );

}
