package com.benbenlaw.flightblocks.item;

import com.benbenlaw.flightblocks.FlightBlocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class FlightBlocksItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(FlightBlocks.MOD_ID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
