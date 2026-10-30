
package com.phantomaura;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.MinecraftClient;

import java.nio.file.Files;
import java.nio.file.Path;

public final class ConfigManager {

    private static final Path FILE =
        MinecraftClient.getInstance().runDirectory
            .toPath()
            .resolve("config/phantomaura.json");

    private ConfigManager() {}

    public static void save() {
        try {
            Files.createDirectories(FILE.getParent());

            JsonObject json = new JsonObject();

            json.addProperty("autoTarget", AuraSettings.autoTarget);
            json.addProperty("targetESP", AuraSettings.targetESP);
            json.addProperty("hostileMobs", AuraSettings.hostileMobs);
            json.addProperty("passiveMobs", AuraSettings.passiveMobs);
            json.addProperty("players", AuraSettings.players);
            json.addProperty("range", AuraSettings.range);
            json.addProperty("aimSpeed", AuraSettings.aimSpeed);

            Files.writeString(FILE, json.toString());

        } catch (Exception e) {
            System.err.println("PhantomAura: Config save failed");
            e.printStackTrace();
        }
    }

    public static void load() {
        try {
            if (!Files.exists(FILE)) return;

            JsonObject json = JsonParser.parseString(
                Files.readString(FILE)
            ).getAsJsonObject();

            AuraSettings.autoTarget =
                json.has("autoTarget") &&
                json.get("autoTarget").getAsBoolean();

            AuraSettings.targetESP =
                json.has("targetESP") &&
                json.get("targetESP").getAsBoolean();

            AuraSettings.hostileMobs =
                json.has("hostileMobs") &&
                json.get("hostileMobs").getAsBoolean();

            AuraSettings.passiveMobs =
                json.has("passiveMobs") &&
                json.get("passiveMobs").getAsBoolean();

            AuraSettings.players =
                json.has("players") &&
                json.get("players").getAsBoolean();

            if (json.has("range"))
                AuraSettings.range = json.get("range").getAsDouble();

            if (json.has("aimSpeed"))
                AuraSettings.aimSpeed = json.get("aimSpeed").getAsDouble();

        } catch (Exception e) {
            System.err.println("PhantomAura: Config load failed");
            e.printStackTrace();
        }
    }
}
