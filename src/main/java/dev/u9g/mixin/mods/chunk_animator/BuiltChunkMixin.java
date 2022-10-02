package dev.u9g.mixin.mods.chunk_animator;

import dev.u9g.mods.chunk_animator.AnimationHandler;
import net.minecraft.client.world.BuiltChunk;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuiltChunk.class)
public class BuiltChunkMixin {
    @Inject(method = "method_10160(Lnet/minecraft/util/math/BlockPos;)V", at = @At("HEAD")) // setPosition
    public void setPosition(BlockPos blockPos, CallbackInfo ci) {
        AnimationHandler.setPosition((BuiltChunk) (Object) this, blockPos);
    }
}
