package dev.u9g.mixin.mods.dragonrider;

import dev.u9g.mods.dragonrider.DragonRiderEventHandler;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public class RenderPlayerMixin {
    @Inject(method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;DDDFF)V", at = @At("HEAD"), cancellable = true)
    public void pre(AbstractClientPlayerEntity abstractClientPlayerEntity, double d, double e, double f, float g, float h, CallbackInfo ci) {
        if (DragonRiderEventHandler.INSTANCE.preRenderPlayer(abstractClientPlayerEntity, (PlayerEntityRenderer) (Object) this, h, d, e, f)) {
            ci.cancel();
        }
    }

    @Inject(method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;DDDFF)V", at = @At("TAIL"))
    public void post(AbstractClientPlayerEntity abstractClientPlayerEntity, double d, double e, double f, float g, float h, CallbackInfo ci) {
        DragonRiderEventHandler.INSTANCE.postRenderPlayer(abstractClientPlayerEntity, (PlayerEntityRenderer) (Object) this, h, d, e, f);
    }
}
