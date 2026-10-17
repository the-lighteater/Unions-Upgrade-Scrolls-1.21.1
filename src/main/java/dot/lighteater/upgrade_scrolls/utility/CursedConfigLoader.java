package dot.lighteater.upgrade_scrolls.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dot.lighteater.upgrade_scrolls.UpgradeScrolls;
import net.neoforged.fml.loading.FMLPaths;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CursedConfigLoader {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private static final Path CONFIG_PATH =
            FMLPaths.CONFIGDIR.get()
                    .resolve("upgrade_scrolls")
                    .resolve("cursed.json");

    private static final double DEFAULT_CHANCE = 0.8;
    private static final int DEFAULT_MAX_LEVEL = 15;
    private static final double DEFAULT_LEVEL_BONUS = 0.5;
    private static final double DEFAULT_MELEE_BONUS = 0.025;

    private static final List<Integer> DEFAULT_MILESTONES = List.of(
            5, 10, 15
    );

    private static final List<String> DEFAULT_SUCCESS_SOUNDS = List.of(
            "minecraft:item.totem.use",
            "minecraft:entity.villager.work_weaponsmith",
            "minecraft:entity.evoker.cast_spell",
            "minecraft:entity.illusioner.mirror_move"
    );

    private static final List<String> DEFAULT_FAILURE_SOUNDS = List.of(
            "minecraft:entity.evoker.prepare_summon",
            "minecraft:block.anvil.land",
            "minecraft:block.fire.extinguish",
            "minecraft:entity.iron_golem.repair"
    );

    private static final List<String> DEFAULT_MILESTONE_SOUND = List.of(
            "minecraft:entity.ender_dragon.growl"
    );

    private static final List<String> DEFAULT_PERFECTED_SOUNDS = List.of(
            "minecraft:entity.lightning_bolt.impact",
            "minecraft:entity.zombie.converted_to_drowned"
    );

    private static final List<String> DEFAULT_HOLY_PROTECTION_SOUND = List.of(
            "minecraft:block.anvil.hit"
    );

    private static final List<ModifierData> DEFAULT_MODIFIERS = List.of(
            new ModifierData(
                    "barbarian",
                    "minecraft:generic.attack_damage",
                    DEFAULT_MELEE_BONUS,
                    "MULTIPLY_BASE",
                    "WEAPON"
            ),

            new ModifierData(
                    "shield",
                    "minecraft:generic.armor",
                    DEFAULT_LEVEL_BONUS,
                    "ADDITION",
                    "SHIELD"
            ),

            new ModifierData(
                    "head",
                    "minecraft:generic.armor",
                    DEFAULT_LEVEL_BONUS,
                    "ADDITION",
                    "HEAD"
            ),

            new ModifierData(
                    "chest",
                    "minecraft:generic.armor",
                    DEFAULT_LEVEL_BONUS,
                    "ADDITION",
                    "CHEST"
            ),

            new ModifierData(
                    "legs",
                    "minecraft:generic.armor",
                    DEFAULT_LEVEL_BONUS,
                    "ADDITION",
                    "LEGS"
            ),

            new ModifierData(
                    "feet",
                    "minecraft:generic.armor",
                    DEFAULT_LEVEL_BONUS,
                    "ADDITION",
                    "FEET"
            )
    );

    private static double chance = DEFAULT_CHANCE;
    private static double maxLevel = DEFAULT_MAX_LEVEL;
    private static double meleeBonus = DEFAULT_MELEE_BONUS;

    private static List<Integer> milestones = DEFAULT_MILESTONES;

    private static List<ModifierData> modifiers = DEFAULT_MODIFIERS;

    private static List<String> successSounds =
            DEFAULT_SUCCESS_SOUNDS;

    private static List<String> failureSounds =
            DEFAULT_FAILURE_SOUNDS;

    private static List<String> milestoneSound =
            DEFAULT_MILESTONE_SOUND;

    private static List<String> perfectedSounds =
            DEFAULT_PERFECTED_SOUNDS;

    private static List<String> holyProtectionSound =
            DEFAULT_HOLY_PROTECTION_SOUND;

    public static double getChance() {
        return chance;
    }

    public static double getMaxLevel() {
        return maxLevel;
    }

    public static double getMeleeBonus() {
        return meleeBonus;
    }

    public static List<Integer> getMilestones() {
        return milestones;
    }

    public static boolean isMilestone(int index) {
        UpgradeScrolls.LOGGER.debug(milestones + " " + index);
        return milestones.contains(index);
    }

    public static List<String> getSuccessSounds() {
        return successSounds;
    }

    public static List<String> getFailureSounds() {
        return failureSounds;
    }

    public static List<String> getMilestoneSound() {
        return milestoneSound;
    }

    public static List<String> getPerfectedSounds() {
        return perfectedSounds;
    }

    public static List<String> getHolyProtectionSound() {
        return holyProtectionSound;
    }

    public static List<ModifierData> getModifiers() {
        return modifiers;
    }

    public static void load() {

        try {
            Files.createDirectories(
                    CONFIG_PATH.getParent()
            );

            if (!Files.exists(CONFIG_PATH)) {
                createDefault();
            }

            try (Reader reader =
                         Files.newBufferedReader(CONFIG_PATH)) {

                CursedData data =
                        GSON.fromJson(
                                reader,
                                CursedData.class
                        );

                if (data == null) {
                    throw new IOException(
                            "cursed.json was empty"
                    );
                }

                chance = data.chance;
                maxLevel = data.maxLevel;
                meleeBonus = data.meleeBonus;

                milestones = data.milestones;

                if (data.modifiers != null) {
                    modifiers = List.copyOf(data.modifiers);
                }

                if (data.sounds != null) {

                    if (data.sounds.success != null) {
                        successSounds = List.copyOf(
                                data.sounds.success
                        );
                    }

                    if (data.sounds.failure != null) {
                        failureSounds = List.copyOf(
                                data.sounds.failure
                        );
                    }

                    if (data.sounds.milestone != null) {
                        milestoneSound = List.copyOf(
                                data.sounds.milestone
                        );
                    }

                    if (data.sounds.perfected != null) {
                        perfectedSounds = List.copyOf(
                                data.sounds.perfected
                        );
                    }

                    if (data.sounds.holyProtection != null) {
                        holyProtectionSound = List.copyOf(
                                data.sounds.holyProtection
                        );
                    }
                }
            }

        } catch (Exception e) {
            chance = DEFAULT_CHANCE;
            maxLevel = DEFAULT_MAX_LEVEL;
            meleeBonus = DEFAULT_MELEE_BONUS;

            milestones = DEFAULT_MILESTONES;

            successSounds = DEFAULT_SUCCESS_SOUNDS;
            failureSounds = DEFAULT_FAILURE_SOUNDS;
            milestoneSound = DEFAULT_MILESTONE_SOUND;
            perfectedSounds = DEFAULT_PERFECTED_SOUNDS;
            holyProtectionSound = DEFAULT_HOLY_PROTECTION_SOUND;

            modifiers = DEFAULT_MODIFIERS;
        }
    }

    private static void createDefault()
            throws IOException {

        CursedData data =
                createDefaultData();

        try (Writer writer =
                     Files.newBufferedWriter(CONFIG_PATH)) {

            GSON.toJson(
                    data,
                    writer
            );
        }
    }

    private static CursedData createDefaultData() {

        CursedData data =
                new CursedData();

        data.chance = DEFAULT_CHANCE;
        data.maxLevel = DEFAULT_MAX_LEVEL;
        data.meleeBonus = DEFAULT_MELEE_BONUS;

        data.milestones = DEFAULT_MILESTONES;

        data.modifiers = DEFAULT_MODIFIERS;

        data.sounds = new CursedData.Sounds();

        data.sounds.success =
                DEFAULT_SUCCESS_SOUNDS;

        data.sounds.failure =
                DEFAULT_FAILURE_SOUNDS;

        data.sounds.milestone =
                DEFAULT_MILESTONE_SOUND;

        data.sounds.perfected =
                DEFAULT_PERFECTED_SOUNDS;

        data.sounds.holyProtection =
                DEFAULT_HOLY_PROTECTION_SOUND;

        return data;
    }

    public static void resetToDefaults()
            throws IOException {

        Files.createDirectories(CONFIG_PATH.getParent());

        CursedData data = createDefaultData();

        try (Writer writer =
                     Files.newBufferedWriter(
                             CONFIG_PATH,
                             java.nio.file.StandardOpenOption.CREATE,
                             java.nio.file.StandardOpenOption.TRUNCATE_EXISTING,
                             java.nio.file.StandardOpenOption.WRITE
                     )) {

            GSON.toJson(
                    data,
                    writer
            );
        }

        chance = DEFAULT_CHANCE;
        maxLevel = DEFAULT_MAX_LEVEL;
        meleeBonus = DEFAULT_MELEE_BONUS;

        milestones = DEFAULT_MILESTONES;

        modifiers = DEFAULT_MODIFIERS;

        successSounds = DEFAULT_SUCCESS_SOUNDS;
        failureSounds = DEFAULT_FAILURE_SOUNDS;
        milestoneSound = DEFAULT_MILESTONE_SOUND;
        perfectedSounds = DEFAULT_PERFECTED_SOUNDS;
        holyProtectionSound = DEFAULT_HOLY_PROTECTION_SOUND;
    }

    private static class CursedData {
        double chance;
        double maxLevel;
        double meleeBonus;

        List<Integer> milestones;

        Sounds sounds;

        List<ModifierData> modifiers;

        private static class Sounds {
            List<String> success;
            List<String> failure;
            List<String> milestone;
            List<String> perfected;
            List<String> holyProtection;
        }
    }

    public static class ModifierData {

        String streakKey;
        String attributeName;
        double levelBonus;
        String operation;
        String equipment;

        public ModifierData(
                String streakKey,
                String attributeName,
                double levelBonus,
                String operation,
                String equipment
        ) {
            this.streakKey = streakKey;
            this.attributeName = attributeName;
            this.levelBonus = levelBonus;
            this.operation = operation;
            this.equipment = equipment;
        }

        public String getStreakKey() {
            return streakKey;
        }

        public String getAttributeName() {
            return attributeName;
        }

        public double getLevelBonus() {
            return levelBonus;
        }

        public String getOperation() {
            return operation;
        }

        public String getEquipment() {
            return equipment;
        }
    }
}