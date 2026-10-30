
package com.phantomaura;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.LivingEntity;

public final class TargetESP {

    private TargetESP() {}

    public static void register() {
        HudRenderCallback.EVENT.register(
            (context, tickCounter) -> render(context)
        );
    }

    private static void render(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null || !AuraSettings.targetESP) {
            return;
        }

        LivingEntity target = TargetManager.getTarget();

        if (target == null || !target.isAlive()) {
            return;
        }

        double distance = Math.sqrt(
            client.player.squaredDistanceTo(target)
        );

        int x = client.getWindow().getScaledWidth() / 2 + 12;
        int y = client.getWindow().getScaledHeight() / 2 + 12;

        context.fill(x, y, x + 155, y + 55, 0xDD171020);
        context.fill(x, y, x + 155, y + 2, 0xFFAC62FF);

        context.drawText(
            client.textRenderer,
            "PHANTOMAURA",
            x + 7, y + 6, 0xFFAC62FF, false
        );

        context.drawText(
            client.textRenderer,
            "Target: " + target.getName().getString(),
            x + 7, y + 21, 0xFFFFFFFF, false
        );

        context.drawText(
            client.textRenderer,
            String.format("Distance: %.1f blocks", distance),
            x + 7, y + 36, 0xFFD7B8FF, false
        );
    }
}
