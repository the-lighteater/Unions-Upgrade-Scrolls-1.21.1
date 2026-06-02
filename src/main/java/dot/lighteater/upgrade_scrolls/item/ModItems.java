package dot.lighteater.upgrade_scrolls.item;

import dot.lighteater.upgrade_scrolls.UnionsUpgradeScrolls;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(UnionsUpgradeScrolls.MODID);

    public static final DeferredItem<Item> UPGRADE_SCROLL_0 = ITEMS.register("upgrade_scroll_0",
            () -> new UpgradeScroll_Item(
                    new Item.Properties()
                            .stacksTo(64)
                            .rarity(Rarity.COMMON), 0,
                    "tooltip.upgrade_scrolls.upgrade_scroll_0.line1",
                    "tooltip.upgrade_scrolls.upgrade_scroll_0.line2",
                    "tooltip.upgrade_scrolls.upgrade_scroll_0.line3"
            ));
    public static final DeferredItem<Item> UPGRADE_SCROLL_1 = ITEMS.register("upgrade_scroll_1",
            () -> new UpgradeScroll_Item(
                    new Item.Properties()
                            .stacksTo(64)
                            .rarity(Rarity.COMMON), 1,
                    "tooltip.upgrade_scrolls.upgrade_scroll_1-2.line1",
                    "tooltip.upgrade_scrolls.upgrade_scroll_1-2.line2",
                    "tooltip.upgrade_scrolls.upgrade_scroll_1-2.line3",
                    "tooltip.upgrade_scrolls.upgrade_scroll_1.line4",
                    "tooltip.upgrade_scrolls.upgrade_scroll_1-2.line5",
                    "tooltip.upgrade_scrolls.upgrade_scroll_1-2.line6",
                    "tooltip.upgrade_scrolls.upgrade_scroll_1-2.line7",
                    "tooltip.upgrade_scrolls.upgrade_scroll_1-2.line8"
            ));
    public static final DeferredItem<Item> UPGRADE_SCROLL_2 = ITEMS.register("upgrade_scroll_2",
            () -> new UpgradeScroll_Item(
                    new Item.Properties()
                            .stacksTo(64)
                            .rarity(Rarity.COMMON), 2,
                    "tooltip.upgrade_scrolls.upgrade_scroll_1-2.line1",
                    "tooltip.upgrade_scrolls.upgrade_scroll_1-2.line2",
                    "tooltip.upgrade_scrolls.upgrade_scroll_1-2.line3",
                    "tooltip.upgrade_scrolls.upgrade_scroll_2.line4",
                    "tooltip.upgrade_scrolls.upgrade_scroll_1-2.line5",
                    "tooltip.upgrade_scrolls.upgrade_scroll_1-2.line6",
                    "tooltip.upgrade_scrolls.upgrade_scroll_1-2.line7",
                    "tooltip.upgrade_scrolls.upgrade_scroll_1-2.line8"
            ));
    public static final DeferredItem<Item> UPGRADE_SCROLL_3 = ITEMS.register("upgrade_scroll_3",
            () -> new UpgradeScroll_Item(
                    new Item.Properties()
                            .stacksTo(64)
                            .rarity(Rarity.COMMON), 3,
                    "tooltip.upgrade_scrolls.upgrade_scroll_3.line1",
                    "tooltip.upgrade_scrolls.upgrade_scroll_3-8.line2",
                    "tooltip.upgrade_scrolls.upgrade_scroll_3-8.line3",
                    "tooltip.upgrade_scrolls.upgrade_scroll_3-8.line4",
                    "tooltip.upgrade_scrolls.upgrade_scroll_3-8.line5"
            ));
    public static final DeferredItem<Item> UPGRADE_SCROLL_4 = ITEMS.register("upgrade_scroll_4",
            () -> new UpgradeScroll_Item(
                    new Item.Properties()
                            .stacksTo(64)
                            .rarity(Rarity.COMMON), 4,
                    "tooltip.upgrade_scrolls.upgrade_scroll_4.line1",
                    "tooltip.upgrade_scrolls.upgrade_scroll_4-7.line2",
                    "tooltip.upgrade_scrolls.upgrade_scroll_3-8.line3",
                    "tooltip.upgrade_scrolls.upgrade_scroll_3-8.line4",
                    "tooltip.upgrade_scrolls.upgrade_scroll_3-8.line5"
            ));
    public static final DeferredItem<Item> UPGRADE_SCROLL_5 = ITEMS.register("upgrade_scroll_5",
            () -> new UpgradeScroll_Item(
                    new Item.Properties()
                            .stacksTo(64)
                            .rarity(Rarity.COMMON), 5,
                    "tooltip.upgrade_scrolls.upgrade_scroll_5.line1",
                    "tooltip.upgrade_scrolls.upgrade_scroll_4-7.line2",
                    "tooltip.upgrade_scrolls.upgrade_scroll_3-8.line3",
                    "tooltip.upgrade_scrolls.upgrade_scroll_3-8.line4",
                    "tooltip.upgrade_scrolls.upgrade_scroll_3-8.line5"
            ));
    public static final DeferredItem<Item> UPGRADE_SCROLL_6 = ITEMS.register("upgrade_scroll_6",
            () -> new UpgradeScroll_Item(
                    new Item.Properties()
                            .stacksTo(64)
                            .rarity(Rarity.COMMON), 6,
                    "tooltip.upgrade_scrolls.upgrade_scroll_6.line1",
                    "tooltip.upgrade_scrolls.upgrade_scroll_4-7.line2",
                    "tooltip.upgrade_scrolls.upgrade_scroll_3-8.line3",
                    "tooltip.upgrade_scrolls.upgrade_scroll_3-8.line4",
                    "tooltip.upgrade_scrolls.upgrade_scroll_3-8.line5"
            ));
    public static final DeferredItem<Item> UPGRADE_SCROLL_7 = ITEMS.register("upgrade_scroll_7",
            () -> new UpgradeScroll_Item(
                    new Item.Properties()
                            .stacksTo(64)
                            .rarity(Rarity.COMMON), 7,
                    "tooltip.upgrade_scrolls.upgrade_scroll_7.line1",
                    "tooltip.upgrade_scrolls.upgrade_scroll_4-7.line2",
                    "tooltip.upgrade_scrolls.upgrade_scroll_3-8.line3",
                    "tooltip.upgrade_scrolls.upgrade_scroll_3-8.line4",
                    "tooltip.upgrade_scrolls.upgrade_scroll_3-8.line5"
            ));
    public static final DeferredItem<Item> UPGRADE_SCROLL_8 = ITEMS.register("upgrade_scroll_8",
            () -> new UpgradeScroll_Item(
                    new Item.Properties()
                            .stacksTo(64)
                            .rarity(Rarity.COMMON), 8,
                    "tooltip.upgrade_scrolls.upgrade_scroll_8.line1",
                    "tooltip.upgrade_scrolls.upgrade_scroll_3-8.line2",
                    "tooltip.upgrade_scrolls.upgrade_scroll_3-8.line3",
                    "tooltip.upgrade_scrolls.upgrade_scroll_3-8.line4",
                    "tooltip.upgrade_scrolls.upgrade_scroll_3-8.line5"
            ));
    public static final DeferredItem<Item> UPGRADE_SCROLL_9 = ITEMS.register("upgrade_scroll_9",
            () -> new UpgradeScroll_Item(
                    new Item.Properties()
                            .stacksTo(64)
                            .rarity(Rarity.COMMON), 9,
                    "tooltip.upgrade_scrolls.upgrade_scroll_9.line1",
                    "tooltip.upgrade_scrolls.upgrade_scroll_9-12.line2",
                    "tooltip.upgrade_scrolls.upgrade_scroll_9-13.line3",
                    "tooltip.upgrade_scrolls.upgrade_scroll_9-13.line4",
                    "tooltip.upgrade_scrolls.upgrade_scroll_9-13.line5",
                    "tooltip.upgrade_scrolls.upgrade_scroll_9-13.line6",
                    "tooltip.upgrade_scrolls.upgrade_scroll_9-13.line7",
                    "tooltip.upgrade_scrolls.upgrade_scroll_9-13.line8"
            ));
    public static final DeferredItem<Item> UPGRADE_SCROLL_10 = ITEMS.register("upgrade_scroll_10",
            () -> new UpgradeScroll_Item(
                    new Item.Properties()
                            .stacksTo(64)
                            .rarity(Rarity.COMMON), 10,
                    "tooltip.upgrade_scrolls.upgrade_scroll_10.line1",
                    "tooltip.upgrade_scrolls.upgrade_scroll_9-12.line2",
                    "tooltip.upgrade_scrolls.upgrade_scroll_9-13.line3",
                    "tooltip.upgrade_scrolls.upgrade_scroll_9-13.line4",
                    "tooltip.upgrade_scrolls.upgrade_scroll_9-13.line5",
                    "tooltip.upgrade_scrolls.upgrade_scroll_9-13.line6",
                    "tooltip.upgrade_scrolls.upgrade_scroll_9-13.line7",
                    "tooltip.upgrade_scrolls.upgrade_scroll_9-13.line8"
            ));
    public static final DeferredItem<Item> UPGRADE_SCROLL_11  = ITEMS.register("upgrade_scroll_11",
            () -> new UpgradeScroll_Item(
                    new Item.Properties()
                            .stacksTo(64)
                            .rarity(Rarity.COMMON), 11,
                    "tooltip.upgrade_scrolls.upgrade_scroll_11.line1",
                    "tooltip.upgrade_scrolls.upgrade_scroll_9-12.line2",
                    "tooltip.upgrade_scrolls.upgrade_scroll_9-13.line3",
                    "tooltip.upgrade_scrolls.upgrade_scroll_9-13.line4",
                    "tooltip.upgrade_scrolls.upgrade_scroll_9-13.line5",
                    "tooltip.upgrade_scrolls.upgrade_scroll_9-13.line6",
                    "tooltip.upgrade_scrolls.upgrade_scroll_9-13.line7",
                    "tooltip.upgrade_scrolls.upgrade_scroll_9-13.line8"
            ));
    public static final DeferredItem<Item> UPGRADE_SCROLL_12 = ITEMS.register("upgrade_scroll_12",
            () -> new UpgradeScroll_Item(
                    new Item.Properties()
                            .stacksTo(64)
                            .rarity(Rarity.COMMON), 12,
                    "tooltip.upgrade_scrolls.upgrade_scroll_12.line1",
                    "tooltip.upgrade_scrolls.upgrade_scroll_9-12.line2",
                    "tooltip.upgrade_scrolls.upgrade_scroll_9-13.line3",
                    "tooltip.upgrade_scrolls.upgrade_scroll_9-13.line4",
                    "tooltip.upgrade_scrolls.upgrade_scroll_9-13.line5",
                    "tooltip.upgrade_scrolls.upgrade_scroll_9-13.line6",
                    "tooltip.upgrade_scrolls.upgrade_scroll_9-13.line7",
                    "tooltip.upgrade_scrolls.upgrade_scroll_9-13.line8"
            ));
    public static final DeferredItem<Item> UPGRADE_SCROLL_13 = ITEMS.register("upgrade_scroll_13",
            () -> new UpgradeScroll_Item(
                    new Item.Properties()
                            .stacksTo(64)
                            .rarity(Rarity.COMMON), 13,
                    "tooltip.upgrade_scrolls.upgrade_scroll_13.line1",
                    "tooltip.upgrade_scrolls.upgrade_scroll_13.line2",
                    "tooltip.upgrade_scrolls.upgrade_scroll_9-13.line3",
                    "tooltip.upgrade_scrolls.upgrade_scroll_9-13.line4",
                    "tooltip.upgrade_scrolls.upgrade_scroll_9-13.line5",
                    "tooltip.upgrade_scrolls.upgrade_scroll_9-13.line6",
                    "tooltip.upgrade_scrolls.upgrade_scroll_9-13.line7",
                    "tooltip.upgrade_scrolls.upgrade_scroll_9-13.line8"
            ));
    public static final DeferredItem<Item> UPGRADE_SCROLL_14 = ITEMS.register("upgrade_scroll_14",
            () -> new UpgradeScroll_Item(
                    new Item.Properties()
                            .stacksTo(64)
                            .rarity(Rarity.COMMON), 14,
                    "tooltip.upgrade_scrolls.upgrade_scroll_14.line1",
                    "tooltip.upgrade_scrolls.upgrade_scroll_14.line2",
                    "tooltip.upgrade_scrolls.upgrade_scroll_14-19.line3",
                    "tooltip.upgrade_scrolls.upgrade_scroll_14-19.line4"
            ));
    public static final DeferredItem<Item> UPGRADE_SCROLL_15 = ITEMS.register("upgrade_scroll_15",
            () -> new UpgradeScroll_Item(
                    new Item.Properties()
                            .stacksTo(64)
                            .rarity(Rarity.COMMON), 15,
                    "tooltip.upgrade_scrolls.upgrade_scroll_15.line1",
                    "tooltip.upgrade_scrolls.upgrade_scroll_15.line2",
                    "tooltip.upgrade_scrolls.upgrade_scroll_14-19.line3",
                    "tooltip.upgrade_scrolls.upgrade_scroll_14-19.line4"
            ));
    public static final DeferredItem<Item> UPGRADE_SCROLL_16 = ITEMS.register("upgrade_scroll_16",
            () -> new UpgradeScroll_Item(
                    new Item.Properties()
                            .stacksTo(64)
                            .rarity(Rarity.COMMON), 16,
                    "tooltip.upgrade_scrolls.upgrade_scroll_16.line1",
                    "tooltip.upgrade_scrolls.upgrade_scroll_16-19.line2",
                    "tooltip.upgrade_scrolls.upgrade_scroll_14-19.line3",
                    "tooltip.upgrade_scrolls.upgrade_scroll_14-19.line4"
            ));
    public static final DeferredItem<Item> UPGRADE_SCROLL_17 = ITEMS.register("upgrade_scroll_17",
            () -> new UpgradeScroll_Item(
                    new Item.Properties()
                            .stacksTo(64)
                            .rarity(Rarity.COMMON), 17,
                    "tooltip.upgrade_scrolls.upgrade_scroll_17.line1",
                    "tooltip.upgrade_scrolls.upgrade_scroll_16-19.line2",
                    "tooltip.upgrade_scrolls.upgrade_scroll_14-19.line3",
                    "tooltip.upgrade_scrolls.upgrade_scroll_14-19.line4"
            ));
    public static final DeferredItem<Item> UPGRADE_SCROLL_18 = ITEMS.register("upgrade_scroll_18",
            () -> new UpgradeScroll_Item(
                    new Item.Properties()
                            .stacksTo(64)
                            .rarity(Rarity.COMMON), 18,
                    "tooltip.upgrade_scrolls.upgrade_scroll_18.line1",
                    "tooltip.upgrade_scrolls.upgrade_scroll_16-19.line2",
                    "tooltip.upgrade_scrolls.upgrade_scroll_14-19.line3",
                    "tooltip.upgrade_scrolls.upgrade_scroll_14-19.line4"
            ));
    public static final DeferredItem<Item> UPGRADE_SCROLL_19 = ITEMS.register("upgrade_scroll_19",
            () -> new UpgradeScroll_Item(
                    new Item.Properties()
                            .stacksTo(64)
                            .rarity(Rarity.COMMON), 19,
                    "tooltip.upgrade_scrolls.upgrade_scroll_19.line1",
                    "tooltip.upgrade_scrolls.upgrade_scroll_16-19.line2",
                    "tooltip.upgrade_scrolls.upgrade_scroll_14-19.line3",
                    "tooltip.upgrade_scrolls.upgrade_scroll_14-19.line4"
            ));
    public static final DeferredItem<Item> UPGRADE_SCROLL_20 = ITEMS.register("upgrade_scroll_20",
            () -> new UpgradeScroll_Item(
                    new Item.Properties()
                            .stacksTo(64)
                            .rarity(Rarity.COMMON), 20,
                    "tooltip.upgrade_scrolls.upgrade_scroll_20.line1",
                    "tooltip.upgrade_scrolls.upgrade_scroll_20.line2",
                    "tooltip.upgrade_scrolls.upgrade_scroll_20.line3"
            ));
    public static final DeferredItem<Item> UPGRADE_SCROLL_21 = ITEMS.register("upgrade_scroll_21",
            () -> new UpgradeScroll_Item(
                    new Item.Properties()
                            .stacksTo(64)
                            .rarity(Rarity.COMMON), 21,
                    "tooltip.upgrade_scrolls.upgrade_scroll_21.line1",
                    "tooltip.upgrade_scrolls.upgrade_scroll_21.line2",
                    "tooltip.upgrade_scrolls.upgrade_scroll_21.line3",
                    "tooltip.upgrade_scrolls.upgrade_scroll_21.line4"
            ));

    public static final DeferredItem<Item> UPGRADE_SCROLL_22 = ITEMS.register("upgrade_scroll_affix_curio",
            () -> new UpgradeScroll_Item(
                    new Item.Properties()
                            .stacksTo(64)
                            .rarity(Rarity.COMMON), 22,
                    "tooltip.upgrade_scrolls.upgrade_scroll_affix_curio.line1",
                    "tooltip.upgrade_scrolls.upgrade_scroll_affix_curio.line2",
                    "tooltip.upgrade_scrolls.upgrade_scroll_affix_curio.line3",
                    "tooltip.upgrade_scrolls.upgrade_scroll_affix_curio.line4",
                    "tooltip.upgrade_scrolls.upgrade_scroll_affix_curio.line5"
            ));
    public static final DeferredItem<Item> UPGRADE_SCROLL_23 = ITEMS.register("upgrade_scroll_cursed_curio",
            () -> new UpgradeScroll_Item(
                    new Item.Properties()
                            .stacksTo(64)
                            .rarity(Rarity.COMMON), 23,
                    "tooltip.upgrade_scrolls.upgrade_scroll_cursed_curio.line1",
                    "tooltip.upgrade_scrolls.upgrade_scroll_cursed_curio.line2",
                    "tooltip.upgrade_scrolls.upgrade_scroll_cursed_curio.line3",
                    "tooltip.upgrade_scrolls.upgrade_scroll_cursed_curio.line4",
                    "tooltip.upgrade_scrolls.upgrade_scroll_cursed_curio.line5",
                    "tooltip.upgrade_scrolls.upgrade_scroll_cursed_curio.line6",
                    "tooltip.upgrade_scrolls.upgrade_scroll_cursed_curio.line7",
                    "tooltip.upgrade_scrolls.upgrade_scroll_cursed_curio.line8"
            ));
    public static final DeferredItem<Item> UPGRADE_SCROLL_24 = ITEMS.register("upgrade_scroll_golden_curio",
            () -> new UpgradeScroll_Item(
                    new Item.Properties()
                            .stacksTo(64)
                            .rarity(Rarity.COMMON), 24,
                    "tooltip.upgrade_scrolls.upgrade_scroll_golden_curio.line1",
                    "tooltip.upgrade_scrolls.upgrade_scroll_golden_curio.line2",
                    "tooltip.upgrade_scrolls.upgrade_scroll_golden_curio.line3",
                    "tooltip.upgrade_scrolls.upgrade_scroll_golden_curio.line4"
            ));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
