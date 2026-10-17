package dot.lighteater.upgrade_scrolls.network;

import dot.lighteater.upgrade_scrolls.UpgradeScrolls;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@EventBusSubscriber(modid = UpgradeScrolls.MODID)
public final class ModNetwork {

    private ModNetwork() {
    }

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        event.registrar("1")
                .playToClient(
                        ScrollAnimationPacket.TYPE,
                        ScrollAnimationPacket.STREAM_CODEC,
                        ScrollAnimationPacket::handle
                )
                .playToClient(
                        AffixSyncPayload.TYPE,
                        AffixSyncPayload.STREAM_CODEC,
                        AffixSyncPayload::handle
                )
                .playToServer(
                    ApplyUpgradeScrollPacket.TYPE,
                    ApplyUpgradeScrollPacket.STREAM_CODEC,
                    ApplyUpgradeScrollPacket::handle
                )
                .playToServer(
                    OpenUpgradeScrollMenuPacket.TYPE,
                    OpenUpgradeScrollMenuPacket.STREAM_CODEC,
                    OpenUpgradeScrollMenuPacket::handle
                );
    }
}