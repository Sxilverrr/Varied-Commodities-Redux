package com.sxilverr.variedcommoditiesredux.client;

import com.sxilverr.variedcommoditiesredux.VCBlockEntities;
import com.sxilverr.variedcommoditiesredux.VCBlocks;
import com.sxilverr.variedcommoditiesredux.VCEntities;
import com.sxilverr.variedcommoditiesredux.VCMenus;
import com.sxilverr.variedcommoditiesredux.VCRecipes;
import com.sxilverr.variedcommoditiesredux.block.BigSignBlock;
import com.sxilverr.variedcommoditiesredux.block.BookPages;
import com.sxilverr.variedcommoditiesredux.block.CrystalBlock;
import com.sxilverr.variedcommoditiesredux.block.RotatedBlock;
import com.sxilverr.variedcommoditiesredux.block.entity.BigSignBlockEntity;
import com.sxilverr.variedcommoditiesredux.client.renderer.BlockRenderers;
import com.sxilverr.variedcommoditiesredux.client.renderer.ProjectileRenderer;
import com.sxilverr.variedcommoditiesredux.client.renderer.SeatRenderer;
import com.sxilverr.variedcommoditiesredux.client.renderer.ThrownWeaponRenderer;
import com.sxilverr.variedcommoditiesredux.client.screen.BigSignEditScreen;
import com.sxilverr.variedcommoditiesredux.client.screen.CarpentryScreen;
import com.sxilverr.variedcommoditiesredux.client.screen.OpenBookScreen;
import com.sxilverr.variedcommoditiesredux.client.screen.TradingScreen;
import com.sxilverr.variedcommoditiesredux.network.VCNetwork;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.client.gui.MenuScreenRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class VCClient {
    public static void init() {
        EntityRendererRegistry.register(VCEntities.SEAT, SeatRenderer::new);
        EntityRendererRegistry.register(VCEntities.PROJECTILE, ProjectileRenderer::new);
        EntityRendererRegistry.register(VCEntities.MAGIC_PROJECTILE, ProjectileRenderer::new);
        EntityRendererRegistry.register(VCEntities.HOLY_HAND_GRENADE, ProjectileRenderer::new);
        EntityRendererRegistry.register(VCEntities.THROWN_WEAPON, ThrownWeaponRenderer::new);
        VCNetwork.clientbound(VCNetwork.OpenSignEditor.ID, VCNetwork.OpenSignEditor::read, packet -> openSignEditor(packet.pos()));
        VCNetwork.clientbound(VCNetwork.OpenBook.ID, VCNetwork.OpenBook::read, packet -> openBook(packet.pos(), packet.book()));
        VCNetwork.clientbound(VCNetwork.SyncRecipes.ID, VCNetwork.SyncRecipes::read, packet -> {
            VCRecipes.synced = packet.entries();
            VCRecipes.onSync.run();
        });
        VCMenus.CARPENTRY.listen(type -> MenuScreenRegistry.registerScreenFactory(type, CarpentryScreen::new));
        VCMenus.TRADING.listen(type -> MenuScreenRegistry.registerScreenFactory(type, TradingScreen::new));
        ClientLifecycleEvent.CLIENT_SETUP.register(minecraft -> setup());
    }

    private static void setup() {
        registerColors();
        BlockEntityRendererRegistry.register(VCBlockEntities.WEAPON_RACK.get(), BlockRenderers.WeaponRack::new);
        BlockEntityRendererRegistry.register(VCBlockEntities.PEDESTAL.get(), BlockRenderers.Pedestal::new);
        BlockEntityRendererRegistry.register(VCBlockEntities.SHOP_SIGN.get(), BlockRenderers.ShopSign::new);
        BlockEntityRendererRegistry.register(VCBlockEntities.BANNER.get(), BlockRenderers.Banner::standing);
        BlockEntityRendererRegistry.register(VCBlockEntities.WALL_BANNER.get(), BlockRenderers.Banner::wall);
        BlockEntityRendererRegistry.register(VCBlockEntities.BIG_SIGN.get(), BlockRenderers.Sign::new);
        BlockEntityRendererRegistry.register(VCBlockEntities.TOMBSTONE.get(), BlockRenderers.Sign::new);
        BlockEntityRendererRegistry.register(VCBlockEntities.BOOK.get(), BlockRenderers.Book::new);
    }

    private static int rgb(int color) {
        float[] c = VCRender.color(color);
        return 0xFF000000 | (int) (c[0] * 255) << 16 | (int) (c[1] * 255) << 8 | (int) (c[2] * 255);
    }

    @SuppressWarnings("unchecked")
    private static void registerColors() {
        ColorHandlerRegistry.registerBlockColors(state -> 0xFF000000 | ((CrystalBlock) state.getBlock()).color().getMapColor().col,
                VCBlocks.CRYSTALS.values().toArray(RegistrySupplier[]::new));
        List<RegistrySupplier<Block>> tinted = new ArrayList<>();
        tinted.addAll(VCBlocks.BANNERS.values());
        tinted.addAll(VCBlocks.WALL_BANNERS.values());
        tinted.addAll(VCBlocks.TALL_LAMPS.values());
        tinted.addAll(VCBlocks.WOOL_COUCHES.values());
        ColorHandlerRegistry.registerBlockColors(new BlockTintSource() {
            @Override
            public int color(BlockState state) {
                return rgb(state.getValue(RotatedBlock.COLOR).getId());
            }

            @Override
            public Set<Property<?>> relevantProperties() {
                return Set.of(RotatedBlock.COLOR);
            }
        }, tinted.toArray(RegistrySupplier[]::new));
    }

    private static void openSignEditor(BlockPos pos) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level != null && minecraft.level.getBlockEntity(pos) instanceof BigSignBlockEntity sign
                && sign.getBlockState().getBlock() instanceof BigSignBlock block && block.hasText()) {
            open(new BigSignEditScreen(sign, block));
        }
    }

    private static void openBook(BlockPos pos, ItemStack book) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) return;
        if (book.is(Items.WRITTEN_BOOK)) {
            open(new BookViewScreen(BookViewScreen.BookAccess.fromItem(book)));
        } else {
            open(new OpenBookScreen(pos, BookPages.pages(book)));
        }
    }

    public static void open(Screen screen) {
        //? if >=26.2 {
        /*Minecraft.getInstance().gui.setScreen(screen);
        *///?} else {
        Minecraft.getInstance().setScreen(screen);
        //?}
    }

    private VCClient() {
    }
}
