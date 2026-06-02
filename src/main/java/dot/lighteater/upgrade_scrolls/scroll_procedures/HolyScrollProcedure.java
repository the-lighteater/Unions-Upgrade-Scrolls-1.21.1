package dot.lighteater.upgrade_scrolls.scroll_procedures;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class HolyScrollProcedure {

    public static boolean execute(Level level, Player player) {
        if (level.isClientSide) return false;

        CompoundTag data = player.getPersistentData();
        String holyKey = "upgradescrolls:holy";

        boolean active = data.getBoolean(holyKey);
        if (active) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 1f, 1f);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.VILLAGER_WORK_LEATHERWORKER, SoundSource.PLAYERS, 1f, 1f);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 1f, 1f);

            player.displayClientMessage(
                Component.literal("You already have the Holy Scroll Applied!")
                    .withStyle(ChatFormatting.RED),
                    false
            );
            return false;
        }
        data.putBoolean(holyKey, true);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1f, 1f);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.VILLAGER_WORK_WEAPONSMITH, SoundSource.PLAYERS, 1f, 1f);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ILLUSIONER_CAST_SPELL, SoundSource.PLAYERS, 1f, 1f);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ILLUSIONER_MIRROR_MOVE, SoundSource.PLAYERS, 1f, 1f);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 1f, 1f);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_DRAGON_GROWL, SoundSource.PLAYERS, 1f, 1f);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ZOMBIE_CONVERTED_TO_DROWNED, SoundSource.PLAYERS, 1f, 1f);

        player.displayClientMessage(
                Component.literal("You absorb the power of the Holy Scroll")
                        .withStyle(ChatFormatting.GREEN), false
        );
        return true;

    }

    public static boolean isHoly(Player player) {
        return player.getPersistentData().getBoolean("upgradescrolls:holy");
    }
}
