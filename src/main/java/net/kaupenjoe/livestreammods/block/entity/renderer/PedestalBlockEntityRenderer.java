package net.kaupenjoe.livestreammods.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.kaupenjoe.livestreammods.block.ModBlocks;
import net.kaupenjoe.livestreammods.block.entity.custom.PedestalBlockEntity;
import net.kaupenjoe.livestreammods.render.ModRenderTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.joml.Vector2i;

import java.util.List;

public class PedestalBlockEntityRenderer implements BlockEntityRenderer<PedestalBlockEntity> {
    List<Vector2i> offsets = List.of(
            new Vector2i(3, 0),
            new Vector2i(2, 2),
            new Vector2i(0, 3),
            new Vector2i(2, -2),

            new Vector2i(-2, 2),
            new Vector2i(-2, -2),
            new Vector2i(0, -3),
            new Vector2i(-3, 0));

    public PedestalBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(PedestalBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack,
                       MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay){
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack stack = pBlockEntity.inventory.getStackInSlot(0);

        pPoseStack.pushPose();
        pPoseStack.translate(0.5f, 1.15f, 0.5f);
        pPoseStack.scale(0.5f, 0.5f, 0.5f);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(pBlockEntity.getRenderingRotation()));

        itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, getLightLevel(pBlockEntity.getLevel(),
                pBlockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY, pPoseStack, pBufferSource, pBlockEntity.getLevel(), 1);

        pPoseStack.popPose();

        offsets.forEach(offset -> {
            if(pBlockEntity.getLevel().getBlockState(pBlockEntity.getBlockPos().offset(offset.x, 0, offset.y)).isAir()) {
                renderSidePedestal(pBlockEntity, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay, offset.x, offset.y);
            }
        });
    }

    private static void renderSidePedestal(PedestalBlockEntity pBlockEntity, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay,
                                           float xOffset, float zOffset) {
        BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        BlockState state = ModBlocks.SIDE_PEDESTAL.get().defaultBlockState();
        BakedModel model = blockRenderer.getBlockModel(state);

        pPoseStack.pushPose();
        pPoseStack.translate(xOffset, 0f, zOffset);

        // Use translucent render type
        RenderType translucentType = ModRenderTypes.GHOST;
        VertexConsumer consumer = pBufferSource.getBuffer(translucentType).setColor(1f, 1f, 1f, 0.5f);

        // Render the model with alpha
        blockRenderer.getModelRenderer().renderModel(
                pPoseStack.last(),
                consumer,
                state,
                model,
                1f, 1f, 1f, // RGB
                pPackedLight,
                pPackedOverlay,
                ModelData.EMPTY,
                translucentType);

        // RenderSystem.enableBlend();
        // RenderSystem.defaultBlendFunc();
        // RenderSystem.setShaderColor(1f, 1f, 1f, 0.5f);
        // RenderSystem.disableBlend();

        pPoseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(PedestalBlockEntity blockEntity) {
        return true;
    }

    @Override
    public AABB getRenderBoundingBox(PedestalBlockEntity blockEntity) {
        return AABB.INFINITE;
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
