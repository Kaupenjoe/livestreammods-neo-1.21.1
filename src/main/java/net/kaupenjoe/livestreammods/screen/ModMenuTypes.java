package net.kaupenjoe.livestreammods.screen;

import net.kaupenjoe.livestreammods.LivestreamMods;
import net.kaupenjoe.livestreammods.screen.custom.KaupenFurnaceMenu;
import net.kaupenjoe.livestreammods.screen.custom.PedestalMenu;
import net.kaupenjoe.livestreammods.screen.custom.SidePedestalMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, LivestreamMods.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<PedestalMenu>> PEDESTAL_MENU =
            registerMenuType("pedestal_menu", PedestalMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<SidePedestalMenu>> SIDE_PEDESTAL_MENU =
            registerMenuType("side_pedestal_menu", SidePedestalMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<KaupenFurnaceMenu>> KAUPEN_FURNACE_MENU =
            registerMenuType("kaupen_furnace_menu", KaupenFurnaceMenu::new);


    private static <T extends AbstractContainerMenu>DeferredHolder<MenuType<?>, MenuType<T>> registerMenuType(String name,
                                                                                                              IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IMenuTypeExtension.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
