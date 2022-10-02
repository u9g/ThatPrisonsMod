package dev.u9g.mods;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormatElement;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Optional;

public class SignReader {
    public static SignReader INSTANCE = new SignReader();
    private static final Identifier SIGN_TEXTURE = new Identifier("textures/entity/sign.png");

    private List<int[]> quadOrder = Lists.newArrayList(new int[]{2,0}, new int[]{2,2}, new int[]{0,2}, new int[]{0,0});

    public void render() {
        // size: w:96, h:48
        MinecraftClient.getInstance().getTextureManager().bindTexture(SIGN_TEXTURE);
        int xScale = 4;
        int yScale = 8;
        int x = 2 * xScale;
        int y = 2 * yScale;
        int width = 24 * xScale;
        int height = 12 * yScale;
        Tessellator t = Tessellator.getInstance();
        BufferBuilder b = t.getBuffer();

        VertexFormat format = new VertexFormat().addElement(VertexFormats.POSITION_ELEMENT).addElement(VertexFormats.TEXTURE_FLOAT_ELEMENT);

        b.begin(GL11.GL_QUADS, format);

        for (int[] vertex : quadOrder) {
            for (VertexFormatElement element : b.getFormat().getElements()) {
                switch (element.getType()) {
                    case POSITION: {
                        int tempX = x + width * vertex[1] / 2;
                        int tempY = y + height * vertex[0] / 2;
                        b.vertex(tempX, tempY, 0);
                        break;
                    }
                    case UV: {
                        int tempX = x + width * vertex[1] / 2;
                        int tempY = y + height * vertex[0] / 2;
                        b.texture(tempX * 1f / 256f, tempY * 1f / 256f);
                        break;
                    }
                    default: {
                        System.out.println("type: " + element.getType());
                        throw new IllegalStateException("How did we get here?");
                    }
                }
            }
        }
        t.draw();
    }

    /**
     * Finds the sign directly in the player's line of sight.
     *
     * @return The sign the player is looking at or {@code null} if the player
     * is not looking at a sign.
     */
    private @Nullable SignBlockEntity getSign(MinecraftClient mc) {
        // Sanity check, but can continue normally if null
        if (mc == null || mc.world == null) {
            return null;
        }

        // Functional approach avoids long null check chain
        return Optional.ofNullable(mc.getCameraEntity())
                .map(entity -> entity.rayTrace(200, 1.0f))
                .map(BlockHitResult::getBlockPos)
                .map(mc.world::getBlockEntity)
                .filter(SignBlockEntity.class::isInstance)
                .map(SignBlockEntity.class::cast)
                .orElse(null);
    }
}
