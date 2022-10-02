package dev.u9g.mixin.mods.global_hotkeys;

import dev.u9g.PrisonsModConfig;
import dev.u9g.configlib.M;
import dev.u9g.configlib.config.MyModConfigEditor;
import dev.u9g.configlib.config.ScreenElementWrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.OtherClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Box;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(InGameHud.class)
public class InGameHUDMixin {
    private boolean down = false;
    @Inject(method = "render", at = @At("TAIL"))
    private void openScreen(float tickDelta, CallbackInfo ci) {
        if (MinecraftClient.getInstance().currentScreen == null && Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            M.C.openScreen(new ScreenElementWrapper(new MyModConfigEditor(PrisonsModConfig.INSTANCE)));
        } else if (!down && Keyboard.isKeyDown(Keyboard.KEY_Y)) {
            down = true;
            BlockHitResult result = M.C.result;
            if (result.type == BlockHitResult.Type.ENTITY) {
                String name = result.entity.getCustomName();
                if (result.entity instanceof PlayerEntity) {
                    name = result.entity.getTranslationKey();
                    List<Entity> healthNametags = result.entity.world.getEntitiesInBox(ArmorStandEntity.class, Box.createNewBox(
                            result.entity.x, result.entity.y + 2, result.entity.z,
                            result.entity.x + 1, result.entity.y + 2 + 1, result.entity.z + 1),
                            input -> input.getCustomName().endsWith(" §c❤"));
                    if (healthNametags.size() > 0) {
                        name += " has " + healthNametags.get(0).getCustomName().split(" ")[0] + " health";
                    }
                }
                String s = "Entity Name: " + name;
                MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(new LiteralText(s));
                System.out.println(s);
            }
        } else if (down && !Keyboard.isKeyDown(Keyboard.KEY_Y)) {
            down = false;
        }
    }
}
