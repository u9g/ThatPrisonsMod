package dev.u9g.mods.dragonrider;

import dev.u9g.PrisonsModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.player.PlayerEntity;

public class DragonRiderEventHandler {
    public static DragonRiderEventHandler INSTANCE = new DragonRiderEventHandler();

    private final MinecraftClient mc = MinecraftClient.getInstance();
    private RenderRidingPlayer renderer;
    private FakeEnderDragon dragon;

    /**
     * @return should cancel render
     */
    public boolean preRenderPlayer(PlayerEntity player, PlayerEntityRenderer renderer, float tick, double x, double y, double z) {
        if (this.renderer == null) {
            this.renderer = new RenderRidingPlayer(this.mc.getEntityRenderManager(), this.mc.player.getModel().equals("slim"));
        }
        else if (this.dragon != null && player.isMainPlayer()) {
            this.renderer.doRender(player, renderer, tick, x, y, z);
            return true;
        }
        return false;
    }

    public void postRenderPlayer(PlayerEntity player, PlayerEntityRenderer renderer, float tick, double x, double y, double z) {}

    public void postRenderLiving(LivingEntity entity) {
        if (entity.equals(this.dragon)) {
            BossBar.framesToLive = 0;
        }
    }

    /**
     * @return should cancel living update
     */
    public boolean livingUpdate(LivingEntity entity) {
        if (PrisonsModConfig.INSTANCE.misc.rideDragonWhileFlying) {
            if (entity instanceof PlayerEntity) {
                if (this.mc.player.abilities.flying && this.mc.player.getHealth() > 0.0f && !this.mc.player.hasVehicle()) {
                    if (this.dragon == null) {
                        (this.dragon = new FakeEnderDragon(this.mc)).spawn();
                    }
                }
                else if (this.dragon != null) {
                    this.dragon.despawn();
                    this.dragon = null;
                }
            }
        }
        else if (this.dragon != null) {
            this.dragon.despawn();
            this.dragon = null;
        }
        return false;
    }
}
