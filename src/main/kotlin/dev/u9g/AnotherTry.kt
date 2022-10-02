package dev.u9g

import com.mojang.blaze3d.platform.GlStateManager
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormats
import net.minecraft.util.Formatting
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import org.lwjgl.opengl.GL11

class AnotherTry {
    companion object {
        private fun renderBoundingBox(
            x: Double,
            y: Double,
            z: Double,
            color: CustomColor,
            alphaMult: Float,
            partialTicks: Float
        ) {
            val box = Box(x, y, z, x + 1, y + 1, z + 1);
            GlStateManager.disableDepthTest()
            GlStateManager.disableCull()
            GlStateManager.disableTexture()
            drawFilledBoundingBox(box, 1f, color)
            GlStateManager.enableDepthTest()
            GlStateManager.enableCull()
            GlStateManager.enableTexture()
        }

        private fun drawFilledBoundingBox(box: Box, alpha: Float, color: CustomColor) {
            GlStateManager.enableBlend()
            GlStateManager.blendFuncSeparate(770, 771, 1, 0)
            GlStateManager.disableTexture()
            val tessellator = Tessellator.getInstance()
            val buffer = tessellator.buffer
            GlStateManager.color4f(color.r, color.g, color.b, color.a * alpha)

            //vertical
            buffer.begin(GL11.GL_QUADS, VertexFormats.POSITION)
            buffer.vertex(box.minX, box.minY, box.minZ).next()
            buffer.vertex(box.maxX, box.minY, box.minZ).next()
            buffer.vertex(box.maxX, box.minY, box.maxZ).next()
            buffer.vertex(box.minX, box.minY, box.maxZ).next()
            tessellator.draw()
            buffer.begin(GL11.GL_QUADS, VertexFormats.POSITION)
            buffer.vertex(box.minX, box.maxY, box.maxZ).next()
            buffer.vertex(box.maxX, box.maxY, box.maxZ).next()
            buffer.vertex(box.maxX, box.maxY, box.minZ).next()
            buffer.vertex(box.minX, box.maxY, box.minZ).next()
            tessellator.draw()
            GlStateManager.color4f(
                color.r * 0.8f,
                color.g * 0.8f,
                color.b * 0.8f,
                color.a * alpha
            )

            //x
            buffer.begin(GL11.GL_QUADS, VertexFormats.POSITION)
            buffer.vertex(box.minX, box.minY, box.maxZ).next()
            buffer.vertex(box.minX, box.maxY, box.maxZ).next()
            buffer.vertex(box.minX, box.maxY, box.minZ).next()
            buffer.vertex(box.minX, box.minY, box.minZ).next()
            tessellator.draw()
            buffer.begin(GL11.GL_QUADS, VertexFormats.POSITION)
            buffer.vertex(box.maxX, box.minY, box.minZ).next()
            buffer.vertex(box.maxX, box.maxY, box.minZ).next()
            buffer.vertex(box.maxX, box.maxY, box.maxZ).next()
            buffer.vertex(box.maxX, box.minY, box.maxZ).next()
            tessellator.draw()
            GlStateManager.color4f(
                color.r * 0.9f,
                color.g * 0.9f,
                color.b * 0.9f,
                color.a * alpha
            )
            //z
            buffer.begin(GL11.GL_QUADS, VertexFormats.POSITION)
            buffer.vertex(box.minX, box.maxY, box.minZ).next()
            buffer.vertex(box.maxX, box.maxY, box.minZ).next()
            buffer.vertex(box.maxX, box.minY, box.minZ).next()
            buffer.vertex(box.minX, box.minY, box.minZ).next()
            tessellator.draw()
            buffer.begin(GL11.GL_QUADS, VertexFormats.POSITION)
            buffer.vertex(box.minX, box.minY, box.maxZ).next()
            buffer.vertex(box.maxX, box.minY, box.maxZ).next()
            buffer.vertex(box.maxX, box.maxY, box.maxZ).next()
            buffer.vertex(box.minX, box.maxY, box.maxZ).next()
            tessellator.draw()
        }

        @JvmStatic
        fun outline(x: Double, y: Double, z: Double, color: CustomColor, tickDelta: Float) {
            val viewer = MinecraftClient.getInstance().cameraEntity
            val viewerX: Double = viewer.prevTickX + (viewer.x - viewer.prevTickX) * tickDelta
            val viewerY: Double = viewer.prevTickY + (viewer.y - viewer.prevTickY) * tickDelta
            val viewerZ: Double = viewer.prevTickZ + (viewer.z - viewer.prevTickZ) * tickDelta

            renderBoundingBox(x - viewerX, y - viewerY, z - viewerZ, color, 1f, tickDelta)
        }
    }
}