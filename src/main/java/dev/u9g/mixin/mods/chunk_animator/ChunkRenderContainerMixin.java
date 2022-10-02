package dev.u9g.mixin.mods.chunk_animator;

import dev.u9g.mods.chunk_animator.AnimationHandler;
import net.minecraft.client.render.world.AbstractChunkRenderManager;
import net.minecraft.client.world.BuiltChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractChunkRenderManager.class)
public class ChunkRenderContainerMixin {
    @Inject(
            method = "method_9770(Lnet/minecraft/client/world/BuiltChunk;)V", // preRender
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/platform/GlStateManager;translatef(FFF)V",
                    shift = At.Shift.BEFORE
            )
    )
    public void onPreRenderChunk(BuiltChunk chunk, CallbackInfo ci) {
        AnimationHandler.preRender(chunk);
    }
}
