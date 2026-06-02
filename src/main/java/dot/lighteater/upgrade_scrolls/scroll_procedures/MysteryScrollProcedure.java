package dot.lighteater.upgrade_scrolls.scroll_procedures;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import dot.lighteater.upgrade_scrolls.item.ModItems;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.function.Supplier;

public class MysteryScrollProcedure {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final List<Supplier<Item>> SCROLL_POOL = List.of(
            ModItems.UPGRADE_SCROLL_1,
            ModItems.UPGRADE_SCROLL_2,
            ModItems.UPGRADE_SCROLL_3,
            ModItems.UPGRADE_SCROLL_4,
            ModItems.UPGRADE_SCROLL_5,
            ModItems.UPGRADE_SCROLL_6,
            ModItems.UPGRADE_SCROLL_7,
            ModItems.UPGRADE_SCROLL_8,
            ModItems.UPGRADE_SCROLL_9,
            ModItems.UPGRADE_SCROLL_10,
            ModItems.UPGRADE_SCROLL_11,
            ModItems.UPGRADE_SCROLL_12,
            ModItems.UPGRADE_SCROLL_13,
            ModItems.UPGRADE_SCROLL_21,
            ModItems.UPGRADE_SCROLL_22,
            ModItems.UPGRADE_SCROLL_23
    );
    private static final List<Supplier<Item>> GOLDEN_SCROLL_POOL = List.of(
            ModItems.UPGRADE_SCROLL_14,
            ModItems.UPGRADE_SCROLL_15,
            ModItems.UPGRADE_SCROLL_16,
            ModItems.UPGRADE_SCROLL_17,
            ModItems.UPGRADE_SCROLL_18,
            ModItems.UPGRADE_SCROLL_19,
            ModItems.UPGRADE_SCROLL_24
    );


    public static boolean execute(Level level, Player player, int isGolden) {
        if (!(level instanceof ServerLevel serverLevel)) return false;

        RandomSource rand = serverLevel.random;
        Item randomScroll;
        if (isGolden == 1) {
            randomScroll = GOLDEN_SCROLL_POOL.get(rand.nextInt(GOLDEN_SCROLL_POOL.size())).get();
        } else {
            randomScroll = SCROLL_POOL.get(rand.nextInt(SCROLL_POOL.size())).get();
        }
        ItemStack result = new ItemStack(randomScroll);

        ItemEntity entityToSpawn = new ItemEntity(
                serverLevel,
                player.getX(),
                player.getY() + 1,
                player.getZ(),
                result
        );
        serverLevel.addFreshEntity(entityToSpawn);

        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.VILLAGER_WORK_MASON, SoundSource.PLAYERS, 1f, 1f);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.VILLAGER_WORK_CARTOGRAPHER, SoundSource.PLAYERS, 1f, 1f);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 1f, 1f);
        return true;
    }
}