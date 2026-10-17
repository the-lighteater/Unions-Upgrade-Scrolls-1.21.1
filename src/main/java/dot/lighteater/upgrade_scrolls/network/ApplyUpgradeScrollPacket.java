package dot.lighteater.upgrade_scrolls.network;

import dot.lighteater.upgrade_scrolls.UpgradeScrolls;
import dot.lighteater.upgrade_scrolls.item.UpgradeScroll_Item;
import dot.lighteater.upgrade_scrolls.menu.UpgradeScrollMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ApplyUpgradeScrollPacket(int scrollSlot)
        implements CustomPacketPayload {

    public static final Type<ApplyUpgradeScrollPacket> TYPE =
            new Type<>(
                    ResourceLocation.fromNamespaceAndPath(
                            UpgradeScrolls.MODID,
                            "apply_upgrade_scroll"
                    )
            );

    public static final StreamCodec<
            RegistryFriendlyByteBuf,
            ApplyUpgradeScrollPacket
            > STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT,
                    ApplyUpgradeScrollPacket::scrollSlot,
                    ApplyUpgradeScrollPacket::new
            );

    @Override
    public Type<ApplyUpgradeScrollPacket> type() {
        return TYPE;
    }

    public static void handle(
            ApplyUpgradeScrollPacket packet,
            IPayloadContext context
    ) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                AbstractContainerMenu menu = player.containerMenu;

                if (!(menu instanceof UpgradeScrollMenu scrollMenu)) {
                    return;
                }

                performApply(
                        player,
                        scrollMenu,
                        packet.scrollSlot()
                );
            }
        });
    }

    private static void performApply(
            ServerPlayer player,
            UpgradeScrollMenu menu,
            int scrollSlot
    ) {
        /*
         * Only these four slots can be used as scroll slots.
         */
        if (scrollSlot != UpgradeScrollMenu.SCROLL_SLOT_1
                && scrollSlot != UpgradeScrollMenu.SCROLL_SLOT_2
                && scrollSlot != UpgradeScrollMenu.SCROLL_SLOT_3
                && scrollSlot != UpgradeScrollMenu.SCROLL_SLOT_4) {

            menu.setResult(2);
            menu.broadcastChanges();
            return;
        }

        /*
         * 0 = no result
         * 1 = success
         * 2 = failed
         */
        menu.setResult(0);

        ItemStack scroll = menu.getScroll(scrollSlot);
        ItemStack target = menu.getApplicationItem();

        if (scroll.isEmpty() || target.isEmpty()) {
            menu.setResult(2);
            menu.broadcastChanges();
            return;
        }

        /*
         * Verify that this is one of our upgrade scrolls.
         */
        if (!(scroll.getItem() instanceof UpgradeScroll_Item scrollItem)) {
            menu.setResult(2);
            menu.broadcastChanges();
            return;
        }

        /*
         * Verify that the scroll is valid for this slot.
         */
        if (!menu.getSlot(scrollSlot).mayPlace(scroll)) {
            menu.setResult(2);
            menu.broadcastChanges();
            return;
        }

        /*
         * Verify that the target is still valid.
         */
        if (!menu.getSlot(UpgradeScrollMenu.APPLICATION_SLOT)
                .mayPlace(target)) {
            menu.setResult(2);
            menu.broadcastChanges();
            return;
        }

        int scrollId = scrollItem.getScrollId();

        UpgradeScrolls.LOGGER.debug(
                "[ScrollMenu] Applying scroll | player={} | slot={} | scrollId={} | target={}",
                player.getGameProfile().getName(),
                scrollSlot,
                scrollId,
                target.getItem()
        );

        /*
         * Run the existing server-side effect.
         * The existing procedure handles consumption.
         */
        boolean success = executeScroll(
                player,
                scrollId,
                target,
                scroll
        );

        menu.setResult(success ? 1 : 2);
        menu.broadcastChanges();
    }

    private static boolean executeScroll(
            ServerPlayer player,
            int scrollId,
            ItemStack target,
            ItemStack scroll
    ) {
        return UpgradeScroll_Item.runScrollEffectFromMenu(
                player,
                scrollId,
                target,
                scroll
        );
    }
}