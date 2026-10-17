package dot.lighteater.upgrade_scrolls.compat;

import dot.lighteater.upgrade_scrolls.data.ModDataComponents;
import dot.lighteater.upgrade_scrolls.affix.AffixData;
import dot.lighteater.upgrade_scrolls.affix.AffixLoader;
import dot.lighteater.upgrade_scrolls.affix.AttributeEffect;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import top.theillusivec4.curios.api.event.CurioAttributeModifierEvent;

import java.util.List;
import java.util.UUID;

public class CurioAffixProcedure {

    private static final String AFFIX_KEY = "mod:tag_affix";

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
