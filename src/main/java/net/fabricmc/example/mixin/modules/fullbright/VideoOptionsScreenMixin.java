package net.fabricmc.example.mixin.modules.fullbright;

import net.fabricmc.example.modules.fullbright.FullBright;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.VideoOptionsScreen;
import net.minecraft.client.gui.widget.EntryListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VideoOptionsScreen.class)
public class VideoOptionsScreenMixin {
    @Shadow private EntryListWidget list;

    @Inject(at = @At("TAIL"), method = "render(IIF)V")
    private void render(int mouseX, int mouseY, float tickDelta, CallbackInfo ci) {
        FullBright.align_button();
        FullBright.get_button().render(MinecraftClient.getInstance(), mouseX, mouseY);
    }

    @Inject(at = @At("TAIL"), method = "mouseReleased(III)V")
    private void mouseReleased(int mouseX, int mouseY, int button, CallbackInfo ci) {
        if (FullBright.get_button().isMouseOver(MinecraftClient.getInstance(), mouseX, mouseY)) {
            FullBright.get_button().mouseReleased(mouseX, mouseY);
        }
    }
}
