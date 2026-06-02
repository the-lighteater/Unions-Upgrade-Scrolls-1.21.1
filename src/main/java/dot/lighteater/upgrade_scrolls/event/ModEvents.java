package dot.lighteater.upgrade_scrolls.event;

import dot.lighteater.upgrade_scrolls.Config;
import dot.lighteater.upgrade_scrolls.UnionsUpgradeScrolls;
import dot.lighteater.upgrade_scrolls.data.ModDataComponents;
import dot.lighteater.upgrade_scrolls.item.ModItems;
import dot.lighteater.upgrade_scrolls.item.UpgradeScroll_Item;
import dot.lighteater.upgrade_scrolls.scroll_procedures.AffixData;
import dot.lighteater.upgrade_scrolls.scroll_procedures.AffixLoader;
import dot.lighteater.upgrade_scrolls.utility.ItemUtility;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

// Events handled by the mod.

@EventBusSubscriber(modid = UnionsUpgradeScrolls.MODID)
public class ModEvents {

    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();

        ItemStack offHand = player.getOffhandItem();
        ItemStack mainHand = player.getMainHandItem();

        if ((offHand.getItem() instanceof UpgradeScroll_Item) &&
                (ItemUtility.isValidShield(mainHand) ||
                        ItemUtility.isValidWeaponOrCurio(mainHand) != 0 ||
                        ItemUtility.isValidBow(mainHand))) {

            player.getCooldowns().addCooldown(mainHand.getItem(), 20);
        }
    }


    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) return;

        CompoundTag tag = stack.get(ModDataComponents.AFFIX_DATA.get());
        if (tag == null) tag = new CompoundTag();

        // Iterate over all loaded affixes
        for (AffixData affix : AffixLoader.getAll()) {
            String affixName = affix.getName(); // e.g., "fabled"

            // Check all keys in the item's NBT that start with this affix
            for (String nbtKey : tag.getAllKeys()) {
                if (!nbtKey.startsWith("mod:tag_affix_" + affixName)) continue;

                // If the tag has a value > 0, the affix exists
                if (tag.getDouble(nbtKey) > 0) {
                    String displayName = affixName.substring(0, 1).toUpperCase() + affixName.substring(1);

                    ChatFormatting color = affix.getColor();
                    if (color == null) color = ChatFormatting.WHITE;

                    event.getToolTip().add(
                            Component.literal("★ Affix: ").withStyle(ChatFormatting.WHITE)
                                    .append(Component.literal(affix.getDisplayName())
                                            .setStyle(Style.EMPTY.withObfuscated(displayName.equals("Fabled")).withColor(color)))
                    );
                    break;
                }
            }
        }

        String[] cursedSlots = {"_head", "_chest", "_legs", "_feet", "", "_curio", "_shield", "_barbarian", "_magician"};
        for (String slotKey : cursedSlots) {
            String streakKey = "upgradescrolls:streak" + slotKey;
            if (tag.contains(streakKey)) {
                int streak = tag.getInt(streakKey);
                if (streak > 0) {
                    String type = "Guardian";
                    if (slotKey.equals("_barbarian")) type = "Barbarian";
                    else if (slotKey.equals("_magician")) type = "Magician";
                    event.getToolTip().add(Component.literal("★ " + type + " + " + streak)
                            .withStyle(ChatFormatting.YELLOW));

                    // Star display
                    StringBuilder stars = new StringBuilder();
                    for (int i = 1; i <= Config.CURSED_SCROLLS_MAX.get(); i++) {
                        if (i <= streak) {
                            stars.append("★");
                        } else {
                            stars.append("☆");
                        }

                        // Add a space every 5 stars, except after the last group
                        if (i % 5 == 0 && i != Config.CURSED_SCROLLS_MAX.get()) {
                            stars.append(" ");
                        }
                    }

                    event.getToolTip().add(Component.literal(stars.toString()).withStyle(ChatFormatting.WHITE));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onScrollTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        if (!(stack.getItem() instanceof UpgradeScroll_Item scroll)) return;

        List<Component> tooltip = event.getToolTip();

        switch (scroll.getScrollId()) {
            case 3, 4, 5, 6, 7, 8, 22 -> AffixLoader.getAll().stream()
                    .filter(affix -> !affix.isDisabled())
                    .sorted(Comparator.comparingDouble(AffixData::getWeight).reversed())
                    .forEach(affix -> tooltip.add(Component.translatable(affix.getTooltip()).withStyle(affix.getColor())));
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
