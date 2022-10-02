package dev.u9g.lib;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import dev.u9g.configlib.M;
import dev.u9g.gl.RenderHelper;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.LivingEntity;

//public class PlayerRenderer {
//    public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, LivingEntity ent) {
//        GlStateManager.enableColorMaterial();
//        GlStateManager.pushMatrix();
//        GlStateManager.translatef((float) posX, (float) posY, 50.0F);
//        GlStateManager.scalef((float) (-scale), (float) scale, (float) scale);
//        GlStateManager.rotatef(180.0F, 0.0F, 0.0F, 1.0F);
//        float renderYawOffset = ent.bodyYaw;
//        float f1 = ent.yaw;
//        float f2 = ent.pitch;
//        float f3 = ent.prevHeadYaw;
//        float f4 = ent.headYaw;
//        GlStateManager.rotatef(135.0F, 0.0F, 1.0F, 0.0F);
//        RenderHelper.enableStandardItemLighting();
//        GlStateManager.rotatef(-135.0F, 0.0F, 1.0F, 0.0F);
//        GlStateManager.rotatef(25, 1.0F, 0.0F, 0.0F);
//        ent.bodyYaw = (float) Math.atan(mouseX / 40.0F) * 20.0F;
//        ent.yaw = (float) Math.atan(mouseX / 40.0F) * 40.0F;
//        ent.pitch = -((float) Math.atan(mouseY / 40.0F)) * 20.0F;
//        ent.headYaw = ent.yaw;
//        ent.prevHeadYaw = ent.yaw;
//        EntityRenderDispatcher rendermanager = M.C.getEntityRenderManager();
//        rendermanager.setYaw(180.0F);
//        rendermanager.setRenderShadows(false);
//        rendermanager.render(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
//
//        ent.bodyYaw = renderYawOffset;
//        ent.yaw = f1;
//        ent.pitch = f2;
//        ent.prevHeadYaw = f3;
//        ent.headYaw = f4;
//        GlStateManager.popMatrix();
//        RenderHelper.disableStandardItemLighting();
//        GlStateManager.disableRescaleNormal();
//        GlStateManager.activeTexture(GLX.lightmapTextureUnit);
//        GlStateManager.disableTexture();
//        GlStateManager.activeTexture(GLX.textureUnit);
//    }
//}
