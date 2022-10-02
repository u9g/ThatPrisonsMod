package dev.u9g.mixin.mods;

import dev.u9g.PrisonsModConfig;
import dev.u9g.mods.SignReader;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method = "render", at = @At("TAIL"))
    private void setChildState(float tickDelta, CallbackInfo ci) {
        if (PrisonsModConfig.INSTANCE.misc.signReader) {
            SignReader.INSTANCE.render();
        }
    }
}
