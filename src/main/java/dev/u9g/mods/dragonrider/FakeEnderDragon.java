package dev.u9g.mods.dragonrider;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class FakeEnderDragon extends EnderDragonEntity {
    private final PlayerEntity player;
    private final SoundManager soundManager;

    public FakeEnderDragon(final MinecraftClient mc) {
        super(mc.world);
        super.field_3745 = true;
        this.player = mc.player;
        this.soundManager = mc.getSoundManager();
        this.update();
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        this.update();
    }

    private void update() {
        super.x = this.player.x;
        super.y = this.player.y - 2.5;
        super.z = this.player.z;
        super.yaw = this.player.yaw + 180.0f;
        super.pitch = this.player.pitch;
    }

    public void spawn() {
        ((ClientWorld)super.world).addEntity(super.getEntityId(), this);
        final PositionedSoundInstance growl = new PositionedSoundInstance(new Identifier("mob.enderdragon.growl"),
                super.getSoundVolume(), super.getSoundPitch(), (float)super.x, (float)super.y, (float)super.z);
        this.soundManager.play(growl);
    }

    public void despawn() {
        ((ClientWorld)super.world).removeEntity(super.getEntityId());
    }
}
