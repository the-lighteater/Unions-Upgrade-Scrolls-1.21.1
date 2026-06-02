package dot.lighteater.upgrade_scrolls.scroll_procedures;

import dot.lighteater.upgrade_scrolls.data.ModDataComponents;
import dot.lighteater.upgrade_scrolls.utility.ItemUtility;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;

import java.util.UUID;

import static dot.lighteater.upgrade_scrolls.UnionsUpgradeScrolls.LOGGER;

@EventBusSubscriber
public class CursedAttributeProcedure {

    @SubscribeEvent
    public static void addCursedAttributeModifier(ItemAttributeModifierEvent event) {

        ItemStack stack = event.getItemStack();

        // READ ONLY — do NOT create or set tag here
        CompoundTag tag = stack.get(ModDataComponents.AFFIX_DATA.get());
        if (tag == null) return;

        // -------------------------
        // WEAPONS / BOWS (MAINHAND)
        // -------------------------
        if ((ItemUtility.isValidWeapon(stack) || ItemUtility.isValidBow(stack))
                && tag.getDouble("upgradescrolls:streak_barbarian") > 0) {

            ResourceLocation modifierId = ResourceLocation.fromNamespaceAndPath(
                    "upgrade_scrolls",
                    "cursed_barbarian_bonus"
            );

            event.addModifier(
                    Attributes.ATTACK_DAMAGE,
                    new AttributeModifier(
                            modifierId,
                            tag.getDouble("upgradescrolls:level_bonus_barbarian"),
                            AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    ),
                    net.minecraft.world.entity.EquipmentSlotGroup.MAINHAND
            );
        }

        // -------------------------
        // SHIELDS (OFFHAND)
        // -------------------------
        if (ItemUtility.isValidShield(stack)
                && tag.getDouble("upgradescrolls:streak_shield") > 0) {

            ResourceLocation modifierId = ResourceLocation.fromNamespaceAndPath(
                    "upgrade_scrolls",
                    "cursed_armor_shield_bonus"
            );

            event.addModifier(
                    Attributes.ARMOR,
                    new AttributeModifier(
                            modifierId,
                            tag.getDouble("upgradescrolls:level_bonus_shield") * 20,
                            AttributeModifier.Operation.ADD_VALUE
                    ),
                    net.minecraft.world.entity.EquipmentSlotGroup.OFFHAND
            );
        }

        // -------------------------
        // ARMOR (STRICT SLOT MATCH)
        // -------------------------
        if (stack.getItem() instanceof ArmorItem armor) {

            ResourceLocation modifierId = ResourceLocation.fromNamespaceAndPath(
                    "upgrade_scrolls",
                    "cursed_armor_bonus"
            );

            EquipmentSlot armorSlot = armor.getEquipmentSlot();

            String keySuffix = "_" + armorSlot.getName();

            String streakKey = "upgradescrolls:streak" + keySuffix;
            String levelKey = "upgradescrolls:level_bonus" + keySuffix;

            if (tag.getDouble(streakKey) <= 0) return;

            event.addModifier(
                    Attributes.ARMOR,
                    new AttributeModifier(
                            modifierId,
                            tag.getDouble(levelKey) * 20,
                            AttributeModifier.Operation.ADD_VALUE
                    ),
                    EquipmentSlotGroup.bySlot(armorSlot)
            );
        }
    }

    // -------------------------
    // STABLE UUID GENERATION
    // -------------------------
    public static UUID getItemUUID(ItemStack stack, String attributeKey) {

        CompoundTag tag = stack.get(ModDataComponents.AFFIX_DATA.get());
        if (tag == null) return UUID.randomUUID(); // fallback (should rarely happen)

        String base = tag.getString("upgrade_scrolls:item_uuid");

        if (base.isEmpty()) {
            base = UUID.randomUUID().toString();
            tag.putString("upgrade_scrolls:item_uuid", base);
        }

        return UUID.nameUUIDFromBytes((base + attributeKey).getBytes());
    }
}