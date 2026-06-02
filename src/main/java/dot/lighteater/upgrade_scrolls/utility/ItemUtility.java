package dot.lighteater.upgrade_scrolls.utility;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.ModList;

public class ItemUtility {
    private static final ResourceLocation[] ADDITIONAL_WEAPON_TAGS = new ResourceLocation[] {
            ResourceLocation.fromNamespaceAndPath("forge", "swords"),
            ResourceLocation.fromNamespaceAndPath("forge", "tools/swords"),
            ResourceLocation.fromNamespaceAndPath("forge", "tools/axe"),
            ResourceLocation.fromNamespaceAndPath("forge", "tools/pickaxes"),
            ResourceLocation.fromNamespaceAndPath("forge", "tools/trident"),

            ResourceLocation.fromNamespaceAndPath("forge", "knives"),
            ResourceLocation.fromNamespaceAndPath("forge", "tools/knives"),

            ResourceLocation.fromNamespaceAndPath("forge", "spears"),
            ResourceLocation.fromNamespaceAndPath("forge", "tools/spears"),

            ResourceLocation.fromNamespaceAndPath("forge", "maces"),
            ResourceLocation.fromNamespaceAndPath("forge", "tools/maces"),

            ResourceLocation.fromNamespaceAndPath("forge", "hammers"),
            ResourceLocation.fromNamespaceAndPath("forge", "tools/hammers"),

            ResourceLocation.fromNamespaceAndPath("forge", "battleaxes"),
            ResourceLocation.fromNamespaceAndPath("forge", "tools/battleaxes"),

            ResourceLocation.fromNamespaceAndPath("forge", "throwing_weapons"),
            ResourceLocation.fromNamespaceAndPath("upgrade_scrolls", "weapon_whitelist")
    };

    private static final ResourceLocation[] CURIOS_TAGS = new ResourceLocation[] {
            ResourceLocation.fromNamespaceAndPath("curios", "head"),
            ResourceLocation.fromNamespaceAndPath("upgrade_scrolls", "curio_whitelist")
    };

    private static final ResourceLocation[] ADDITIONAL_BOW_TAGS = new ResourceLocation[] {
            ResourceLocation.fromNamespaceAndPath("forge", "tools/bows"),
            ResourceLocation.fromNamespaceAndPath("forge", "crossbows"),
            ResourceLocation.fromNamespaceAndPath("upgrade_scrolls", "bow_whitelist")
    };

    private static final ResourceLocation[] ADDITIONAL_SHIELD_TAGS = new ResourceLocation[] {
            ResourceLocation.fromNamespaceAndPath("forge", "tools/shields"),
            ResourceLocation.fromNamespaceAndPath("upgrade_scrolls", "shield_whitelist")
    };

    private static final ResourceLocation[] ADDITIONAL_SPAWNER_TAGS = new ResourceLocation[] {
            ResourceLocation.fromNamespaceAndPath("upgrade_scrolls", "spawner_whitelist")
    };

    private static final ResourceLocation[] ADDITIONAL_HELMET_TAGS = new ResourceLocation[] {
            ResourceLocation.parse("forge:helmets"),
            ResourceLocation.parse("forge:armors/helmets"),
            ResourceLocation.fromNamespaceAndPath("upgrade_scrolls", "helmet_whitelist")
    };

    private static final ResourceLocation[] ADDITIONAL_CHESTPLATE_TAGS = new ResourceLocation[] {
            ResourceLocation.parse("forge:chestplates"),
            ResourceLocation.parse("forge:armors/chestplates"),
            ResourceLocation.fromNamespaceAndPath("upgrade_scrolls", "chestplate_whitelist")
    };

    private static final ResourceLocation[] ADDITIONAL_LEGGINGS_TAGS = new ResourceLocation[] {
            ResourceLocation.parse("forge:leggings"),
            ResourceLocation.parse("forge:armors/leggings"),
            ResourceLocation.fromNamespaceAndPath("upgrade_scrolls", "leggings_whitelist")
    };

    private static final ResourceLocation[] ADDITIONAL_BOOTS_TAGS = new ResourceLocation[] {
            ResourceLocation.parse("forge:boots"),
            ResourceLocation.parse("forge:armors/boots"),
            ResourceLocation.fromNamespaceAndPath("upgrade_scrolls", "boots_whitelist")
    };

