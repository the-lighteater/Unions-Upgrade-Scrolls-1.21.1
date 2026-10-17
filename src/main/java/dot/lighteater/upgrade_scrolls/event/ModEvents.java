package dot.lighteater.upgrade_scrolls.event;

import dot.lighteater.upgrade_scrolls.Config;
import dot.lighteater.upgrade_scrolls.UpgradeScrolls;
import dot.lighteater.upgrade_scrolls.data.ModDataComponents;
import dot.lighteater.upgrade_scrolls.item.ModItems;
import dot.lighteater.upgrade_scrolls.item.UpgradeScroll_Item;
import dot.lighteater.upgrade_scrolls.affix.AffixData;
import dot.lighteater.upgrade_scrolls.affix.AffixLoader;
import dot.lighteater.upgrade_scrolls.network.AffixSyncPayload;
import dot.lighteater.upgrade_scrolls.utility.CursedConfigLoader;
import dot.lighteater.upgrade_scrolls.utility.ModUtility;
import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

// Events handled by the mod.

@EventBusSubscriber(modid = UpgradeScrolls.MODID)
public class ModEvents {

    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            UpgradeScrolls.LOGGER.info(
                    "[AffixSync] Sending affixes to {}",
                    player.getGameProfile().getName()
            );

            AffixSyncPayload.sendTo(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();

        ItemStack offHand = player.getOffhandItem();
        ItemStack mainHand = player.getMainHandItem();

        if ((offHand.getItem() instanceof UpgradeScroll_Item) &&
                (ModUtility.isValidShield(mainHand) ||
                        ModUtility.isValidWeaponOrCurio(mainHand) != 0 ||
                        ModUtility.isValidBow(mainHand))) {

            player.getCooldowns().addCooldown(mainHand.getItem(), 20);
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingIncomingDamageEvent event) {

        LivingEntity target = event.getEntity();

        // attacker comes from source in this event version
        if (!(event.getSource().getEntity() instanceof Player player)) return;

        ItemStack weapon = player.getMainHandItem();
        if (weapon.isEmpty()) return;

        CompoundTag tag = weapon.get(ModDataComponents.AFFIX_DATA.get());
        if (tag == null) return;

        double magicianPower = tag.getDouble("upgradescrolls:level_bonus_magician");
        if (magicianPower <= 0) return;

        float original = event.getAmount();

        // ADD DAMAGE (safe modification instead of spawning new hit)
        float bonus = (float) (original * magicianPower);

        event.setAmount(original + bonus);
    }

    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        ResourceLocation name = event.getName();

        int minScrolls = Config.MYSTERY_SCROLL_DROP_AMOUNT.get() > 0 ? 1 : 0;

        if (name.equals(ResourceLocation.fromNamespaceAndPath("minecraft", "blocks/spawner")) ||
                name.equals(ResourceLocation.fromNamespaceAndPath("iceandfire", "blocks/dread_spawner")) ||
                name.equals(ResourceLocation.fromNamespaceAndPath("goety", "blocks/void_spawner"))) {
            LootPool pool = LootPool.lootPool()
                    .name("extra_drops")
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(ModItems.UPGRADE_SCROLL_0.get()).setWeight(Config.GOLDEN_MYSTERY_SCROLL_DROP_RATE.get())
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(minScrolls, Config.MYSTERY_SCROLL_DROP_AMOUNT.get()))))
                    .add(LootItem.lootTableItem(ModItems.UPGRADE_SCROLL_20.get()).setWeight(1)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(minScrolls, Config.MYSTERY_SCROLL_DROP_AMOUNT.get()))))
                    .build();

            LootTable table = event.getTable();
            table.addPool(pool);
        }
    }
}
