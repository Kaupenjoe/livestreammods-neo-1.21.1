package net.kaupenjoe.livestreammods.screen.custom;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.client.gui.screens.recipebook.AbstractFurnaceRecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

public class KaupenFurnaceScreen extends AbstractFurnaceScreen<KaupenFurnaceMenu> {
    private static final ResourceLocation LIT_PROGRESS_SPRITE = ResourceLocation.withDefaultNamespace("container/furnace/lit_progress");
    private static final ResourceLocation BURN_PROGRESS_SPRITE = ResourceLocation.withDefaultNamespace("container/furnace/burn_progress");
    private static final ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace("textures/gui/container/furnace.png");

    public KaupenFurnaceScreen(KaupenFurnaceMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        this(pMenu, new KaupenFurnaceRecipeBookComponent(), pPlayerInventory, pTitle, TEXTURE);
    }

    private KaupenFurnaceScreen(KaupenFurnaceMenu pMenu, AbstractFurnaceRecipeBookComponent pRecipeBookComponent,
                               Inventory pPlayerInventory, Component pTitle, ResourceLocation pTexture) {
        super(pMenu, pRecipeBookComponent, pPlayerInventory, pTitle, pTexture, LIT_PROGRESS_SPRITE, BURN_PROGRESS_SPRITE);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int i = this.leftPos;
        int j = this.topPos;
        guiGraphics.blit(TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight);
        if (this.menu.isLit()) {
            int k = 14;
            int l = Mth.ceil(this.menu.getLitProgress() * 13.0F) + 1;
            guiGraphics.blitSprite(LIT_PROGRESS_SPRITE, 14, 14, 0, 14 - l, i + 56, j + 36 + 14 - l, 14, l);
        }

        int arrowLength = 24;
        //
        int j1 = Mth.ceil(arrowLength - this.menu.getBurnProgress() * 24.0F);
        int arrowProgress = j1 == 24 ? 0 : j1;
        guiGraphics.blitSprite(BURN_PROGRESS_SPRITE, 24, 16, 0, 0, i + 79, j + 34, arrowProgress, 16);
    }
}
