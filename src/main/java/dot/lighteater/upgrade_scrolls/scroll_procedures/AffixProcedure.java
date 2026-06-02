package dot.lighteater.upgrade_scrolls.scroll_procedures;

import dot.lighteater.upgrade_scrolls.data.ModDataComponents;
import dot.lighteater.upgrade_scrolls.utility.ItemUtility;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import org.checkerframework.checker.units.qual.A;
import top.theillusivec4.curios.api.event.CurioAttributeModifierEvent;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static dot.lighteater.upgrade_scrolls.UnionsUpgradeScrolls.LOGGER;

@EventBusSubscriber
public class AffixProcedure {

    private static final String AFFIX_KEY = "mod:tag_affix";

    @SubscribeEvent
    public static void addAttributeModifier(ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();

        // Weapon affixes
        if (ItemUtility.isValidWeapon(stack)) {
            applyAttributes(stack, "", "weapon", EquipmentSlot.MAINHAND, event);
        }

        // Bow affixes
        if (ItemUtility.isValidBow(stack)) {
            applyAttributes(stack, "", "bow", EquipmentSlot.MAINHAND, event);
        }

        // Shield affixes
        if (ItemUtility.isValidShield(stack)) {
            applyAttributes(stack, "_shield", "shield", EquipmentSlot.OFFHAND, event);
        }

        // Armor affixes
        if (stack.getItem() instanceof ArmorItem armor) {
            applyAttributes(stack,
                    "_" + armor.getEquipmentSlot().getName(),
                    "armor",
                    armor.getEquipmentSlot(),
                    event);
        }
    }

    @SubscribeEvent
    public static void addCurioModifier(CurioAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();

        CompoundTag tag = stack.get(ModDataComponents.AFFIX_DATA.get());
        if (tag == null) return;

        if (!stack.isEmpty()) {
            if (tag.getDouble("upgradescrolls:streak_curio") != 0.0) {
                ResourceLocation modifierId = ResourceLocation.fromNamespaceAndPath(
                        "upgrade_scrolls",
                        "curio_armor_bonus"
                );

                event.addModifier(Attributes.ARMOR, new AttributeModifier(
                        modifierId,
                        (tag.getDouble("upgradescrolls:streak_curio") / 2),
                        AttributeModifier.Operation.ADD_VALUE));
            }
            applyCurioAttributes(stack, tag, event);
        }
    }

    public static void applyCurioAttributes(ItemStack stack, CompoundTag tag, CurioAttributeModifierEvent event) {
        AffixData affix = null;
        for (AffixData affixes : AffixLoader.getAll()) {
            if (tag.getDouble(AFFIX_KEY + "_" + affixes.getName() + "_curio") > 0 && !affixes.isDisabled()) {
                affix = affixes;
                break;
            }
        }

        if (affix == null) return;
        AffixData newData = AffixLoader.AFFIXES.get(ResourceLocation.fromNamespaceAndPath("upgrade_scrolls", affix.getName()));
        if (newData == null) return;

        List<AttributeEffect> stats = newData.getEffectsFor("curio");
        if (stats == null) return;

        for (AttributeEffect attr : stats) {
            Holder.Reference<net.minecraft.world.entity.ai.attributes.Attribute> attribute =
                    attr.getAttribute();

            if (attribute == null) continue;

            ResourceLocation modifierId = ResourceLocation.fromNamespaceAndPath(
                    "upgrade_scrolls",
                    affix.getName() + "_" + getItemUUID(stack, attribute.toString()) + "_" + attr.getEndString() + "_bonus"
            );

            event.addModifier(attribute, new AttributeModifier(
                    modifierId,
                    attr.getValue(),
                    attr.getOperation()
            ));
        }
    }


    public static void applyAttributes(ItemStack stack,
                                       String slotSuffix,
                                       String type,
                                       EquipmentSlot slot,
                                       ItemAttributeModifierEvent event) {

        CompoundTag tag = stack.get(ModDataComponents.AFFIX_DATA.get());
        if (tag == null) {
            tag = new CompoundTag();
            stack.set(ModDataComponents.AFFIX_DATA.get(), tag);
        }

        AffixData affix = null;

        for (AffixData affixes : AffixLoader.getAll()) {
            if (tag.getDouble(AFFIX_KEY + "_" + affixes.getName() + slotSuffix) > 0
                    && !affixes.isDisabled()) {
                affix = affixes;
                break;
            }
        }

        if (affix == null) return;

        AffixData data = AffixLoader.AFFIXES.get(
                ResourceLocation.fromNamespaceAndPath("upgrade_scrolls", affix.getName())
        );

        if (data == null) return;

        List<AttributeEffect> stats = data.getEffectsFor(type);
        if (stats == null) return;

        for (AttributeEffect attr : stats) {
            Holder.Reference<net.minecraft.world.entity.ai.attributes.Attribute> attribute =
                    attr.getAttribute();

            if (attribute == null) continue;

            ResourceLocation modifierId = ResourceLocation.fromNamespaceAndPath(
                    "upgrade_scrolls",
                    affix.getName()
                            + "_" + getItemUUID(stack, attribute.toString()) + "_" + attr.getEndString() + "_bonus"
            );

            event.addModifier(
                    attribute,
                    new AttributeModifier(
                            modifierId,
                            attr.getValue(),
                            attr.getOperation()
                    ),
                    EquipmentSlotGroup.bySlot(slot)  // ✅ SAFE FIX
            );
        }
    }

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