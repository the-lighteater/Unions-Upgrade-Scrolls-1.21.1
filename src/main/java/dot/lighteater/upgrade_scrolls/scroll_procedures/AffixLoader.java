package dot.lighteater.upgrade_scrolls.scroll_procedures;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AffixLoader extends SimpleJsonResourceReloadListener {

    public static final Gson GSON = new Gson();
    public static final Map<ResourceLocation, AffixData> AFFIXES = new HashMap<>();

    public AffixLoader() {
        super(GSON, "affixes");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonMap,
                         ResourceManager manager,
                         ProfilerFiller profiler) {

        AFFIXES.clear();

        for (Map.Entry<ResourceLocation, JsonElement> entry : jsonMap.entrySet()) {
            try {
                AffixData affix = GSON.fromJson(entry.getValue(), AffixData.class);
                AFFIXES.put(entry.getKey(), affix);
            } catch (Exception e) {
                System.err.println("Failed to load affix: " + entry.getKey());
            }
        }

        System.out.println("Loaded " + AFFIXES.size() + " affixes.");
    }

    public static Collection<AffixData> getAll() {
        return AFFIXES.values();
    }
}