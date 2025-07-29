package net.kaupenjoe.livestreammods;

import net.kaupenjoe.livestreammods.block.ModBlocks;
import net.kaupenjoe.livestreammods.block.entity.ModBlockEntities;
import net.kaupenjoe.livestreammods.block.entity.renderer.PedestalBlockEntityRenderer;
import net.kaupenjoe.livestreammods.block.entity.renderer.SidePedestalBlockEntityRenderer;
import net.kaupenjoe.livestreammods.screen.ModMenuTypes;
import net.kaupenjoe.livestreammods.screen.custom.PedestalScreen;
import net.kaupenjoe.livestreammods.screen.custom.SidePedestalMenu;
import net.kaupenjoe.livestreammods.screen.custom.SidePedestalScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = LivestreamMods.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = LivestreamMods.MOD_ID, value = Dist.CLIENT)
public class LivestreamModsClient {
    public LivestreamModsClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // ItemBlockRenderTypes.setRenderLayer(ModBlocks.SIDE_PEDESTAL.get(), RenderType.translucent());
    }

    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.MAIN_PEDESTAL_BE.get(), PedestalBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.SIDE_PEDESTAL_BE.get(), SidePedestalBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.PEDESTAL_MENU.get(), PedestalScreen::new);
        event.register(ModMenuTypes.SIDE_PEDESTAL_MENU.get(), SidePedestalScreen::new);
    }
}
