package dev.u9g.mods.dragonrider;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.UseAction;

public class RenderRidingPlayer extends PlayerEntityRenderer {
    public RenderRidingPlayer(final EntityRenderDispatcher renderManager, final boolean flag) {
        super(renderManager, flag);
    }
    
    public void doRender(PlayerEntity player, PlayerEntityRenderer renderer, float tick, double x, double y, double z) {
        this.setModelVisibilities((AbstractClientPlayerEntity) player);
        this.doRender((AbstractClientPlayerEntity) player, x, y, z, player.yaw, tick);
    }
    
    public void doRender(final AbstractClientPlayerEntity player, final double x, final double y, final double z, final float yaw, final float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        final PlayerEntityModel model = super.getModel();
        model.handSwingProgress = super.method_5787/*getHandSwingProgress*/(player, partialTicks);
        model.riding = true;
        model.child = false;
        try {
            final float f = super.method_5769/*???*/(player.prevBodyYaw, player.bodyYaw, partialTicks);
            final float f2 = super.method_5769/*???*/(player.prevHeadYaw, player.headYaw, partialTicks);
            final float f3 = 0.0625f;
            final float f4 = player.prevPitch + (player.pitch - player.prevPitch) * partialTicks;
            final float f5 = f2 - f;
            super.method_5772/*renderLivingAt*/(player, x, y, z);
            final float f6 = super.method_5783/*handleRotationFloat*/(player, partialTicks);
            super.method_5777/*rotateCorpse*/(player, f6, f, partialTicks);
            GlStateManager.enableRescaleNormal();
            GlStateManager.scalef(-1.0f, -1.0f, 1.0f);
            super.scale(player, partialTicks);
            GlStateManager.translatef(0.0f, -1.5078125f, 0.0f);
            float f7 = player./*prevLimbSwingAmount*/field_6748 + (player./*limbSwingAmount*/field_6749 - player./*prevLimbSwingAmount*/field_6748) * partialTicks;
            final float f8 = player./*limbSwing*/field_6750 - player./*limbSwingAmount*/field_6749 * (1.0f - partialTicks);
            if (f7 > 1.0f) {
                f7 = 1.0f;
            }
            GlStateManager.enableAlphaTest();
            model.animateModel(player, f8, f7, partialTicks);
            model.setAngles(f8, f7, f6, f5, f4, 0.0625f, player);
            if (super.field_11124/*???*/) {
                final boolean flag = super.method_10257/*???*/(player);
                super.renderModel(player, 0.0f, 0.0f, f6, f5, f4, 0.0625f);
                if (flag) {
                    super.method_10259/*???*/();
                }
            }
            else {
                final boolean flag = super.method_10258/*???*/(player, partialTicks);
                super.renderModel(player, 0.0f, 0.0f, f6, f5, f4, 0.0625f);
                if (flag) {
                    super.method_10260/*???*/();
                }
                GlStateManager.depthMask(true);
                super.renderFeatures(player, f8, f7, partialTicks, f6, f5, f4, 0.0625f);
            }
            GlStateManager.disableRescaleNormal();
        }
        catch (final Exception exception) {
            exception.printStackTrace();
        }
        GlStateManager.activeTexture(GLX.lightmapTextureUnit);
        GlStateManager.enableTexture();
        GlStateManager.activeTexture(GLX.textureUnit);
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }
    
    protected Identifier getEntityTexture(final AbstractClientPlayerEntity player) {
        return player.getSkinTexture();
    }
    
    private void setModelVisibilities(final AbstractClientPlayerEntity player) {
        final PlayerEntityModel model = super.getModel();
        final ItemStack heldItem = player.inventory.getMainHandStack();
        model.setVisible(true);
        model.hat.visible = player.isPartVisible(PlayerModelPart.HAT);
        model.body.visible = player.isPartVisible(PlayerModelPart.JACKET);
        model.leftLeg.visible = player.isPartVisible(PlayerModelPart.LEFT_PANTS_LEG);
        model.rightLeg.visible = player.isPartVisible(PlayerModelPart.RIGHT_PANTS_LEG);
        model.leftArm.visible = player.isPartVisible(PlayerModelPart.LEFT_SLEEVE);
        model.rightArm.visible = player.isPartVisible(PlayerModelPart.RIGHT_SLEEVE);
        model.leftArmPose = 0;
        model.aiming = false;
        model.sneaking = player.isSneaking();
        if (heldItem == null) {
            model.rightArmPose = 0;
        }
        else {
            model.rightArmPose = 1;
            if (player.getItemUseTicks() > 0) {
                final UseAction action = heldItem.getUseAction();
                if (action == UseAction.BLOCK) {
                    model.rightArmPose = 3;
                }
                else if (action == UseAction.BOW) {
                    model.aiming = true;
                }
            }
        }
    }
}