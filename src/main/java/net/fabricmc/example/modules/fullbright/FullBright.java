package net.fabricmc.example.modules.fullbright;

import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.example.mixin.modules.fullbright.ButtonWidgetAccessor;
import net.fabricmc.example.mixin.modules.fullbright.OptionSliderWidgetAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionPairWidget;
import net.minecraft.client.gui.widget.OptionSliderWidget;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.util.Identifier;

public class FullBright {
    private static FullBright.FullBrightButton widget = null;
    private static OptionSliderWidget active_brightness_slider = null;
    private static OptionPairWidget option_pair = null;

    public static FullBright.FullBrightButton get_button() {
        return widget;
    }

    public static void set_widgets(OptionSliderWidget active_brightness_slider, OptionPairWidget option_pair) {
        if (FullBright.active_brightness_slider == active_brightness_slider) return;
        FullBright.active_brightness_slider = active_brightness_slider;
        FullBright.option_pair = option_pair;
        FullBright.widget = new FullBright.FullBrightButton();
    }

    public static String fullbright_slider_string() {
        return I18n.translate(GameOptions.Option.BRIGHTNESS.getName()) + ": " + I18n.translate("options.gamma.fullbright");
    }

    public static void align_button() {
        FullBright.widget.x = FullBright.active_brightness_slider.x + FullBright.active_brightness_slider.getWidth() +
                (FullBright.is_showing_scroll_bar() ? 8 : 0);
        FullBright.widget.y = FullBright.active_brightness_slider.y;
    }
    
    private static boolean is_showing_scroll_bar() {
        return option_pair.getMaxScroll() > 0;
    }

    public static boolean is_fullbright_enabled() {
        return MinecraftClient.getInstance().options.gamma == 10000.0;
    }

    public static class FullBrightButton extends ButtonWidget {
        private static final Identifier litBtns = new Identifier("that_prisons_mod", "textures/gui/lit_btns.png");

        public FullBrightButton() {
            super(998877, 0, 0, 20, 20, "");
        }

        @Override
        public void render(MinecraftClient client, int mouseX, int mouseY) {
            if (!this.visible) {
                return;
            }

            boolean hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            client.getTextureManager().bindTexture(litBtns);
            GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
            /*topLeftX, topLeftY, U(offsetX), V(offsetY), widthToDraw, heightToDraw, imageWidth, imageHeight*/
            drawTexture(this.x, this.y, FullBright.is_fullbright_enabled() ? 20 : 0, hovered ? 20 : 0, this.width, this.height, this.width*2, this.height*2);
        }

        @Override
        public void mouseReleased(int mouseX, int mouseY) {
            MinecraftClient.getInstance().options.gamma = 10000;
            MinecraftClient.getInstance().options.save();
            ((OptionSliderWidgetAccessor) FullBright.active_brightness_slider).value(1);
            ((ButtonWidgetAccessor) FullBright.active_brightness_slider).message(FullBright.fullbright_slider_string());
        }
    }
}
