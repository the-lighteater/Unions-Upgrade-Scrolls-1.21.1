package dot.lighteater.upgrade_scrolls.commands;

import com.mojang.brigadier.CommandDispatcher;
import dot.lighteater.upgrade_scrolls.utility.CursedConfigLoader;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import static dot.lighteater.upgrade_scrolls.UpgradeScrolls.MODID;

@EventBusSubscriber(modid = MODID)
public final class ModCommands {

    private ModCommands() {
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        register(event.getDispatcher());
    }

    private static void register(
            CommandDispatcher<CommandSourceStack> dispatcher
    ) {
        dispatcher.register(
                Commands.literal("upgrade_scrolls")
                        .requires(source -> source.hasPermission(2))
                        .then(
                                Commands.literal("reset_cursed_config")
                                        .executes(context ->
                                                resetCursedConfig(
                                                        context.getSource()
                                                )
                                        )
                        )
        );
    }

    private static int resetCursedConfig(CommandSourceStack source) {
        try {
            CursedConfigLoader.resetToDefaults();

            source.sendSuccess(
                    () -> Component.literal(
                            "Cursed config reset to defaults."
                    ),
                    true
            );

            return 1;
        } catch (Exception e) {
            source.sendFailure(
                    Component.literal(
                            "Failed to reset cursed config: "
                                    + e.getMessage()
                    )
            );

            return 0;
        }
    }
}