package dot.lighteater.upgrade_scrolls.network;

import dot.lighteater.upgrade_scrolls.UpgradeScrolls;
import dot.lighteater.upgrade_scrolls.menu.UpgradeScrollMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record OpenUpgradeScrollMenuPacket()
        implements CustomPacketPayload {

    public static final Type<OpenUpgradeScrollMenuPacket> TYPE =
            new Type<>(
                    ResourceLocation.fromNamespaceAndPath(
                            UpgradeScrolls.MODID,
                            "open_upgrade_scroll_menu"
                    )
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, OpenUpgradeScrollMenuPacket> STREAM_CODEC =
            StreamCodec.unit(new OpenUpgradeScrollMenuPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(
            OpenUpgradeScrollMenuPacket packet,
            IPayloadContext context
    ) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player)) {
                return;
            }

            player.openMenu(
                    new SimpleMenuProvider(
                            (containerId, inventory, serverPlayer) ->
                                    new UpgradeScrollMenu(
                                            containerId,
                                            inventory
                                    ),
                            Component.literal("Upgrade Scrolls")
                    )
            );
        });
    }
}