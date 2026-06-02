package dot.lighteater.upgrade_scrolls;

import dot.lighteater.upgrade_scrolls.data.ModDataComponents;
import dot.lighteater.upgrade_scrolls.item.ModItems;
import dot.lighteater.upgrade_scrolls.scroll_procedures.AffixLoader;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(UnionsUpgradeScrolls.MODID)
public class UnionsUpgradeScrolls {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "upgrade_scrolls";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "upgrade_scrolls" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "upgrade_scrolls" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Creates a new Block with the id "upgrade_scrolls:example_block", combining the namespace and path
    public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock("example_block", BlockBehaviour.Properties.of().mapColor(MapColor.STONE));
    // Creates a new BlockItem with the id "upgrade_scrolls:example_block", combining the namespace and path
//    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);

    // Creates a creative tab with the id "upgrade_scrolls:example_tab" for the example item, that is placed after the combat tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.upgrade_scrolls")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ModItems.UPGRADE_SCROLL_3.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ModItems.UPGRADE_SCROLL_0);
                output.accept(ModItems.UPGRADE_SCROLL_1);
                output.accept(ModItems.UPGRADE_SCROLL_2);
                output.accept(ModItems.UPGRADE_SCROLL_3);
                output.accept(ModItems.UPGRADE_SCROLL_4);
                output.accept(ModItems.UPGRADE_SCROLL_5);
                output.accept(ModItems.UPGRADE_SCROLL_6);
                output.accept(ModItems.UPGRADE_SCROLL_7);
                output.accept(ModItems.UPGRADE_SCROLL_8);
                output.accept(ModItems.UPGRADE_SCROLL_9);
                output.accept(ModItems.UPGRADE_SCROLL_10);
                output.accept(ModItems.UPGRADE_SCROLL_11);
                output.accept(ModItems.UPGRADE_SCROLL_12);
                output.accept(ModItems.UPGRADE_SCROLL_13);
                output.accept(ModItems.UPGRADE_SCROLL_14);
                output.accept(ModItems.UPGRADE_SCROLL_15);
                output.accept(ModItems.UPGRADE_SCROLL_16);
                output.accept(ModItems.UPGRADE_SCROLL_17);
                output.accept(ModItems.UPGRADE_SCROLL_18);
                output.accept(ModItems.UPGRADE_SCROLL_19);
                output.accept(ModItems.UPGRADE_SCROLL_20);
                output.accept(ModItems.UPGRADE_SCROLL_21);
                output.accept(ModItems.UPGRADE_SCROLL_22);
                output.accept(ModItems.UPGRADE_SCROLL_23);
                output.accept(ModItems.UPGRADE_SCROLL_24);
            }).build());

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public UnionsUpgradeScrolls(IEventBus modEventBus, ModContainer modContainer) {

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ModItems.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);

        ModDataComponents.DATA_COMPONENTS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (UnionsUpgradeScrolls) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        NeoForge.EVENT_BUS.addListener(this::onAddReloadListeners);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    private void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new AffixLoader());
    }
}
