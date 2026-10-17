package dot.lighteater.upgrade_scrolls.event;

import dot.lighteater.upgrade_scrolls.menu.UpgradeScrollScreen;
import dot.lighteater.upgrade_scrolls.menu.UpgradeScrollsMenus;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(value = Dist.CLIENT)
public final class ClientSetup {

    private ClientSetup() {
    }

    @SubscribeEvent
    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(
                UpgradeScrollsMenus.UPGRADE_SCROLL_MENU.get(),
                UpgradeScrollScreen::new
        );
    }
}