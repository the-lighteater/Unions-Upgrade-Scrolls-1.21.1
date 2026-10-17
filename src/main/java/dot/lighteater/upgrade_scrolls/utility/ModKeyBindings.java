package dot.lighteater.upgrade_scrolls.utility;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(value = Dist.CLIENT)
public class ModKeyBindings {

    public static final String CATEGORY =
            "key.categories.upgrade_scrolls";

    public static final KeyMapping OPEN_SCROLLS =
            new KeyMapping(
                    "key.upgrade_scrolls.open_scroll_application_menu",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_B,
                    CATEGORY
            );

    @SubscribeEvent
    public static void register(RegisterKeyMappingsEvent event) {
        event.register(OPEN_SCROLLS);
    }
}