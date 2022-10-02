package dev.u9g.mixin.mods.dragonrider;

import dev.u9g.mods.dragonrider.DragonRiderEventHandler;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void preTick(CallbackInfo ci) {
        if (DragonRiderEventHandler.INSTANCE.livingUpdate((LivingEntity) (Object) this)) {
            ci.cancel();
        }
    }
}
