package dot.lighteater.upgrade_scrolls.scroll_procedures;

import dot.lighteater.upgrade_scrolls.UpgradeScrolls;
import dot.lighteater.upgrade_scrolls.data.ModDataComponents;
import dot.lighteater.upgrade_scrolls.utility.CursedConfigLoader;
import dot.lighteater.upgrade_scrolls.utility.ModUtility;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@EventBusSubscriber(modid = UpgradeScrolls.MODID)
public class CursedAttributeProcedure {

    @SubscribeEvent
    public static void addCursedAttributeModifier(
            ItemAttributeModifierEvent event
    ) {
        ItemStack stack = event.getItemStack();

        // Read existing data only. Do not create or modify components here.
        CompoundTag tag = stack.get(ModDataComponents.AFFIX_DATA.get());

        if (tag == null) {
            return;
        }

        EquipmentSlot slot = stack.getItem() instanceof net.minecraft.world.item.ArmorItem armor
                ? armor.getEquipmentSlot()
                : EquipmentSlot.MAINHAND;

        EquipmentSlotGroup slotGroup = switch (slot) {
            case MAINHAND -> EquipmentSlotGroup.MAINHAND;
            case OFFHAND -> EquipmentSlotGroup.OFFHAND;
            case HEAD -> EquipmentSlotGroup.HEAD;
            case CHEST -> EquipmentSlotGroup.CHEST;
            case LEGS -> EquipmentSlotGroup.LEGS;
            case FEET -> EquipmentSlotGroup.FEET;
            case BODY -> EquipmentSlotGroup.BODY;
        };

        var modifiers = CursedConfigLoader.getModifiers();

        for (CursedConfigLoader.ModifierData modifier : modifiers) {
            String streakKey = modifier.getStreakKey();

            if (!isApplicable(modifier, stack, slot)) {
                continue;
            }

            String attributeName = modifier.getAttributeName();

            if (attributeName == null || attributeName.isBlank()) {
                continue;
            }

            ResourceLocation attributeId = ResourceLocation.tryParse(attributeName);
            if (attributeId == null) {
                continue;
            }

            Holder<Attribute> attributeHolder =
                    BuiltInRegistries.ATTRIBUTE.getHolder(attributeId).orElse(null);

            if (attributeHolder == null) {
                continue;
            }

            String fullStreakKey = "upgradescrolls:streak_" + streakKey;
            double streak = tag.getDouble(fullStreakKey);

            if (streak <= 0) {
                continue;
            }

            double amount = streak * modifier.getLevelBonus();

            AttributeModifier.Operation operation =
                    parseOperation(modifier.getOperation());

            if (operation == null) {
                continue;
            }

            ResourceLocation modifierId = getModifierId(
                    streakKey,
                    attributeId,
                    modifier.getOperation()
            );

            AttributeModifier attributeModifier = new AttributeModifier(
                    modifierId,
                    amount,
                    operation
            );

            event.addModifier(attributeHolder, attributeModifier, slotGroup);
        }
    }

    private static boolean isApplicable(
            CursedConfigLoader.ModifierData modifier,
            ItemStack stack,
            EquipmentSlot slot
    ) {
        String equipment = modifier.getEquipment();

        if (equipment == null) {
            return false;
        }

        return switch (equipment.toUpperCase()) {
            case "WEAPON" -> isWeaponApplication(stack, slot);
            case "SHIELD" -> isShieldApplication(stack, slot);
            case "HEAD" -> isHeadApplication(stack, slot);
            case "CHEST" -> isChestApplication(stack, slot);
            case "LEGS" -> isLegsApplication(stack, slot);
            case "FEET" -> isFeetApplication(stack, slot);
            default -> false;
        };
    }

    private static boolean isWeaponApplication(
            ItemStack stack,
            EquipmentSlot slot
    ) {
        return (slot == EquipmentSlot.MAINHAND
                || slot == EquipmentSlot.OFFHAND)
                && (
                ModUtility.isValidWeapon(stack)
                        || ModUtility.isValidBow(stack)
        );
    }

    private static boolean isShieldApplication(
            ItemStack stack,
            EquipmentSlot slot
    ) {
        return (slot == EquipmentSlot.MAINHAND
                || slot == EquipmentSlot.OFFHAND)
                && ModUtility.isValidShield(stack);
    }

    private static boolean isHeadApplication(
            ItemStack stack,
            EquipmentSlot slot
    ) {
        return slot == EquipmentSlot.HEAD
                && ModUtility.isValidHelmet(stack);
    }

    private static boolean isChestApplication(
            ItemStack stack,
            EquipmentSlot slot
    ) {
        return slot == EquipmentSlot.CHEST
                && ModUtility.isValidChestplate(stack);
    }

    private static boolean isLegsApplication(
            ItemStack stack,
            EquipmentSlot slot
    ) {
        return slot == EquipmentSlot.LEGS
                && ModUtility.isValidLeggings(stack);
    }

    private static boolean isFeetApplication(
            ItemStack stack,
            EquipmentSlot slot
    ) {
        return slot == EquipmentSlot.FEET
                && ModUtility.isValidBoots(stack);
    }

    private static AttributeModifier.Operation parseOperation(
            String operation
    ) {
        if (operation == null) {
            return null;
        }

        return switch (operation.toUpperCase()) {
            case "ADDITION", "ADD_VALUE" ->
                    AttributeModifier.Operation.ADD_VALUE;

            case "MULTIPLY_BASE", "ADD_MULTIPLIED_BASE" ->
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE;

            case "MULTIPLY_TOTAL", "ADD_MULTIPLIED_TOTAL" ->
                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL;

            default -> null;
        };
    }

    private static ResourceLocation getModifierId(
            String streakKey,
            ResourceLocation attributeId,
            String operation
    ) {
        String path = "cursed/"
                + sanitize(streakKey)
                + "/"
                + sanitize(attributeId.toString())
                + "/"
                + sanitize(operation);

        return ResourceLocation.fromNamespaceAndPath(
                UpgradeScrolls.MODID,
                path
        );
    }

    private static String sanitize(String value) {
        if (value == null || value.isBlank()) {
            return "unknown";
        }

        return value.toLowerCase()
                .replace(':', '/')
                .replaceAll("[^a-z0-9/._-]", "_");
    }

    public static UUID getItemUUID(
            ItemStack stack,
            String attributeKey
    ) {
        CompoundTag tag =
                stack.get(ModDataComponents.AFFIX_DATA.get());

        if (tag == null) {
            return UUID.nameUUIDFromBytes(
                    (stack.getItem().toString() + attributeKey)
                            .getBytes(StandardCharsets.UTF_8)
            );
        }

        String base =
                tag.getString("upgrade_scrolls:item_uuid");

        if (base.isEmpty()) {
            base = stack.getItem().toString();
        }

        return UUID.nameUUIDFromBytes(
                (base + attributeKey).getBytes(StandardCharsets.UTF_8)
        );
    }
}