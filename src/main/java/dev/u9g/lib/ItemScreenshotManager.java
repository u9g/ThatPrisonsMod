/*
 *  * Copyright © Wynntils - 2018 - 2022.
 */
package dev.u9g.lib;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.u9g.InventoryHelper;
import dev.u9g.gl.RenderHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.TextureUtil;
import net.minecraft.inventory.slot.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemScreenshotManager {

    public static void takeScreenshot() {
        Screen gui = MinecraftClient.getInstance().currentScreen;
        if (!(gui instanceof HandledScreen)) return;

        System.out.println("screen: " + gui);

        Slot slot = InventoryHelper.hoveredSlot((HandledScreen) gui);
        if (slot == null || !InventoryHelper.getHasStack(slot)) return;
        ItemStack stack = slot.getStack();
        if (!stack.hasCustomName()) return;

        List<String> tooltip = stack.getTooltip(MinecraftClient.getInstance().player, false);
        removeItemLore(tooltip);

        TextRenderer fr = MinecraftClient.getInstance().textRenderer;
        int width = 0;
        int height = 16;

        // calculate width of tooltip
        for (String s : tooltip) {
            int w = fr.getStringWidth(s);
            if (w > width) width = w;
        }
        width += 8;

        // calculate height of tooltip
        if (tooltip.size() > 1) height += 2 + (tooltip.size() - 1) * 10;

        // account for text wrapping
        if (width > gui.width/2 + 8) {
            int wrappedWidth = 0;
            int wrappedLines = 0;
            for (String s : tooltip) {
                List<String> wrappedLine = fr.wrapLines(s, gui.width/2);
                for (String ws : wrappedLine) {
                    wrappedLines++;
                    int w = fr.getStringWidth(ws);
                    if (w > wrappedWidth) wrappedWidth = w;
                }
            }
            width = wrappedWidth + 8;
            height = 16 + (2 + (wrappedLines - 1) * 10);
        }

        // calculate scale of tooltip to fit it to the framebuffer
        float scaleh = (float) gui.height/height;
        float scalew = (float) gui.width/width;

        // draw tooltip to framebuffer, create image from it
        GlStateManager.pushMatrix();
        Framebuffer fb = new Framebuffer((int) (gui.width*(1/scalew)*2), (int) (gui.height*(1/scaleh)*2), true);
        fb.bind(false);
        GlStateManager.scalef(scalew, scaleh, 1);
        drawTooltip(tooltip, gui.width/2, fr);
        BufferedImage bi = createScreenshot(width*2, height*2);
        fb.endWrite();
        GlStateManager.popMatrix();

        // copy to clipboard
        ClipboardImage ci = new ClipboardImage(bi);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ci, null);

        MinecraftClient.getInstance().player.sendMessage(new LiteralText(Formatting.GREEN + "Copied " + stack.getName() + Formatting.GREEN + " to the clipboard!"));

        LiteralText msg = new LiteralText(Formatting.DARK_GREEN + "" + Formatting.UNDERLINE + "A screenshot of the item has been copied!");
//        String encoded = ChatItemManager.encodeItem(stack);
//        if (encoded != null) { // item was valid, show copy message
//            LiteralText msg = new LiteralText(Formatting.DARK_GREEN + "" + Formatting.UNDERLINE + "Click here to copy the item for chat!");
//            msg.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString(TextFormatting.DARK_AQUA + "Paste this text in chat to display your item to other Wynntils users.")));
//            msg = TextAction.withDynamicEvent(msg, () -> {
//                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(encoded), null);
//                McIf.mc().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.BLOCK_NOTE_PLING, 1f)); // confirmation pling
//            });
//
//            McIf.player().sendMessage(msg);
//        }
    }

    private static void removeItemLore(List<String> tooltip) {
//        // iterate through each line of the tooltip and remove item lore
//        List<String> temp = new ArrayList<>();
//        boolean lore = false;
//        for (String s : tooltip) {
//            // only remove text after the item type indicator
//            Matcher m = ITEM_PATTERN.matcher(Formatting.strip(s));
//            if (!lore && m.matches()) lore = true;
//
//            if (lore && s.contains("" + Formatting.DARK_GRAY)) temp.add(s);
//        }
//        tooltip.removeAll(temp);
    }

    private static void drawTooltip(List<String> textLines, int maxTextWidth, TextRenderer font) {
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepthTest();
        GlStateManager.color4f(1f, 1f, 1f, 1f);
        int tooltipTextWidth = 0;

        for (String textLine : textLines) {
            int textLineWidth = font.getStringWidth(textLine);

            if (textLineWidth > tooltipTextWidth) {
                tooltipTextWidth = textLineWidth;
            }
        }

        boolean needsWrap = false;

        int titleLinesCount = 1;

        if (maxTextWidth > 0 && tooltipTextWidth > maxTextWidth) {
            tooltipTextWidth = maxTextWidth;
            needsWrap = true;
        }

        if (needsWrap) {
            int wrappedTooltipWidth = 0;
            List<String> wrappedTextLines = new ArrayList<String>();
            for (int i = 0; i < textLines.size(); i++) {
                String textLine = textLines.get(i);
                List<String> wrappedLine = font.wrapLines(textLine, tooltipTextWidth);
                if (i == 0)
                    titleLinesCount = wrappedLine.size();

                for (String line : wrappedLine) {
                    int lineWidth = font.getStringWidth(line);
                    if (lineWidth > wrappedTooltipWidth)
                        wrappedTooltipWidth = lineWidth;

                    wrappedTextLines.add(line);
                }
            }
            tooltipTextWidth = wrappedTooltipWidth;
            textLines = wrappedTextLines;
        }

        int tooltipHeight = 8;
        if (textLines.size() > 1) {
            tooltipHeight += (textLines.size() - 1) * 10;
            if (textLines.size() > titleLinesCount)
                tooltipHeight += 2; // gap between title lines and next lines
        }

        final int zLevel = 300;
        int tooltipX = 4;
        int tooltipY = 4;
        int backgroundColor = 0xF0100010;
        int borderColorStart = 0x505000FF;
        int borderColorEnd = (borderColorStart & 0xFEFEFE) >> 1 | borderColorStart & 0xFF000000;
        drawGradientRect(zLevel, tooltipX - 3, tooltipY - 4, tooltipX + tooltipTextWidth + 3, tooltipY - 3, backgroundColor, backgroundColor);
        drawGradientRect(zLevel, tooltipX - 3, tooltipY + tooltipHeight + 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 4, backgroundColor, backgroundColor);
        drawGradientRect(zLevel, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
        drawGradientRect(zLevel, tooltipX - 4, tooltipY - 3, tooltipX - 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
        drawGradientRect(zLevel, tooltipX + tooltipTextWidth + 3, tooltipY - 3, tooltipX + tooltipTextWidth + 4, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
        drawGradientRect(zLevel, tooltipX - 3, tooltipY - 3 + 1, tooltipX - 3 + 1, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
        drawGradientRect(zLevel, tooltipX + tooltipTextWidth + 2, tooltipY - 3 + 1, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
        drawGradientRect(zLevel, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY - 3 + 1, borderColorStart, borderColorStart);
        drawGradientRect(zLevel, tooltipX - 3, tooltipY + tooltipHeight + 2, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, borderColorEnd, borderColorEnd);

        for (int lineNumber = 0; lineNumber < textLines.size(); lineNumber++) {
            String line = textLines.get(lineNumber);
            font.drawWithShadow(line, (float) tooltipX, (float) tooltipY, -1);

            if (lineNumber + 1 == titleLinesCount)
                tooltipY += 2;

            tooltipY += 10;
        }

        GlStateManager.enableLighting();
        GlStateManager.enableDepthTest();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableRescaleNormal();
    }

    private static void drawGradientRect(int zLevel, int left, int top, int right, int bottom, int startColor, int endColor) {
        float startAlpha = (float)(startColor >> 24 & 255) / 255.0F;
        float startRed   = (float)(startColor >> 16 & 255) / 255.0F;
        float startGreen = (float)(startColor >>  8 & 255) / 255.0F;
        float startBlue  = (float)(startColor       & 255) / 255.0F;
        float endAlpha   = (float)(endColor   >> 24 & 255) / 255.0F;
        float endRed     = (float)(endColor   >> 16 & 255) / 255.0F;
        float endGreen   = (float)(endColor   >>  8 & 255) / 255.0F;
        float endBlue    = (float)(endColor         & 255) / 255.0F;

        GlStateManager.disableTexture();
        GlStateManager.enableBlend();
        GlStateManager.disableAlphaTest();
        GlStateManager.blendFuncSeparate(11, 9, 11, 9);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, VertexFormats.POSITION_COLOR);
        buffer.vertex(right,    top, zLevel).color(startRed, startGreen, startBlue, startAlpha).next();
        buffer.vertex( left,    top, zLevel).color(startRed, startGreen, startBlue, startAlpha).next();
        buffer.vertex( left, bottom, zLevel).color(  endRed,   endGreen,   endBlue,   endAlpha).next();
        buffer.vertex(right, bottom, zLevel).color(  endRed,   endGreen,   endBlue,   endAlpha).next();
        tessellator.draw();

        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlphaTest();
        GlStateManager.enableTexture();
    }

    private static BufferedImage createScreenshot(int width, int height) {
        // create pixel arrays
        int i = width * height;
        IntBuffer pixelBuffer = BufferUtils.createIntBuffer(i);
        int[] pixelValues = new int[i];

        GL11.glPixelStorei(3333, 1);
        GL11.glPixelStorei(3317, 1);
        pixelBuffer.clear();

        // create image from pixels
        GL11.glReadPixels(0, 0, width, height, 32993, 33639, pixelBuffer);
        pixelBuffer.get(pixelValues);
        TextureUtil.flipXY(pixelValues, width, height);
        BufferedImage bufferedimage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        bufferedimage.setRGB(0, 0, width, height, pixelValues, 0, width);
        return bufferedimage;
    }

    private static class ClipboardImage implements Transferable {

        Image image;

        public ClipboardImage(Image image) {
            this.image = image;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] { DataFlavor.imageFlavor };
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return DataFlavor.imageFlavor.equals(flavor);
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            if (!DataFlavor.imageFlavor.equals(flavor)) throw new UnsupportedFlavorException(flavor);
            return this.image;
        }

    }

}