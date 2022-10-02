package dev.u9g.mixin.mods.dragonrider;

import dev.u9g.mods.dragonrider.DragonRiderEventHandler;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;DDDFF)V", at = @At("TAIL"))
    public void postRenderLiving(LivingEntity livingEntity, double d, double e, double f, float g, float h, CallbackInfo ci) {
        DragonRiderEventHandler.INSTANCE.postRenderLiving(livingEntity);
    }
}
