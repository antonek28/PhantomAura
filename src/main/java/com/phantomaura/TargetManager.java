
package com.phantomaura;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;

public final class TargetManager {

    private TargetManager() {}

    private static LivingEntity target;

    public static LivingEntity getTarget() {
        return target;
    }

    public static void update() {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null ||
            client.world == null ||
            !AuraSettings.autoTarget) {
            target = null;
            return;
        }

        target = client.world.getEntitiesByClass(
            LivingEntity.class,
            client.player.getBoundingBox()
                .expand(AuraSettings.range),
            TargetManager::isValid
        ).stream()
        .min((a, b) -> Double.compare(
            client.player.squaredDistanceTo(a),
            client.player.squaredDistanceTo(b)
        ))
        .orElse(null);
    }

    private static boolean isValid(LivingEntity entity) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (!entity.isAlive() ||
            entity == client.player ||
            entity.isRemoved()) {
            return false;
        }

        if (entity instanceof PlayerEntity) {
            return AuraSettings.players;
        }

        if (entity instanceof HostileEntity) {
            return AuraSettings.hostileMobs;
        }

        if (entity instanceof PassiveEntity) {
            return AuraSettings.passiveMobs;
        }

        return false;
    }
}
