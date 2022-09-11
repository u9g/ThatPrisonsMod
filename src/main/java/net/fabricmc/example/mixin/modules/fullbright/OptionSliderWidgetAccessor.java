package net.fabricmc.example.mixin.modules.fullbright;

import net.minecraft.client.gui.widget.OptionSliderWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(OptionSliderWidget.class)
public interface OptionSliderWidgetAccessor {
    @Accessor("value")
    void value(float value);
}
