package dev.u9g.mixin.mods.fullbright;

import dev.u9g.mods.fullbright.FullBright;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionPairWidget;
import net.minecraft.client.gui.widget.OptionSliderWidget;
import net.minecraft.client.options.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OptionPairWidget.class)
public class OptionPairWidgetMixin {
    @Inject(at = @At("TAIL"), method = "createWidget(Lnet/minecraft/client/MinecraftClient;IILnet/minecraft/client/options/GameOptions$Option;)Lnet/minecraft/client/gui/widget/ButtonWidget;")
    void onCreateWidget(MinecraftClient client, int x, int y, GameOptions.Option option, CallbackInfoReturnable<ButtonWidget> cir) {
        if (option != GameOptions.Option.BRIGHTNESS) return;

        FullBright.set_widgets((OptionSliderWidget) cir.getReturnValue(), ((OptionPairWidget)(Object)this));
    }
}
