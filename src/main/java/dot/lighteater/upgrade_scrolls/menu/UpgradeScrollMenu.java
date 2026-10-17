package dot.lighteater.upgrade_scrolls.menu;

import dot.lighteater.upgrade_scrolls.UpgradeScrolls;
import dot.lighteater.upgrade_scrolls.item.UpgradeScroll_Item;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

import java.util.Set;

public class UpgradeScrollMenu extends AbstractContainerMenu {

    public static final int SCROLL_SLOT_1 = 0;
    public static final int APPLICATION_SLOT = 1;
    public static final int SCROLL_SLOT_2 = 2;
    public static final int SCROLL_SLOT_3 = 3;
    public static final int SCROLL_SLOT_4 = 4;

    public static final int ARMOR_HEAD_SLOT = 5;
    public static final int ARMOR_CHEST_SLOT = 6;
    public static final int ARMOR_LEGS_SLOT = 7;
    public static final int ARMOR_FEET_SLOT = 8;
    public static final int OFFHAND_SLOT = 9;

    private static final int PLAYER_INVENTORY_START = 10;

    private static final int RESULT_INDEX = 0;

    private final Container scrollContainer;

    private final ContainerData data;

    public final static int inventoryHeight = 120;

    public final static int hotbarHeight = 190;

    /*
     * Server constructor.
     */
    public UpgradeScrollMenu(
            int containerId,
            Inventory playerInventory
    ) {
        this(
                containerId,
                playerInventory,
                new SimpleContainer(5),
                new SimpleContainerData(1)
        );
    }

    /*
     * Client constructor.
     */
    public UpgradeScrollMenu(
            int containerId,
            Inventory playerInventory,
            FriendlyByteBuf extraData
    ) {
        this(
                containerId,
                playerInventory,
                new SimpleContainer(5),
                new SimpleContainerData(1)
        );
    }

    private UpgradeScrollMenu(
            int containerId,
            Inventory playerInventory,
            Container container,
            ContainerData data
    ) {
        super(UpgradeScrollsMenus.UPGRADE_SCROLL_MENU.get(), containerId);

        this.scrollContainer = container;
        this.data = data;

        checkContainerSize(
                container,
                5
        );

        /*
         * Scroll slot 1
         */
        addSlot(
                new ScrollSlot(
                        container,
                        SCROLL_SLOT_1,
                        74,
                        25,
                        ScrollSlotType.AFFIX
                )
        );

        /*
         * Application item
         */
        addSlot(
                new UpgradeApplicationSlot(
                        container,
                        APPLICATION_SLOT,
                        111,
                        45
                )
        );

        /*
         * Scroll slot 2
         */
        addSlot(
                new ScrollSlot(
                        container,
                        SCROLL_SLOT_2,
                        140,
                        25,
                        ScrollSlotType.CURSED
                )
        );

        /*
         * Scroll slot 3
         */
        addSlot(
                new ScrollSlot(
                        container,
                        SCROLL_SLOT_3,
                        74,
                        65,
                        ScrollSlotType.GOLDEN
                )
        );

        addSlot(
                new ScrollSlot(
                        container,
                        SCROLL_SLOT_4,
                        140,
                        65,
                        ScrollSlotType.HOLY
                )
        );

        /*
         * Player armor.
         */
        addSlot(
                new ArmorSlot(
                        playerInventory,
                        39,
                        10,
                        20,
                        EquipmentType.HELMET
                )
        );

        addSlot(
                new ArmorSlot(
                        playerInventory,
                        38,
                        10,
                        38,
                        EquipmentType.CHESTPLATE
                )
        );

        addSlot(
                new ArmorSlot(
                        playerInventory,
                        37,
                        10,
                        56,
                        EquipmentType.LEGGINGS
                )
        );

        addSlot(
                new ArmorSlot(
                        playerInventory,
                        36,
                        10,
                        74,
                        EquipmentType.BOOTS
                )
        );

        /*
         * Player offhand.
         */
        addSlot(
                new Slot(
                        playerInventory,
                        40,
                        10,
                        92
                )
        );

        /*
         * Player inventory.
         */
        addPlayerInventory(
                playerInventory
        );

        /*
         * Result state:
         *
         * 0 = nothing
         * 1 = success
         * 2 = failed
         */
        addDataSlots(
                data
        );
    }

    private void addPlayerInventory(
            Inventory inventory
    ) {
        /*
         * Main inventory.
         */
        for (int row = 0; row < 3; row++) {

            for (int column = 0; column < 9; column++) {

                addSlot(
                        new net.minecraft.world.inventory.Slot(
                                inventory,
                                column + row * 9 + 9,
                                8 + column * 18,
                                inventoryHeight + row * 18
                        )
                );
            }
        }

        /*
         * Hotbar.
         */
        for (int column = 0; column < 9; column++) {

            addSlot(
                    new net.minecraft.world.inventory.Slot(
                            inventory,
                            column,
                            8 + column * 18,
                            hotbarHeight
                    )
            );
        }
    }

