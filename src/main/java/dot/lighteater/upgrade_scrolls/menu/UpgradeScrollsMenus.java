package dot.lighteater.upgrade_scrolls.menu;

import dot.lighteater.upgrade_scrolls.UpgradeScrolls;
import dot.lighteater.upgrade_scrolls.menu.UpgradeScrollMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class UpgradeScrollsMenus {

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(
                    Registries.MENU,
                    UpgradeScrolls.MODID
            );

    public static final Supplier<MenuType<UpgradeScrollMenu>>
            UPGRADE_SCROLL_MENU =
            MENUS.register(
                    "upgrade_scroll_menu",
                    () -> new MenuType<>(
                            UpgradeScrollMenu::new,
                            net.minecraft.world.flag.FeatureFlags.DEFAULT_FLAGS
                    )
            );
}