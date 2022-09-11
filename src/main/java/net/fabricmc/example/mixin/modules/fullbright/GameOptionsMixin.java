package net.fabricmc.example.mixin.modules.fullbright;

import net.fabricmc.example.modules.fullbright.FullBright;
import net.minecraft.client.options.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameOptions.class)
public abstract class GameOptionsMixin {
    @Inject(at = @At("HEAD"), method = "getValueMessage(Lnet/minecraft/client/options/GameOptions$Option;)Ljava/lang/String;", cancellable = true)
    private void valueMessage(GameOptions.Option option, CallbackInfoReturnable<String> cir) {
        if (option == GameOptions.Option.BRIGHTNESS) {
            if (!FullBright.is_fullbright_enabled()) return;
            cir.setReturnValue(FullBright.fullbright_slider_string());
            cir.cancel();
        }
    }
}
