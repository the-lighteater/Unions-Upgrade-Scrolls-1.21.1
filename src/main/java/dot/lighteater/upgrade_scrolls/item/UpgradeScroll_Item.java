package dot.lighteater.upgrade_scrolls.item;

import dot.lighteater.upgrade_scrolls.ClientConfig;
import dot.lighteater.upgrade_scrolls.network.ModNetwork;
import dot.lighteater.upgrade_scrolls.network.ScrollAnimationPacket;
import dot.lighteater.upgrade_scrolls.scroll_procedures.*;
import net.neoforged.neoforge.network.PacketDistributor;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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

import java.util.List;

public class UpgradeScroll_Item extends Item {

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
    public InteractionResultHolder<ItemStack> use(
            Level level,
            Player player,
            InteractionHand hand
    ) {
        ItemStack itemstack =
                player.getItemInHand(hand);

        ItemStack copy =
                itemstack.copy();

        if (!level.isClientSide()) {

            boolean success =
                    runScrollEffect(
                            level,
                            player,
                            scrollId,
                            null,
                            itemstack
                    );

            if (success) {
                sendScrollAnimation(
                        player,
                        copy
                );
            }
        }

        return InteractionResultHolder.sidedSuccess(
                itemstack,
                level.isClientSide()
        );
    }

    private static void sendScrollAnimation(Player player, ItemStack stack) {
        if (player instanceof ServerPlayer serverPlayer) {
                        PacketDistributor.sendToPlayer(
                                serverPlayer, new ScrollAnimationPacket(stack.copy()));
        }
    }

    public static boolean runScrollEffectFromMenu(
            ServerPlayer player,
            int id,
            ItemStack targetItem,
            ItemStack scrollItem
    ) {
        return runScrollEffect(
                player.level(),
                player,
                id,
                targetItem,
                scrollItem
        );
    }

    private static boolean runScrollEffect(Level level, Player player, int id, ItemStack targetItem, ItemStack scrollItem) {
        Runnable consumeScroll = () ->
                scrollItem.shrink(1);

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
            case 0 -> {
                return tryMystery(MysteryScrollProcedure.execute(level, player, 0),
                        consumeScroll
                );
            }
            case 1 -> {
                return handleCursedResult(CursedScrollProcedure.execute(level, player, EquipmentSlot.MAINHAND, 0, 1, 0, targetItem),
                        consumeScroll,
                        playSuccessEffect,
                        playFailedEffect);
            }
            case 2 -> {
                return handleCursedResult(CursedScrollProcedure.execute(level, player, EquipmentSlot.MAINHAND, 0, 0, 0, targetItem),
                        consumeScroll,
                        playSuccessEffect,
                        playFailedEffect);
            }
            case 3 -> {
                return tryUpgradeGolden(UpgradeScrollProcedure.execute(level, player, EquipmentSlot.MAINHAND, 0, 0,  targetItem),
                        consumeScroll,
                        playSuccessEffect
                );
            }
            case 4, 5, 6, 7 -> {
                return tryUpgradeGolden(
                        UpgradeScrollProcedure.execute(level, player, ARMOR_SLOTS[id - 4], 0, 0,  targetItem),
                        consumeScroll,
                        playSuccessEffect
                );
            }
            case 8 -> {
                return tryUpgradeGolden(UpgradeScrollProcedure.execute(level, player, EquipmentSlot.MAINHAND, 1, 0,  targetItem),
                        consumeScroll,
                        playSuccessEffect
                );
            }
            case 9, 10, 11, 12 -> {
                return handleCursedResult(CursedScrollProcedure.execute(level, player, ARMOR_SLOTS[id - 9], 0, 0, 0,  targetItem),
                        consumeScroll,
                        playSuccessEffect,
                        playFailedEffect);
            }
            case 13 -> {
                return handleCursedResult(CursedScrollProcedure.execute(level, player, EquipmentSlot.MAINHAND, 1, 0, 0,  targetItem),
                        consumeScroll,
                        playSuccessEffect,
                        playFailedEffect);
            }
            case 14 -> {
                return tryUpgradeGolden(GoldenScrollProcedure.execute(level, player, EquipmentSlot.MAINHAND, 0, 0,  targetItem),
                        consumeScroll,
                        playSuccessEffect
                );
            }
            case 15 -> {
                return tryUpgradeGolden(GoldenScrollProcedure.execute(level, player, EquipmentSlot.MAINHAND, 1, 0,  targetItem),
                        consumeScroll,
                        playSuccessEffect
                );
            }
            case 16, 17, 18, 19 -> {
                return tryUpgradeGolden(GoldenScrollProcedure.execute(level, player, ARMOR_SLOTS[id - 16], 0, 0,  targetItem),
                        consumeScroll,
                        playSuccessEffect
                );
            }
            case 20 -> {
                return tryMystery(MysteryScrollProcedure.execute(level, player, 1),
                        consumeScroll
                );
            }
            case 21 -> {
                return tryHoly(HolyScrollProcedure.execute(level, player),
                        consumeScroll,
                        playSuccessEffect
                );
            }
            case 22 -> {
                return tryUpgradeGolden(UpgradeScrollProcedure.execute(level, player, EquipmentSlot.MAINHAND, 0, 1,  targetItem),
                        consumeScroll,
                        playSuccessEffect
                );
            }
            case 23 -> {
                return handleCursedResult(CursedScrollProcedure.execute(level, player, EquipmentSlot.MAINHAND, 0, 0, 1,  targetItem),
                        consumeScroll,
                        playSuccessEffect,
                        playFailedEffect);
            }
            case 24 -> {
                return tryUpgradeGolden(GoldenScrollProcedure.execute(level, player, EquipmentSlot.MAINHAND, 0, 1,  targetItem),
                        consumeScroll,
                        playSuccessEffect
                );
            }
        }
        return false;
    }

    private static boolean handleCursedResult(
            int result,
            Runnable consume,
            Runnable success,
            Runnable failure
    ) {
        if (result == 2) {
            consume.run();
            success.run();
            return true;
        } else if (result == 1) {
            consume.run();
            failure.run();
            return true;
        } else {
            return false;
        }
    }

    private static boolean tryUpgradeGolden(
            boolean success,
            Runnable consume,
            Runnable successFx
    ) {
        if (success) {
            consume.run();
            successFx.run();
            return true;
        } else {
            return false;
        }
    }

    private static boolean tryMystery(
            boolean success,
            Runnable consume
    ) {
        if (success) {
            consume.run();
            return true;
        } else {
            return false;
        }
    }

    private static boolean tryHoly(
            boolean success,
            Runnable consume,
            Runnable successFx
    ) {
        if (success) {
            consume.run();
            successFx.run();
            return true;
        } else {
            return false;
        }
    }
}