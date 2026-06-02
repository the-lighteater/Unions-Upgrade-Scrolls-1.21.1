package dot.lighteater.upgrade_scrolls.scroll_procedures;

import dot.lighteater.upgrade_scrolls.data.ModDataComponents;
import dot.lighteater.upgrade_scrolls.utility.ItemUtility;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.level.Level;

public class GoldenScrollProcedure {

    public static final DataComponentType<CompoundTag> AFFIX_DATA =
            DataComponentType.<CompoundTag>builder()
                    .persistent(CompoundTag.CODEC)
                    .build();

    public static boolean execute(Level level, Player player, EquipmentSlot slot, int isShield, int isCurio, boolean serverSide, ItemStack targetItem) {
        if (level.isClientSide && serverSide) return false;
        ItemStack target = null;

        boolean armorSlot = slot == EquipmentSlot.HEAD || slot == EquipmentSlot.CHEST ||
                slot == EquipmentSlot.LEGS || slot == EquipmentSlot.FEET;
        if (!(serverSide) && armorSlot) return false;

        if (serverSide) target = player.getItemBySlot(slot);
        else target = targetItem;

        int itemType = ItemUtility.isValidWeaponOrCurio(target);
        if (itemType != 0 && armorSlot) return false;

        if (target.isEmpty()) {
            player.displayClientMessage(Component.literal("No item equipped in " + slot.getName()), true);
            return false;
        } else if ((itemType == 0) && slot != EquipmentSlot.HEAD && slot != EquipmentSlot.CHEST
                && slot != EquipmentSlot.LEGS && slot != EquipmentSlot.FEET) { return false;
        }

        if (!((itemType == 2 && isCurio == 1) || (itemType != 2 && isCurio != 1))) {
            return false;
        }

        if (isShield == 0 && ItemUtility.isValidShield(target)) return false;
        else if (isShield != 0 && !(ItemUtility.isValidShield(target))) return false;

        CompoundTag original = target.get(ModDataComponents.AFFIX_DATA.get());
        CompoundTag tag = (original == null) ? new CompoundTag() : original.copy();

        String fabledKey = "upgradescrolls:golden";
        if (tag.getDouble(fabledKey) >= 10.0) {
            if (level.isClientSide) {
                level.playLocalSound(player.getX(), player.getY(), player.getZ(), (SoundEvent) SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 1f, 1f, false);
                level.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.VILLAGER_WORK_LEATHERWORKER, SoundSource.PLAYERS, 1f, 1f, false);
                level.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 1f, 1f, false);
            } else {
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 1f, 1f);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.VILLAGER_WORK_LEATHERWORKER, SoundSource.PLAYERS, 1f, 1f);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 1f, 1f);
            }

            player.displayClientMessage(Component.literal("This item is already unbreakable!")
                    .withStyle(ChatFormatting.RED), false);
            return false;
        }

        Component message;

            if (level.isClientSide) {
                level.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1f, 1f, false);
                level.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.VILLAGER_WORK_WEAPONSMITH, SoundSource.PLAYERS, 1f, 1f, false);
                level.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.ILLUSIONER_CAST_SPELL, SoundSource.PLAYERS, 1f, 1f, false);
                level.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.ILLUSIONER_MIRROR_MOVE, SoundSource.PLAYERS, 1f, 1f, false);
                level.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 1f, 1f, false);
                level.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_DRAGON_GROWL, SoundSource.PLAYERS, 1f, 1f, false);
                level.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.ZOMBIE_CONVERTED_TO_DROWNED, SoundSource.PLAYERS, 1f, 1f, false);
            } else {
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1f, 1f);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.VILLAGER_WORK_WEAPONSMITH, SoundSource.PLAYERS, 1f, 1f);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ILLUSIONER_CAST_SPELL, SoundSource.PLAYERS, 1f, 1f);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ILLUSIONER_MIRROR_MOVE, SoundSource.PLAYERS, 1f, 1f);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 1f, 1f);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_DRAGON_GROWL, SoundSource.PLAYERS, 1f, 1f);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ZOMBIE_CONVERTED_TO_DROWNED, SoundSource.PLAYERS, 1f, 1f);
            }

            tag.putDouble(fabledKey, 10.0);
            target.set(DataComponents.UNBREAKABLE, new Unbreakable(true));
            target.set(ModDataComponents.AFFIX_DATA.get(), tag);
            message = Component.literal(player.getName().getString())
                    .append(Component.literal(" made their ").withStyle(ChatFormatting.YELLOW))
                    .append(target.getHoverName().copy())
                    .append(Component.literal(" unbreakable!").withStyle(ChatFormatting.YELLOW));

        for (Player p : level.players()) {
            p.displayClientMessage(message, false);
        }
        return true;
    }
}