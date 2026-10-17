package dot.lighteater.upgrade_scrolls.event;

import dot.lighteater.upgrade_scrolls.UpgradeScrolls;
import dot.lighteater.upgrade_scrolls.network.ModNetwork;
import dot.lighteater.upgrade_scrolls.network.OpenUpgradeScrollMenuPacket;
import dot.lighteater.upgrade_scrolls.utility.ModKeyBindings;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(
        modid = UpgradeScrolls.MODID,
        value = Dist.CLIENT
)
public final class ClientEvents {

    private ClientEvents() {
    }

    @SubscribeEvent
    public static void clientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();

        while (ModKeyBindings.OPEN_SCROLLS.consumeClick()) {
            if (minecraft.player != null) {
                PacketDistributor.sendToServer(new OpenUpgradeScrollMenuPacket());
            }
        }
    }
}