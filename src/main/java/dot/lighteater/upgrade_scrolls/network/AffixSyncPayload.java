
package dot.lighteater.upgrade_scrolls.network;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dot.lighteater.upgrade_scrolls.UpgradeScrolls;
import dot.lighteater.upgrade_scrolls.affix.AffixData;
import dot.lighteater.upgrade_scrolls.affix.AffixLoader;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.MinecraftServer;

import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public record AffixSyncPayload(String json) implements CustomPacketPayload {

    public static final Type<AffixSyncPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(
                    UpgradeScrolls.MODID, "affix_sync"
            ));

    public static final StreamCodec<RegistryFriendlyByteBuf, AffixSyncPayload>
            STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            AffixSyncPayload::json,
            AffixSyncPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    /**
     * Serializes the server's currently loaded affixes.
     */
    public static String createJson() {
        JsonObject root = new JsonObject();

        AffixLoader.AFFIXES.forEach((id, affix) ->
                root.add(id.toString(), AffixLoader.GSON.toJsonTree(affix))
        );

        return root.toString();
    }

    /**
     * Sends the current affix definitions to one player.
     */
    public static void sendTo(ServerPlayer player) {
        PacketDistributor.sendToPlayer(
                player,
                new AffixSyncPayload(createJson())
        );
    }

    /**
     * Sends the current definitions to every connected player.
     */
    public static void syncToClients() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();

        if (server == null) {
            return;
        }

        server.execute(() -> {
            String json = createJson();

            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                PacketDistributor.sendToPlayer(
                        player,
                        new AffixSyncPayload(json)
                );
            }
        });
    }

    /**
     * Called when the client receives the packet.
     */
    public static void handle(
            AffixSyncPayload payload,
            IPayloadContext context
    ) {
        context.enqueueWork(() -> {
            try {
                JsonObject root = JsonParser.parseString(payload.json())
                        .getAsJsonObject();

                AffixLoader.AFFIXES.clear();

                root.entrySet().forEach(entry -> {
                    ResourceLocation id =
                            ResourceLocation.tryParse(entry.getKey());

                    if (id == null) {
                        UpgradeScrolls.LOGGER.warn(
                                "[AffixSync] Invalid affix ID: {}",
                                entry.getKey()
                        );
                        return;
                    }

                    AffixData affix = AffixLoader.GSON.fromJson(
                            entry.getValue(),
                            AffixData.class
                    );

                    if (affix != null) {
                        AffixLoader.AFFIXES.put(id, affix);
                    }
                });

                UpgradeScrolls.LOGGER.info(
                        "[AffixSync] Received {} affixes from server",
                        AffixLoader.AFFIXES.size()
                );
            } catch (Exception e) {
                UpgradeScrolls.LOGGER.error(
                        "[AffixSync] Failed to deserialize affixes",
                        e
                );
            }
        });
    }
}
