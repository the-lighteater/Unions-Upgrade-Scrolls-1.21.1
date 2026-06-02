package dot.lighteater.upgrade_scrolls;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<Integer> GOLDEN_MYSTERY_SCROLL_DROP_RATE = BUILDER.comment("The rate golden scrolls drop compared to regular mystery scrolls, meaning for this many spawners dropping regular scrolls, 1 will drop golden mystery scrolls. (Random not coded to be a fixed ratio)")
            .defineInRange("Golden Mystery Scroll Drop Rate", 150, 1, 500);
    public static final ModConfigSpec.ConfigValue<Integer> MYSTERY_SCROLL_DROP_AMOUNT = BUILDER.comment("The upper limit of mystery scrolls dropped from spawners")
            .define("Max amount of scrolls dropped", 3);

    public static final ModConfigSpec.ConfigValue<Integer> CURSED_SCROLLS_MAX = BUILDER.comment("The limit of cursed scrolls")
            .define("Max cursed scroll level", 15);
    public static final ModConfigSpec.ConfigValue<Double> CURSED_SCROLLS_CHANCE = BUILDER.comment("The chances for cursed scrolls")
            .defineInRange("Cursed scroll chances", .8, 0, 1);


    public static final ModConfigSpec.ConfigValue<Boolean> ALLOW_BOTH_HANDS = BUILDER.comment("Allows affixes for weapons, bows, and shields to work in both hands. Disabled means it will only work in one hand.")
            .define("Allow both hands affixes", false);

    static final ModConfigSpec SPEC = BUILDER.build();

    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }
}
