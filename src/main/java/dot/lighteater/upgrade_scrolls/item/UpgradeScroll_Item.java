package dot.lighteater.upgrade_scrolls.item;

import dot.lighteater.upgrade_scrolls.scroll_procedures.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.List;

public class UpgradeScroll_Item extends Item {

    private static final Logger LOGGER = LogManager.getLogger();

    private final String[] tooltipKeys;
    private final int scrollId;

    private static final EquipmentSlot[] ARMOR_SLOTS = {
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET
    };

    public UpgradeScroll_Item(Properties properties, int scrollId, String... tooltipKeys) {
        super(properties);
        this.tooltipKeys = tooltipKeys;
        this.scrollId = scrollId;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        for (String key : tooltipKeys) {
            tooltipComponents.add(Component.translatable(key));
        }
    }

    public int getScrollId() {
        return scrollId;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        ItemStack itemstack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            runScrollEffect(level, player, scrollId, true, null, itemstack);
        }

        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }

    @Override
    public boolean overrideOtherStackedOnMe(
            ItemStack scroll,
            ItemStack targetItem,
            Slot slot,
            ClickAction action,
            Player player,
            SlotAccess access
    ) {
        if (action != ClickAction.SECONDARY) return false;
        if (scrollId == 0 || scrollId == 20 || scrollId == 21) return false;

        if (!(player.level().isClientSide && (!(player.isCreative())))) runScrollEffect(player.level(), player, scrollId, false, targetItem, scroll);
        else if (player.isCreative() && player.level().isClientSide) runScrollEffect(player.level(), player, scrollId, false, targetItem, scroll);
        return true;
    }


    private static void runScrollEffect(Level level, Player player, int id, boolean serverSide, ItemStack targetItem, ItemStack scrollItem) {
        Runnable consumeScroll = () -> {
            if (serverSide && !(player.isCreative())) scrollItem.shrink(1);
            else if (!(player.isCreative())) scrollItem.shrink(1);
        };

        Runnable playSuccessEffect = () -> {
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(
                        ParticleTypes.ENCHANTED_HIT,
                        player.getX(),
                        player.getY() + 1.0,
                        player.getZ(),
                        200,
                        0.5, 0.5, 0.5,
                        0.02
                );
            }
        };

        Runnable playFailedEffect = () -> {
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(
                        ParticleTypes.SMOKE,
                        player.getX(),
                        player.getY() + 1.0,
                        player.getZ(),
                        200,
                        0.5, 0.5, 0.5,
                        0.02
                );
            }
        };

        switch (id) {
            case 0 -> tryMystery(MysteryScrollProcedure.execute(level, player, 0),
                    consumeScroll
            );
            case 1 -> handleCursedResult(CursedScrollProcedure.execute(level, player, EquipmentSlot.MAINHAND, 0, 1, 0, serverSide, targetItem),
                    consumeScroll,
                    playSuccessEffect,
                    playFailedEffect);
            case 2 -> handleCursedResult(CursedScrollProcedure.execute(level, player, EquipmentSlot.MAINHAND, 0, 0, 0, serverSide, targetItem),
                    consumeScroll,
                    playSuccessEffect,
                    playFailedEffect);
            case 3 -> tryUpgradeGolden(UpgradeScrollProcedure.execute(level, player, EquipmentSlot.MAINHAND, 0, 0, serverSide, targetItem),
                    consumeScroll,
                    playSuccessEffect
            );
            case 4, 5, 6, 7 -> tryUpgradeGolden(
                    UpgradeScrollProcedure.execute(level, player, ARMOR_SLOTS[id - 4], 0, 0, serverSide, targetItem),
                    consumeScroll,
                    playSuccessEffect
            );
            case 8 -> tryUpgradeGolden(UpgradeScrollProcedure.execute(level, player, EquipmentSlot.MAINHAND, 1, 0, serverSide, targetItem),
                    consumeScroll,
                    playSuccessEffect
            );
            case 9, 10, 11, 12 -> handleCursedResult(CursedScrollProcedure.execute(level, player, ARMOR_SLOTS[id - 9], 0, 0, 0, serverSide, targetItem),
                    consumeScroll,
                    playSuccessEffect,
                    playFailedEffect);
            case 13 -> handleCursedResult(CursedScrollProcedure.execute(level, player, EquipmentSlot.MAINHAND, 1, 0, 0, serverSide, targetItem),
                    consumeScroll,
                    playSuccessEffect,
                    playFailedEffect);
            case 14 -> tryUpgradeGolden(GoldenScrollProcedure.execute(level, player, EquipmentSlot.MAINHAND, 0, 0, serverSide, targetItem),
                    consumeScroll,
                    playSuccessEffect
            );
            case 15 -> tryUpgradeGolden(GoldenScrollProcedure.execute(level, player, EquipmentSlot.MAINHAND, 1, 0, serverSide, targetItem),
                    consumeScroll,
                    playSuccessEffect
            );
            case 16, 17, 18, 19 -> tryUpgradeGolden(GoldenScrollProcedure.execute(level, player, ARMOR_SLOTS[id - 16], 0, 0, serverSide, targetItem),
                    consumeScroll,
                    playSuccessEffect
            );
            case 20 -> tryMystery(MysteryScrollProcedure.execute(level, player, 1),
                    consumeScroll
            );
            case 21 -> tryHoly(HolyScrollProcedure.execute(level, player),
                    consumeScroll,
                    playSuccessEffect
            );
            case 22 -> tryUpgradeGolden(UpgradeScrollProcedure.execute(level, player, EquipmentSlot.MAINHAND, 0, 1, serverSide, targetItem),
                    consumeScroll,
                    playSuccessEffect
            );
            case 23 -> handleCursedResult(CursedScrollProcedure.execute(level, player, EquipmentSlot.MAINHAND, 0, 0, 1, serverSide, targetItem),
                    consumeScroll,
                    playSuccessEffect,
                    playFailedEffect);
            case 24 -> tryUpgradeGolden(GoldenScrollProcedure.execute(level, player, EquipmentSlot.MAINHAND, 0, 1, serverSide, targetItem),
                    consumeScroll,
                    playSuccessEffect
            );
        }
    }

    private static void handleCursedResult(
            int result,
            Runnable consume,
            Runnable success,
            Runnable failure
    ) {
        if (result == 2) {
            consume.run();
            success.run();
        } else if (result == 1) {
            consume.run();
            failure.run();
        }
    }

    private static void tryUpgradeGolden(
            boolean success,
            Runnable consume,
            Runnable successFx
    ) {
        if (success) {
            consume.run();
            successFx.run();
        }
    }

    private static void tryMystery(
            boolean success,
            Runnable consume
    ) {
        if (success) {
            consume.run();
        }
    }

    private static void tryHoly(
            boolean success,
            Runnable consume,
            Runnable successFx
    ) {
        if (success) {
            consume.run();
            successFx.run();
        }
    }
}