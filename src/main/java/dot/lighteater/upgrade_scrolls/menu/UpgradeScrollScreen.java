package dot.lighteater.upgrade_scrolls.menu;

import dot.lighteater.upgrade_scrolls.UpgradeScrolls;
import dot.lighteater.upgrade_scrolls.network.ApplyUpgradeScrollPacket;
import dot.lighteater.upgrade_scrolls.network.ModNetwork;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Objects;

import static dot.lighteater.upgrade_scrolls.menu.UpgradeScrollMenu.*;

public class UpgradeScrollScreen
        extends AbstractContainerScreen<UpgradeScrollMenu> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    UpgradeScrolls.MODID,
                    "textures/gui/upgrade_scroll_menu.png"
            );

    private Button affixButton;
    private Button cursedButton;
    private Button goldenButton;
    private Button holyButton;

    public UpgradeScrollScreen(
            UpgradeScrollMenu menu,
            Inventory inventory,
            Component title
    ) {
        super(
                menu,
                inventory,
                title
        );

        imageWidth = 176;
        imageHeight = 220;
    }

    @Override
    protected void init() {
        super.init();

        /*
         * AFFIX
         */
        affixButton =
                addRenderableWidget(
                        Button.builder(
                                Component.literal("Apply"),
                                button ->
                                        onApplyClicked(
                                                SCROLL_SLOT_1
                                        )
                        ).bounds(
                                leftPos + 70,
                                topPos + 45,
                                38,
                                16
                        ).build()
                );

        /*
         * CURSED
         */
        cursedButton =
                addRenderableWidget(
                        Button.builder(
                                Component.literal("Apply"),
                                button ->
                                        onApplyClicked(
                                                SCROLL_SLOT_2
                                        )
                        ).bounds(
                                leftPos + 130,
                                topPos + 45,
                                38,
                                16
                        ).build()
                );

        /*
         * GOLDEN
         */
        goldenButton =
                addRenderableWidget(
                        Button.builder(
                                Component.literal("Apply"),
                                button ->
                                        onApplyClicked(
                                                SCROLL_SLOT_3
                                        )
                        ).bounds(
                                leftPos + 70,
                                topPos + 85,
                                38,
                                16
                        ).build()
                );

        /*
         * HOLY
         */
        holyButton =
                addRenderableWidget(
                        Button.builder(
                                Component.literal("Apply"),
                                button ->
                                        onApplyClicked(
                                                UpgradeScrollMenu.SCROLL_SLOT_4
                                        )
                        ).bounds(
                                leftPos + 130,
                                topPos + 85,
                                38,
                                16
                        ).build()
                );
    }

    private void onApplyClicked(int scrollSlot) {
        PacketDistributor.sendToServer(
                new ApplyUpgradeScrollPacket(scrollSlot)
        );
    }

    @Override
    protected void containerTick() {
        super.containerTick();

        updateApplyButton(
                affixButton,
                SCROLL_SLOT_1
        );

        updateApplyButton(
                cursedButton,
                SCROLL_SLOT_2
        );

        updateApplyButton(
                goldenButton,
                SCROLL_SLOT_3
        );

        updateApplyButton(
                holyButton,
                SCROLL_SLOT_4
        );
    }

    private void updateApplyButton(
            Button button,
            int scrollSlot
    ) {
        if (button == null) {
            return;
        }

        button.active =
                menu.canApply(scrollSlot);

        button.setMessage(
                Component.literal(
                        "Apply"
                )
        );
    }

    @Override
    public void render(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick
    ) {
        super.render(graphics, mouseX, mouseY, partialTick);

        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(
            GuiGraphics graphics,
            float partialTick,
            int mouseX,
            int mouseY
    ) {
        int x = leftPos;
        int y = topPos;

        /*
         * Main menu background.
         */
        graphics.fill(
                x,
                y,
                x + imageWidth,
                y + imageHeight,
                0xFF080808
        );

        graphics.fill(
                x + 2,
                y + 2,
                x + imageWidth - 2,
                y + imageHeight - 2,
                0xFF202020
        );

        /*
         * Player display panel.
         */
        graphics.fill(
                x + 30,
                y + 20,
                x + 70,
                y + 100,
                0xFF050505
        );

        graphics.fill(
                x + 32,
                y + 22,
                x + 68,
                y + 98,
                0xFF111111
        );

        assert Objects.requireNonNull(minecraft).player != null;
        if (minecraft.player != null) {
            InventoryScreen.renderEntityInInventoryFollowsMouse(
                    graphics,
                    x + 20,
                    y + 20,
                    x + 80,
                    y + 100,
                    30,
                    0.0F,
                    mouseX,
                    mouseY,
                    minecraft.player
            );
        }

        /*
         * Scroll slots.
         */
        drawSlotBox(
                graphics,
                x + 74,
                y + 25
        );

        drawSlotBox(
                graphics,
                x + 140,
                y + 25
        );

        drawSlotBox(
                graphics,
                x + 74,
                y + 65
        );

        drawSlotBox(
                graphics,
                x + 140,
                y + 65
        );

        drawSlotBox(
                graphics,
                x + 10,
                y + 20
        );

        drawSlotBox(
                graphics,
                x + 10,
                y + 38
        );

        drawSlotBox(
                graphics,
                x + 10,
                y + 56
        );

        drawSlotBox(
                graphics,
                x + 10,
                y + 74
        );

        drawSlotBox(
                graphics,
                x + 10,
                y + 92
        );

        /*
         * Application slot.
         */
        drawApplicationSlotBox(
                graphics,
                x + 111,
                y + 45
        );

        /*
         * Player inventory.
         */
        for (int row = 0; row < 3; row++) {

            for (int column = 0; column < 9; column++) {

                drawSlotBox(
                        graphics,
                        x + 8 + column * 18,
                        y + UpgradeScrollMenu.inventoryHeight + row * 18
                );
            }
        }

        /*
         * Player hotbar.
         */
        for (int column = 0; column < 9; column++) {

            drawSlotBox(
                    graphics,
                    x + 8 + column * 18,
                    y + UpgradeScrollMenu.hotbarHeight
            );
        }
    }

    private void drawSlotBox(
            GuiGraphics graphics,
            int x,
            int y
    ) {
        x -= 1;
        y -= 1;

        /*
         * Outer dark border.
         */
        graphics.fill(
                x - 1,
                y - 1,
                x + 19,
                y + 19,
                0xFF080808
        );

        /*
         * Slot background.
         */
        graphics.fill(
                x,
                y,
                x + 18,
                y + 18,
                0xFF3A3A3A
        );

        /*
         * Highlighted upper/left edge.
         */
        graphics.fill(
                x,
                y,
                x + 18,
                y + 1,
                0xFF707070
        );

        graphics.fill(
                x,
                y,
                x + 1,
                y + 18,
                0xFF707070
        );

        /*
         * Dark lower/right edge.
         */
        graphics.fill(
                x,
                y + 17,
                x + 18,
                y + 18,
                0xFF181818
        );

        graphics.fill(
                x + 17,
                y,
                x + 18,
                y + 18,
                0xFF181818
        );
    }

    private void drawApplicationSlotBox(
            GuiGraphics graphics,
            int x,
            int y
    ) {
        x -= 1;
        y -= 1;

        /*
         * Slightly larger/different outer border
         * to distinguish the application slot.
         */
        graphics.fill(
                x - 1,
                y - 1,
                x + 19,
                y + 19,
                0xFF080808
        );

        graphics.fill(
                x,
                y,
                x + 18,
                y + 18,
                0xFF454545
        );

        /*
         * Upper/left highlight.
         */
        graphics.fill(
                x,
                y,
                x + 18,
                y + 1,
                0xFF858585
        );

        graphics.fill(
                x,
                y,
                x + 1,
                y + 18,
                0xFF858585
        );

        /*
         * Lower/right shadow.
         */
        graphics.fill(
                x,
                y + 17,
                x + 18,
                y + 18,
                0xFF181818
        );

        graphics.fill(
                x + 17,
                y,
                x + 18,
                y + 18,
                0xFF181818
        );
    }

    @Override
    protected void renderLabels(
            GuiGraphics graphics,
            int mouseX,
            int mouseY
    ) {
        graphics.drawString(
                font,
                title,
                8,
                6,
                0x404040,
                false
        );

        String resultText = "";

        graphics.drawString(
                font,
                resultText,
                8,
                78,
                0xFFFFFF,
                false
        );
    }

    @Override
    protected void renderTooltip(
            GuiGraphics graphics,
            int mouseX,
            int mouseY
    ) {
        super.renderTooltip(graphics, mouseX, mouseY);
    }
}