    public static int isValidWeaponOrCurio(ItemStack stack) {
        Item item = stack.getItem();

        if (item instanceof SwordItem ||
                item instanceof AxeItem ||
                item instanceof PickaxeItem ||
                item instanceof ShovelItem ||
                item instanceof HoeItem ||
                item instanceof ShieldItem ||
                item instanceof BowItem ||
                item instanceof CrossbowItem ||
                item instanceof TridentItem) {
            return 1;
        }

        for (ResourceLocation rl : ADDITIONAL_WEAPON_TAGS) {
            if (stack.is(ItemTags.create(rl))) {
                return 1;
            }
        }

        for (ResourceLocation rl : CURIOS_TAGS) {
            if (stack.is(ItemTags.create(rl))) {
                return 2;
            }
        }

        for (ResourceLocation rl : ADDITIONAL_SHIELD_TAGS) {
            if (stack.is(ItemTags.create(rl))) {
                return 1;
            }
        }

        for (ResourceLocation rl : ADDITIONAL_BOW_TAGS) {
            if (stack.is(ItemTags.create(rl))) {
                return 1;
            }
        }


        if (ModList.get().isLoaded("spartanweaponry")) {
            try {
                Class<?> spartanItemClass = Class.forName("net.spartanweaponry.item.SpartanWeaponItem");
                if (spartanItemClass.isInstance(item)) {
                    return 1;
                }

                Class<?> throwableItemClass = Class.forName("net.spartanweaponry.item.ThrowingWeaponItem");
                if (throwableItemClass.isInstance(item)) {

                    return 1;
                }
            } catch (ClassNotFoundException ignored) {}
        }

        if (ModList.get().isLoaded("curios")) {
            try {
                Class<?> curioClass = Class.forName("top.theillusivec4.curios.api.type.capability.ICurioItem");
                if (curioClass.isInstance(item)) {
                    return 2;
                }
            } catch (ClassNotFoundException ignored) {}
        }


        return 0;
    }

    public static boolean isValidWeapon(ItemStack stack) {
        Item item = stack.getItem();

        if (item instanceof SwordItem ||
                item instanceof AxeItem ||
                item instanceof PickaxeItem ||
                item instanceof ShovelItem ||
                item instanceof HoeItem ||
                item instanceof TridentItem) {
            return true;
        }

        for (ResourceLocation rl : ADDITIONAL_WEAPON_TAGS) {
            if (stack.is(ItemTags.create(rl))) {
                return true;
            }
        }

        if (ModList.get().isLoaded("spartanweaponry")) {
            try {
                Class<?> spartanItemClass = Class.forName("net.spartanweaponry.item.SpartanWeaponItem");
                if (spartanItemClass.isInstance(item)) {
                    return true;
                }

                Class<?> throwableItemClass = Class.forName("net.spartanweaponry.item.ThrowingWeaponItem");
                if (throwableItemClass.isInstance(item)) {

                    return true;
                }
            } catch (ClassNotFoundException ignored) {}
        }

        return false;
    }

    public static boolean isValidBow(ItemStack stack) {
        Item item = stack.getItem();

        if (item instanceof BowItem ||
            item instanceof CrossbowItem) {
            return true;
        }

        for (ResourceLocation rl : ADDITIONAL_BOW_TAGS) {
            if (stack.is(ItemTags.create(rl))) {
                return true;
            }
        }


        return false;
    }

    public static boolean isValidShield(ItemStack stack) {
        Item item = stack.getItem();

        if (item instanceof ShieldItem) {
            return true;
        }

        for (ResourceLocation rl : ADDITIONAL_SHIELD_TAGS) {
            if (stack.is(ItemTags.create(rl))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidSpawner(Block block) {
        for (ResourceLocation rl : ADDITIONAL_SPAWNER_TAGS) {
            TagKey<Block> tag = BlockTags.create(rl);

            // Check if this block is in the tag
            if (block.defaultBlockState().is(tag)) {
                return true;
            }
        }
        return false;
    }


    public static boolean isValidArmor(ItemStack stack) {
        for (ResourceLocation rl : ADDITIONAL_HELMET_TAGS) {
            if (stack.is(ItemTags.create(rl))) {
                return true;
            }
        }
        for (ResourceLocation rl : ADDITIONAL_CHESTPLATE_TAGS) {
            if (stack.is(ItemTags.create(rl))) {
                return true;
            }
        }
        for (ResourceLocation rl : ADDITIONAL_LEGGINGS_TAGS) {
            if (stack.is(ItemTags.create(rl))) {
                return true;
            }
        }
        for (ResourceLocation rl : ADDITIONAL_BOOTS_TAGS) {
            if (stack.is(ItemTags.create(rl))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidHelmet(ItemStack stack) {
        for (ResourceLocation rl : ADDITIONAL_HELMET_TAGS) {
            if (stack.is(ItemTags.create(rl))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidChestplate(ItemStack stack) {
        for (ResourceLocation rl : ADDITIONAL_CHESTPLATE_TAGS) {
            if (stack.is(ItemTags.create(rl))) {
                return true;
            }
        }
        return false;
    }
    public static boolean isValidLeggings(ItemStack stack) {
        for (ResourceLocation rl : ADDITIONAL_LEGGINGS_TAGS) {
            if (stack.is(ItemTags.create(rl))) {
                return true;
            }
        }
        return false;
    }
    public static boolean isValidBoots(ItemStack stack) {
        for (ResourceLocation rl : ADDITIONAL_BOOTS_TAGS) {
            if (stack.is(ItemTags.create(rl))) {
                return true;
            }
        }
        return false;
    }
}
