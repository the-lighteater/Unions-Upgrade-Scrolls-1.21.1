package dot.lighteater.upgrade_scrolls;

import dot.lighteater.upgrade_scrolls.compat.CurioAffixProcedure;
import dot.lighteater.upgrade_scrolls.data.ModDataComponents;
import dot.lighteater.upgrade_scrolls.item.ModItems;
import dot.lighteater.upgrade_scrolls.menu.UpgradeScrollsMenus;
import dot.lighteater.upgrade_scrolls.affix.AffixLoader;
import dot.lighteater.upgrade_scrolls.utility.CursedConfigLoader;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(UpgradeScrolls.MODID)
public class UpgradeScrolls {

    public static final String MODID = "upgrade_scrolls";

    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static boolean curioLoaded = false;

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> UPGRADE_SCROLLS_TAB = CREATIVE_MODE_TABS.register("upgrade_scrolls_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.upgrade_scrolls"))
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

                // Curios compatibility
                if (ModList.get().isLoaded("curios")) {
                    output.accept(ModItems.UPGRADE_SCROLL_22.get());
                    output.accept(ModItems.UPGRADE_SCROLL_23.get());
                    output.accept(ModItems.UPGRADE_SCROLL_24.get());
                }
            }).build());

    public UpgradeScrolls(IEventBus modEventBus, ModContainer modContainer) {

        BLOCKS.register(modEventBus);

        ModItems.register(modEventBus);

        CREATIVE_MODE_TABS.register(modEventBus);

        ModDataComponents.DATA_COMPONENTS.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);

        UpgradeScrollsMenus.MENUS.register(modEventBus);

        if (ModList.get().isLoaded("curios")) {
            NeoForge.EVENT_BUS.register(CurioAffixProcedure.class);
            curioLoaded = true;
        }

        NeoForge.EVENT_BUS.addListener(this::onAddReloadListeners);

        CursedConfigLoader.load();

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    private void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new AffixLoader());
        CursedConfigLoader.load();
    }
}
