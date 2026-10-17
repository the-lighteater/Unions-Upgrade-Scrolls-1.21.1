package dot.lighteater.upgrade_scrolls.menu;

import dot.lighteater.upgrade_scrolls.utility.ModUtility;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class UpgradeApplicationSlot extends Slot {

    public UpgradeApplicationSlot(
            Container container,
            int index,
            int x,
            int y
    ) {
        super(container, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return !stack.isEmpty()
                && (ModUtility.isValidWeaponOrCurio(stack) != 0
                || ModUtility.isValidArmor(stack));
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}