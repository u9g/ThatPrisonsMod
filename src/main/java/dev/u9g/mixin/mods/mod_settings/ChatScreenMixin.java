package dev.u9g.mixin.mods.mod_settings;

import dev.u9g.PrisonsModConfig;
import dev.u9g.configlib.M;
import dev.u9g.configlib.config.MyModConfigEditor;
import dev.u9g.configlib.config.ScreenElementWrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {
    @Shadow protected TextFieldWidget chatField;

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    public void sendMessage(char character, int code, CallbackInfo ci) {
        String input = this.chatField.getText().trim();
        if (input.equalsIgnoreCase("/pr")) {
            ci.cancel();
            M.C.openScreen(new ScreenElementWrapper(new MyModConfigEditor(PrisonsModConfig.INSTANCE)));
        }
    }
}
