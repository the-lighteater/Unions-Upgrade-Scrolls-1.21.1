package dot.lighteater.upgrade_scrolls.event;

import dot.lighteater.upgrade_scrolls.UpgradeScrolls;
import dot.lighteater.upgrade_scrolls.affix.AffixData;
import dot.lighteater.upgrade_scrolls.affix.AffixLoader;
import dot.lighteater.upgrade_scrolls.data.ModDataComponents;
import dot.lighteater.upgrade_scrolls.item.UpgradeScroll_Item;
import dot.lighteater.upgrade_scrolls.utility.CursedConfigLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

@EventBusSubscriber(modid = UpgradeScrolls.MODID, value = Dist.CLIENT)
public class ClientTooltipEvents {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) return;

        CompoundTag tag = stack.get(ModDataComponents.AFFIX_DATA.get());
        if (tag == null) tag = new CompoundTag();

        // Iterate over all loaded affixes
        for (AffixData affix : AffixLoader.getAll()) {
            if (affix == null) continue;

            String affixName = affix.getName(); // e.g., "fabled"

            if (affixName.isEmpty()) {
                continue;
            }

            // Check all keys in the item's NBT that start with this affix
            for (String nbtKey : tag.getAllKeys()) {
                if (!nbtKey.startsWith("mod:tag_affix_" + affixName)) continue;

                // If the tag has a value > 0, the affix exists
                if (tag.getDouble(nbtKey) > 0) {
                    String displayName = affixName.substring(0, 1).toUpperCase() + affixName.substring(1);

                    event.getToolTip().add(
                            Component.literal("★ Affix: ").withStyle(ChatFormatting.WHITE)
                                    .append(Component.literal(affix.getDisplayName())
                                            .setStyle(Style.EMPTY.withObfuscated(displayName.equals("Fabled")).withColor(affix.getColorNumber())))
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
                    for (int i = 1; i <= CursedConfigLoader.getMaxLevel(); i++) {
                        if (i <= streak) {
                            stars.append("★");
                        } else {
                            stars.append("☆");
                        }

                        // Add a space every 5 stars, except after the last group
                        if (i % 5 == 0 && i != CursedConfigLoader.getMaxLevel()) {
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

        if (!(stack.getItem() instanceof UpgradeScroll_Item scroll)) {
            return;
        }

        List<Component> tooltip = event.getToolTip();

        switch (scroll.getScrollId()) {
            case 3, 4, 5, 6, 7, 8, 22 -> AffixLoader.getAll().stream()
                    .filter(affix -> affix != null && !affix.isDisabled())
                    .sorted(
                            Comparator.comparingDouble(
                                    AffixData::getWeight
                            ).reversed()
                    )
                    .forEach(affix -> {
                        double percentage = new BigDecimal(String.valueOf(affix.getPercentage() * 100))
                                .setScale(2, RoundingMode.UP)
                                .doubleValue();

                        String tooltipKey = (percentage) + "% " + affix.getDisplayName();

                        if (affix.isHidden()) {
                            tooltip.add(
                                    Component.translatable(
                                                    tooltipKey
                                            ).withStyle(style -> style.withColor(affix.getColorNumber()))
                                            .withStyle(ChatFormatting.OBFUSCATED)
                            );
                        } else {
                            tooltip.add(
                                    Component.translatable(
                                            tooltipKey
                                    ).withStyle(style -> style.withColor(affix.getColorNumber()))
                            );
                        }
                    });
        }
    }
}
