package dot.lighteater.upgrade_scrolls.scroll_procedures;

import dot.lighteater.upgrade_scrolls.Config;
import dot.lighteater.upgrade_scrolls.data.ModDataComponents;
import dot.lighteater.upgrade_scrolls.utility.CursedConfigLoader;
import dot.lighteater.upgrade_scrolls.utility.ModUtility;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class CursedScrollProcedure {

    private static final Logger LOGGER = LogManager.getLogger();

    public static int execute(Level level, Player player, EquipmentSlot slot, int isShield, int isMagician, int isCurio, ItemStack targetItem) {
        boolean armorSlot = slot == EquipmentSlot.HEAD || slot == EquipmentSlot.CHEST ||
                slot == EquipmentSlot.LEGS || slot == EquipmentSlot.FEET;

        ItemStack target =
                targetItem != null
                        ? targetItem
                        : player.getItemBySlot(slot);

        int itemType = ModUtility.isValidWeaponOrCurio(target);
        if (itemType != 0 && armorSlot) return 0;

        if (target.isEmpty()) {
            player.displayClientMessage(Component.literal("No item equipped in " + slot.getName()), true);
            return 0;
        } else if ((itemType == 0) && slot != EquipmentSlot.HEAD && slot != EquipmentSlot.CHEST
                && slot != EquipmentSlot.LEGS && slot != EquipmentSlot.FEET
                || (isShield == 0 && ModUtility.isValidShield(target))) {
            return 0;
        }

        if (isShield == 0 && ModUtility.isValidShield(target)) return 0;
        else if (isShield != 0 && !(ModUtility.isValidShield(target))) return 0;

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

        if (currentStreak >= CursedConfigLoader.getMaxLevel()) {
            player.displayClientMessage(Component.literal("This item has reached its maximum streak!"), false);
            return 0;
        } else if (tag.getInt(oppStreakKey) != 0) {
            return 0;
        }

        double roll = level.random.nextDouble();
        double successChance = CursedConfigLoader.getChance();
        boolean success = roll <= successChance;

        if (success) {
            playCursedSFX(CursedConfigLoader.getSuccessSounds(), level, player);
            int newStreak = currentStreak + 1;
            if (CursedConfigLoader.isMilestone(newStreak)) {
                playCursedSFX(CursedConfigLoader.getMilestoneSound(), level, player);
                if (newStreak == CursedConfigLoader.getMaxLevel()) {
                    playCursedSFX(CursedConfigLoader.getPerfectedSounds(), level, player);
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
            if (newStreak == CursedConfigLoader.getMaxLevel()) {
                message = Component.literal(player.getName().getString())
                        .append(Component.literal(" has perfected their ").withStyle(ChatFormatting.WHITE))
                        .append(target.getHoverName().copy())
                        .append(Component.literal(" at ").withStyle(ChatFormatting.WHITE))
                        .append(Component.literal(type + " +").withStyle(ChatFormatting.YELLOW))
                        .append(Component.literal(String.valueOf(CursedConfigLoader.getMaxLevel())).withStyle(ChatFormatting.YELLOW));


                for (Player p : level.players()) {
                    p.displayClientMessage(message, false);
                }
            }
            return 2;

        } else {
            playCursedSFX(CursedConfigLoader.getFailureSounds(), level, player);

            Component message = Component.literal(player.getName().getString()).withStyle(ChatFormatting.RED)
                    .append(Component.literal(" failed to roll their ").withStyle(ChatFormatting.RED))
                    .append(target.getHoverName().copy().withStyle(ChatFormatting.RED))
                    .append(Component.literal(" at " + type + " +" + currentStreak).withStyle(ChatFormatting.RED));

            if (holy && unbreakable) {
                player.displayClientMessage(Component.literal("Your Holy Scroll prevented your item's stars resetting!")
                        .withStyle(ChatFormatting.GREEN), false);
                player.getPersistentData().putBoolean(holyKey, false);
                playCursedSFX(CursedConfigLoader.getHolyProtectionSound(), level, player);
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


    private static void playCursedSFX(List<String> soundIDs, Level level, Player player) {
        for (String soundID : soundIDs) {
            SoundEvent sound = ModUtility.getSoundEvent(soundID);
            if (sound == null) continue;

            level.playSound(null, player.getX(), player.getY(), player.getZ(), sound, SoundSource.PLAYERS, 1f, 1f);
        }
    }
}