    public Container getScrollContainer() {
        return scrollContainer;
    }

    public ItemStack getApplicationItem() {
        return slots
                .get(APPLICATION_SLOT)
                .getItem();
    }

    public ItemStack getScroll(int slotIndex) {

        if (slotIndex != SCROLL_SLOT_1
                && slotIndex != SCROLL_SLOT_2
                && slotIndex != SCROLL_SLOT_3
                && slotIndex != SCROLL_SLOT_4) {

            return ItemStack.EMPTY;
        }

        return slots
                .get(slotIndex)
                .getItem();
    }

    public int getResult() {
        return data.get(RESULT_INDEX);
    }

    public void setResult(int result) {

        /*
         * This should only really be called server-side.
         */
        data.set(
                RESULT_INDEX,
                result
        );
    }

    public boolean canApply(int scrollSlot) {

        if (scrollSlot != SCROLL_SLOT_1
                && scrollSlot != SCROLL_SLOT_2
                && scrollSlot != SCROLL_SLOT_3
                && scrollSlot != SCROLL_SLOT_4) {

            return false;
        }

        ItemStack scroll =
                getScroll(scrollSlot);

        ItemStack target =
                getApplicationItem();

        if (scroll.isEmpty()
                || (target.isEmpty() && scrollSlot != SCROLL_SLOT_4)) {

            return false;
        }

        return getSlot(scrollSlot).mayPlace(scroll)
                && getSlot(APPLICATION_SLOT).mayPlace(target);
    }

    public UpgradeScroll_Item getScrollItem(
            int slotIndex
    ) {

        ItemStack stack =
                getScroll(slotIndex);

        if (stack.getItem()
                instanceof UpgradeScroll_Item scroll) {

            return scroll;
        }

        return null;
    }

    @Override
    public boolean stillValid(
            Player player
    ) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(
            Player player,
            int index
    ) {

        Slot slot =
                slots.get(index);

        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack sourceStack =
                slot.getItem();

        ItemStack originalStack =
                sourceStack.copy();

        /*
         * =========================
         * CUSTOM SCROLL/APPLICATION
         * =========================
         *
         * 0 - 4
         */
        if (index < PLAYER_INVENTORY_START) {

            /*
             * Scroll/application/equipment
             * -> player inventory
             */
            if (!moveItemStackTo(
                    sourceStack,
                    PLAYER_INVENTORY_START,
                    slots.size(),
                    true
            )) {
                return ItemStack.EMPTY;
            }

        } else {

            /*
             * =========================
             * PLAYER INVENTORY
             * =========================
             *
             * First try scroll slots.
             */

            boolean moved =
                    moveItemStackTo(
                            sourceStack,
                            SCROLL_SLOT_1,
                            SCROLL_SLOT_1 + 1,
                            false
                    );

            if (!moved) {
                moved =
                        moveItemStackTo(
                                sourceStack,
                                SCROLL_SLOT_2,
                                SCROLL_SLOT_2 + 1,
                                false
                        );
            }

            if (!moved) {
                moved =
                        moveItemStackTo(
                                sourceStack,
                                SCROLL_SLOT_3,
                                SCROLL_SLOT_3 + 1,
                                false
                        );
            }

            if (!moved) {
                moved =
                        moveItemStackTo(
                                sourceStack,
                                SCROLL_SLOT_4,
                                SCROLL_SLOT_4 + 1,
                                false
                        );
            }

            /*
             * Then try application slot.
             */
            if (!moved) {
                moved =
                        moveItemStackTo(
                                sourceStack,
                                APPLICATION_SLOT,
                                APPLICATION_SLOT + 1,
                                false
                        );
            }

            /*
             * Finally, try equipment slots.
             *
             * ArmorSlot.mayPlace()
             * determines which armor slot accepts it.
             */
            if (!moved) {
                moved =
                        moveItemStackTo(
                                sourceStack,
                                ARMOR_HEAD_SLOT,
                                OFFHAND_SLOT + 1,
                                false
                        );
            }

            if (!moved) {
                return ItemStack.EMPTY;
            }
        }

        /*
         * If the entire stack was moved,
         * clear the original slot.
         */
        if (sourceStack.isEmpty()) {

            slot.set(
                    ItemStack.EMPTY
            );

        } else {

            slot.setChanged();
        }

        /*
         * Nothing actually moved.
         */
        if (sourceStack.getCount()
                == originalStack.getCount()) {

            return ItemStack.EMPTY;
        }

        slot.onTake(
                player,
                sourceStack
        );

        return originalStack;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);

        if (!player.level().isClientSide()) {
            clearContainer(
                    player,
                    scrollContainer
            );
        }
    }
}