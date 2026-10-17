package dot.lighteater.upgrade_scrolls.menu;

import dot.lighteater.upgrade_scrolls.utility.ModUtility;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ArmorSlot extends Slot {

    private final EquipmentType type;

    public ArmorSlot(
            Container container,
            int index,
            int x,
            int y,
            EquipmentType type
    ) {
        super(container, index, x, y);

        this.type = type;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {

        if (stack.isEmpty()) {
            return false;
        }

        return switch (type) {
            case HELMET -> ModUtility.isValidHelmet(stack);
            case CHESTPLATE -> ModUtility.isValidChestplate(stack);
            case LEGGINGS -> ModUtility.isValidLeggings(stack);
            case BOOTS -> ModUtility.isValidBoots(stack);
        };
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    public EquipmentType getType() {
        return type;
    }
}