package dev.u9g.mixin;

import dev.u9g.PrisonsModConfig;
import dev.u9g.configlib.M;
import dev.u9g.configlib.config.MyModConfigEditor;
import dev.u9g.configlib.config.ScreenElementWrapper;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
    @Inject(at = @At("HEAD"), method = "switchToRealms()V", cancellable = true)
    private void valueMessage(CallbackInfo ci) {
        ci.cancel();
        M.C.openScreen(new ScreenElementWrapper(new MyModConfigEditor(PrisonsModConfig.INSTANCE)));
//        MinecraftClient.getInstance().openScreen(new SettingsScreen());
    }
}
