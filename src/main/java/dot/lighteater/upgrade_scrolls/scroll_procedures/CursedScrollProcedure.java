package dot.lighteater.upgrade_scrolls.scroll_procedures;

import dot.lighteater.upgrade_scrolls.Config;
import dot.lighteater.upgrade_scrolls.data.ModDataComponents;
import dot.lighteater.upgrade_scrolls.utility.ItemUtility;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CursedScrollProcedure {

    private static final Logger LOGGER = LogManager.getLogger();

    public static int execute(Level level, Player player, EquipmentSlot slot, int isShield, int isMagician, int isCurio, boolean serverSide, ItemStack targetItem) {
        if (level.isClientSide && serverSide) return 0;
        ItemStack target;

        boolean armorSlot = slot == EquipmentSlot.HEAD || slot == EquipmentSlot.CHEST ||
                slot == EquipmentSlot.LEGS || slot == EquipmentSlot.FEET;
        if (!(serverSide) && armorSlot) return 0;

        if (serverSide) target = player.getItemBySlot(slot);
        else target = targetItem;

        int itemType = ItemUtility.isValidWeaponOrCurio(target);
        if (itemType != 0 && armorSlot) return 0;

        if (target.isEmpty()) {
            player.displayClientMessage(Component.literal("No item equipped in " + slot.getName()), true);
            return 0;
        } else if ((itemType == 0) && slot != EquipmentSlot.HEAD && slot != EquipmentSlot.CHEST
                && slot != EquipmentSlot.LEGS && slot != EquipmentSlot.FEET
                || (isShield == 0 && ItemUtility.isValidShield(target))) {
            return 0;
        }

        if (isShield == 0 && ItemUtility.isValidShield(target)) return 0;
        else if (isShield != 0 && !(ItemUtility.isValidShield(target))) return 0;

        String slotKey = "_barbarian";
        String type = "Barbarian";
        String oppSlotKey = "_magician";
        if (slot == EquipmentSlot.HEAD) {
            slotKey = "_head";
            type = "Guardian";
        } else if (slot == EquipmentSlot.CHEST) {
            slotKey = "_chest";
            type = "Guardian";
        } else if (slot == EquipmentSlot.LEGS) {
            slotKey = "_legs";
            type = "Guardian";
        } else if (slot == EquipmentSlot.FEET) {
            slotKey = "_feet";
            type = "Guardian";
        } else if (itemType == 2) {
            slotKey = "_curio";
            type = "Guardian";
        } else if (isShield == 1) {
            slotKey = "_shield";
            type = "Guardian";
        } else if (isMagician == 1) {
            slotKey = "_magician";
            oppSlotKey = "_barbarian";
            type = "Magician";
        }

        if (!((itemType == 2 && isCurio == 1) || (itemType != 2 && isCurio != 1))) {
            return 0;
        }

        CompoundTag tag = target.get(ModDataComponents.AFFIX_DATA.get());
        if (tag == null) tag = new CompoundTag();

        String streakKey = "upgradescrolls:streak" + slotKey;
        String oppStreakKey = "upgradescrolls:streak" + oppSlotKey;
        String tagKey = "upgradescrolls:level_bonus" + slotKey;

        String unbreakableKey = "upgradescrolls:golden";

        int currentStreak = tag.getInt(streakKey);
        boolean unbreakable = tag.getBoolean(unbreakableKey);
        String holyKey = "upgradescrolls:holy";
        boolean holy = player.getPersistentData().getBoolean(holyKey);

        if (currentStreak >= Config.CURSED_SCROLLS_MAX.get()) {
            player.displayClientMessage(Component.literal("This item has reached its maximum streak!"), false);
            return 0;
        } else if (tag.getInt(oppStreakKey) != 0) {
            return 0;
        }

        double roll = level.random.nextDouble();
        double successChance = Config.CURSED_SCROLLS_CHANCE.get();
        boolean success = roll <= successChance;

        if (success) {
            if (level.isClientSide) {
                level.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1f, 1f, false);
                level.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.VILLAGER_WORK_WEAPONSMITH, SoundSource.PLAYERS, 1f, 1f, false);
                level.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.ILLUSIONER_CAST_SPELL, SoundSource.PLAYERS, 1f, 1f, false);
                level.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.ILLUSIONER_MIRROR_MOVE, SoundSource.PLAYERS, 1f, 1f, false);
            } else {
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1f, 1f);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.VILLAGER_WORK_WEAPONSMITH, SoundSource.PLAYERS, 1f, 1f);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ILLUSIONER_CAST_SPELL, SoundSource.PLAYERS, 1f, 1f);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ILLUSIONER_MIRROR_MOVE, SoundSource.PLAYERS, 1f, 1f);
            }
            int newStreak = currentStreak + 1;
            if (newStreak % 5 == 0) {
                if (level.isClientSide)
                    level.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_DRAGON_GROWL, SoundSource.PLAYERS, 1f, 1f, false);
                else
                    level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_DRAGON_GROWL, SoundSource.PLAYERS, 1f, 1f);
                if (newStreak == Config.CURSED_SCROLLS_MAX.get()) {
                    if (level.isClientSide) {
                        level.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 1f, 1f, false);
                        level.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.ZOMBIE_CONVERTED_TO_DROWNED, SoundSource.PLAYERS, 1f, 1f, false);
                    } else {
                        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 1f, 1f);
                        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ZOMBIE_CONVERTED_TO_DROWNED, SoundSource.PLAYERS, 1f, 1f);
                    }
                }
            }
            tag.putInt(streakKey, newStreak);

            tag.putDouble(tagKey, newStreak * 0.025);

            Component message = Component.literal(player.getName().getString() + " rolled a scroll upgrade of ")
                    .append(Component.literal(type + " +" + newStreak).withStyle(ChatFormatting.YELLOW));
            LOGGER.info("Successful...");

            for (Player p : level.players()) {
                p.displayClientMessage(message, false);
            }
            if (newStreak == Config.CURSED_SCROLLS_MAX.get()) {
                message = Component.literal(player.getName().getString())
                        .append(Component.literal(" has perfected their ").withStyle(ChatFormatting.WHITE))
                        .append(target.getHoverName().copy())
                        .append(Component.literal(" at ").withStyle(ChatFormatting.WHITE))
                        .append(Component.literal(type + " +").withStyle(ChatFormatting.YELLOW))
                        .append(Component.literal(String.valueOf(Config.CURSED_SCROLLS_MAX.get())).withStyle(ChatFormatting.YELLOW));


                for (Player p : level.players()) {
                    p.displayClientMessage(message, false);
                }
            }
            return 2;

        } else {
            if (level.isClientSide) {
                level.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.EVOKER_PREPARE_SUMMON, SoundSource.PLAYERS, 1f, 1f, false);
                level.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 1f, 1f, false);
                level.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 1f, 1f, false);
                level.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.IRON_GOLEM_REPAIR, SoundSource.PLAYERS, 1f, 1f, false);
            } else {
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EVOKER_PREPARE_SUMMON, SoundSource.PLAYERS, 1f, 1f);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 1f, 1f);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 1f, 1f);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.IRON_GOLEM_REPAIR, SoundSource.PLAYERS, 1f, 1f);
            }
            Component message = Component.literal(player.getName().getString()).withStyle(ChatFormatting.RED)
                    .append(Component.literal(" failed to roll their ").withStyle(ChatFormatting.RED))
                    .append(target.getHoverName().copy().withStyle(ChatFormatting.RED))
                    .append(Component.literal(" at " + type + " +" + currentStreak).withStyle(ChatFormatting.RED));


            if (holy && unbreakable) {
                player.displayClientMessage(Component.literal("Your Holy Scroll prevented your item's stars resetting!")
                        .withStyle(ChatFormatting.GREEN), false);
                player.getPersistentData().putBoolean(holyKey, false);
                holy = player.getPersistentData().getBoolean(holyKey);
                if (level.isClientSide)                player.level().playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.ANVIL_HIT, SoundSource.PLAYERS, 1.0F, 1.2F, false);
                else player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ANVIL_HIT, SoundSource.PLAYERS, 1.0F, 1.2F);

            } else if (holy) {
                tag.remove(streakKey);
                tag.remove(tagKey);
                player.displayClientMessage(Component.literal("Your Holy Scroll saved your item from blowing up!")
                        .withStyle(ChatFormatting.BLUE), false);
                player.displayClientMessage(Component.literal("Your item's stars have been reset.")
                        .withStyle(ChatFormatting.RED), false);
                player.getPersistentData().putBoolean("upgradescrolls:holy", false);
            } else if (unbreakable) {
                tag.remove(streakKey);
                tag.remove(tagKey);
                player.displayClientMessage(Component.literal("Your Golden Scroll saved your item from blowing up!")
                        .withStyle(ChatFormatting.YELLOW), false);
                player.displayClientMessage(Component.literal("Your item's stars have been reset.")
                        .withStyle(ChatFormatting.RED), false);
                player.getPersistentData().putBoolean("upgradescrolls:holy", false);
            } else {
                MutableComponent itemName = (target.getHoverName().copy());
                target.shrink(1);
                    player.displayClientMessage(Component.literal("The scroll failed! Your item was destroyed.")
                            .withStyle(ChatFormatting.RED), false);
                    message = Component.literal(player.getName().getString()).withStyle(ChatFormatting.RED)
                            .append(Component.literal(" has blown up their ").withStyle(ChatFormatting.RED))
                            .append(itemName.withStyle(ChatFormatting.RED))
                            .append(Component.literal(" at " + type + " +" + currentStreak + "...").withStyle(ChatFormatting.RED));
                }
                for (Player p : level.players()) {
                    p.displayClientMessage(message, false);
                }
                return 1;
            }
        }
}
