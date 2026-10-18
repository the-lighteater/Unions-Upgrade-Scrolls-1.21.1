
package dot.lighteater.upgrade_scrolls.network;

import dot.lighteater.upgrade_scrolls.ClientConfig;
import dot.lighteater.upgrade_scrolls.UpgradeScrolls;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ScrollAnimationPacket(ItemStack stack)
        implements CustomPacketPayload {

    public static final Type<ScrollAnimationPacket> TYPE =
            new Type<>(
                    ResourceLocation.fromNamespaceAndPath(
                            UpgradeScrolls.MODID,
                            "scroll_animation"
                    )
            );

    public static final StreamCodec<
            RegistryFriendlyByteBuf,
            ScrollAnimationPacket
            > STREAM_CODEC =
            StreamCodec.composite(
                    ItemStack.STREAM_CODEC,
                    ScrollAnimationPacket::stack,
                    ScrollAnimationPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(
            ScrollAnimationPacket payload,
            IPayloadContext context
    ) {
        context.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();

            if (minecraft.player != null) {
                if (ClientConfig.ALLOW_ANIMATION.get()) {
                    minecraft.gameRenderer.displayItemActivation(
                            payload.stack()
                    );
                }
            }
        });
    }
}