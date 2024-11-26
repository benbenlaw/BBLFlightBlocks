package com.benbenlaw.flightblocks;


import com.benbenlaw.flightblocks.block.FlightBlock;
import com.benbenlaw.flightblocks.block.FlightBlocksBlocks;
import com.benbenlaw.flightblocks.block.entity.FlightBlockEntities;
import com.benbenlaw.flightblocks.config.StartupConfig;
import com.benbenlaw.flightblocks.item.FlightBlocksItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
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


        /*

        ModItems.register(eventBus);
        ModBlocks.register(eventBus);
        ModBlockEntities.register(eventBus);
        ModMenuTypes.register(eventBus);
        ModRecipes.register(eventBus);
        ModCreativeTab.register(eventBus);

        eventBus.addListener(this::setup);
        eventBus.addListener(this::enqueueIMC);
        eventBus.addListener(this::processIMC);
        eventBus.addListener(this::setup);
        eventBus.addListener(this::doClientStuff);
        eventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

         */



    }

    private void addItemToCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(FlightBlocksBlocks.FLIGHT_BLOCK.get());
        }
    }


    /*


    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getName());
    }


    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ModMessages::register);

    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("com", "helloworld", () -> {
            LOGGER.info("Hello world from the MDK");
            return "Hello world";
        });
    }

    private void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m -> m.messageSupplier().get()).
                collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    private void doClientStuff(final FMLClientSetupEvent event) {

        event.enqueueWork(() -> {

            MenuScreens.register(ModMenuTypes.MINER_MENU.get(), MinerScreen::new);
            MenuScreens.register(ModMenuTypes.TREE_ABSORBER_MENU.get(), TreeAbsorberScreen::new);
            MenuScreens.register(ModMenuTypes.FLUID_ABSORBER_MENU.get(), FluidAbsorberScreen::new);
            MenuScreens.register(ModMenuTypes.CRUSHER_MENU.get(), CrusherScreen::new);

        });
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus =Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                //      ModItemProperties.addCustomItemProperties();
                //     ModItemProperties.addCustomItemProperties();
            });
        }


    }

     */

}
