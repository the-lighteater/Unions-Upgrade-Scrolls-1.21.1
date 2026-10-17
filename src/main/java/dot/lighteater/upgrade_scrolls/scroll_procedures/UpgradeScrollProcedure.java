package dot.lighteater.upgrade_scrolls.scroll_procedures;

import dot.lighteater.upgrade_scrolls.affix.AffixData;
import dot.lighteater.upgrade_scrolls.affix.AffixLoader;
import dot.lighteater.upgrade_scrolls.data.ModDataComponents;
import dot.lighteater.upgrade_scrolls.utility.ModUtility;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.List;

public class UpgradeScrollProcedure {

    public static final DataComponentType<CompoundTag> AFFIX_DATA =
            DataComponentType.<CompoundTag>builder()
                    .persistent(CompoundTag.CODEC)
                    .build();

    private static final Logger LOGGER = LogManager.getLogger();

    public static boolean execute(Level level, Player player, EquipmentSlot slot, int isShield, int isCurio, ItemStack targetItem) {
        boolean armorSlot = slot == EquipmentSlot.HEAD || slot == EquipmentSlot.CHEST ||
                slot == EquipmentSlot.LEGS || slot == EquipmentSlot.FEET;

        ItemStack target =
                targetItem != null
                        ? targetItem
                        : player.getItemBySlot(slot);

        int itemType = ModUtility.isValidWeaponOrCurio(target);
        if (itemType != 0 && armorSlot) return false;
        if (target.isEmpty()) {
            player.displayClientMessage(Component.literal("No item equipped in " + slot.getName()), true);
            return false;
        } else if ((itemType == 0) && slot != EquipmentSlot.HEAD && slot != EquipmentSlot.CHEST
                && slot != EquipmentSlot.LEGS && slot != EquipmentSlot.FEET
                || (isShield == 0 && ModUtility.isValidShield(target))) { return false;}

        if (isShield == 0 && ModUtility.isValidShield(target)) return false;
        else if (isShield != 0 && !(ModUtility.isValidShield(target))) return false;

        String slotKey = "";
        if (slot == EquipmentSlot.HEAD) {
            slotKey = "_head";
        } else if (slot == EquipmentSlot.CHEST) {
            slotKey = "_chest";
        } else if (slot == EquipmentSlot.LEGS) {
            slotKey = "_legs";
        } else if (slot == EquipmentSlot.FEET) {
            slotKey = "_feet";
        } else if (itemType == 2) {
            slotKey = "_curio";
        } else if (isShield == 1) {
            slotKey = "_shield";
        }

        if (!((itemType == 2 && isCurio == 1) || (itemType != 2 && isCurio != 1))) {
            return false;
        }

        CompoundTag original = target.get(ModDataComponents.AFFIX_DATA.get());
        CompoundTag tag = (original == null) ? new CompoundTag() : original.copy();

        for (AffixData finalAffixes : AffixLoader.getAll()) {
            if (finalAffixes.isFinal() && tag.getDouble("mod:tag_affix_" + finalAffixes.getName() + slotKey) >= 10.0) {
                player.displayClientMessage(Component.literal("This item already has a final affix! (" + finalAffixes.getDisplayName() + ")" ), true);
                return false;
            }
        }

        clearAffix(tag, slotKey);
        AffixData affix = rollAffix(level.random);
        if (affix == null) return false;

        String key = "mod:tag_affix_" + affix.getName() + slotKey;
        tag.putDouble(key, 10.0);
        target.set(ModDataComponents.AFFIX_DATA.get(), tag);
        Component message = buildMessage(player, target, affix);

        for (String soundID : affix.getSounds()) {
            ResourceLocation soundRL = ResourceLocation.tryParse(soundID);
            if (soundRL == null) continue;
            var soundEvent = BuiltInRegistries.SOUND_EVENT.get(soundRL);
            if (soundEvent == null) continue;

            level.playSound(null, player.getX(), player.getY(), player.getZ(), soundEvent, SoundSource.PLAYERS, 1f, 1f);
        }

        for (Player p : level.players()) {
            p.displayClientMessage(message, false);
        }

        return true;
    }

    public static AffixData rollAffix(RandomSource random) {

        Collection<AffixData> affixes = AffixLoader.getAll();

        List<AffixData> validAffixes = affixes.stream()
                .filter(a -> !a.isDisabled())
                .toList();

        if (validAffixes.isEmpty()) return null;

        double totalWeight = validAffixes.stream()
                .mapToDouble(AffixData::getWeight)
                .sum();

        double roll = random.nextDouble() * totalWeight;

        double cumulative = 0;

        for (AffixData affix : validAffixes) {
            cumulative += affix.getWeight();

            if (roll <= cumulative) {
                return affix;
            }
        }

        return null;
    }

    public static void clearAffix(CompoundTag tag, String slotKey) {
        Collection<AffixData> affixes = AffixLoader.getAll();

        for (AffixData affix : affixes) {
            String removeKey = "mod:tag_affix_" + affix.getName() + slotKey;
            tag.remove(removeKey);
        }
    }

    public static Component buildMessage(Player player, ItemStack stack, AffixData affix) {
        MutableComponent base = Component.literal(player.getName().getString())
                .append(Component.literal(" rolled the "))
                .append(Component.literal(affix.getName().toUpperCase())
                        .setStyle(Style.EMPTY.withObfuscated(affix.getName().equals("fabled")))
                        .withStyle(style -> style.withColor(affix.getColorNumber())))
                .append(Component.literal(" affix on their "))
                .append(stack.getHoverName().copy());

        return switch (affix.getName()) {
            case "junk" -> base.append(Component.literal("..."));
            case "ordinary", "unique" -> base.append(Component.literal("."));
            case "fabled" -> base.append(Component.literal("!!!"));
            default -> base.append(Component.literal("!"));
        };
    }
}
