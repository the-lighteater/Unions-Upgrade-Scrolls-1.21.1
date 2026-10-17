package dot.lighteater.upgrade_scrolls.menu;

import dot.lighteater.upgrade_scrolls.item.UpgradeScroll_Item;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ScrollSlot extends Slot {

    private final ScrollSlotType type;

    public ScrollSlot(
            Container container,
            int index,
            int x,
            int y,
            ScrollSlotType type
    ) {
        super(container, index, x, y);

        this.type = type;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {

        if (!(stack.getItem() instanceof UpgradeScroll_Item scroll)) {
            return false;
        }

        return type.accepts(
                scroll.getScrollId()
        );
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    public ScrollSlotType getType() {
        return type;
    }
}