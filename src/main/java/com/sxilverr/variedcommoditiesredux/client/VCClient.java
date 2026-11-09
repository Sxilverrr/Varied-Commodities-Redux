package com.sxilverr.variedcommoditiesredux.client;

import com.sxilverr.variedcommoditiesredux.Compat;
import com.sxilverr.variedcommoditiesredux.VCArmors;
import com.sxilverr.variedcommoditiesredux.VCBlockEntities;
import com.sxilverr.variedcommoditiesredux.VCBlocks;
import com.sxilverr.variedcommoditiesredux.VCEntities;
import com.sxilverr.variedcommoditiesredux.VCItems;
import com.sxilverr.variedcommoditiesredux.VCMenus;
import com.sxilverr.variedcommoditiesredux.VCWeapons;
import com.sxilverr.variedcommoditiesredux.VariedCommodities;
import com.sxilverr.variedcommoditiesredux.block.BigSignBlock;
import com.sxilverr.variedcommoditiesredux.block.BookPages;
import com.sxilverr.variedcommoditiesredux.block.CrystalBlock;
import com.sxilverr.variedcommoditiesredux.block.RotatedBlock;
import com.sxilverr.variedcommoditiesredux.client.renderer.BlockRenderers;
import com.sxilverr.variedcommoditiesredux.client.renderer.ProjectileRenderer;
import com.sxilverr.variedcommoditiesredux.client.renderer.SeatRenderer;
import com.sxilverr.variedcommoditiesredux.client.renderer.ThrownWeaponRenderer;
import com.sxilverr.variedcommoditiesredux.client.screen.OpenBookScreen;
import com.sxilverr.variedcommoditiesredux.client.screen.CarpentryScreen;
import com.sxilverr.variedcommoditiesredux.client.screen.BigSignEditScreen;
import com.sxilverr.variedcommoditiesredux.client.screen.TradingScreen;
import com.sxilverr.variedcommoditiesredux.block.entity.BigSignBlockEntity;
import com.sxilverr.variedcommoditiesredux.item.MiscItems;
import com.sxilverr.variedcommoditiesredux.network.VCNetwork;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.item.ItemPropertiesRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public final class VCClient {
    public static void init() {
        EntityRendererRegistry.register(VCEntities.SEAT, SeatRenderer::new);
        EntityRendererRegistry.register(VCEntities.PROJECTILE, ProjectileRenderer::new);
        EntityRendererRegistry.register(VCEntities.MAGIC_PROJECTILE, ProjectileRenderer::new);
        EntityRendererRegistry.register(VCEntities.HOLY_HAND_GRENADE, ProjectileRenderer::new);
        EntityRendererRegistry.register(VCEntities.THROWN_WEAPON, ThrownWeaponRenderer::new);
        registerColors();
        VCNetwork.clientbound(VCNetwork.OpenSignEditor.ID, VCNetwork.OpenSignEditor::read, packet -> openSignEditor(packet.pos()));
        VCNetwork.clientbound(VCNetwork.OpenBook.ID, VCNetwork.OpenBook::read, packet -> openBook(packet.pos(), packet.book()));
        VCMenus.CARPENTRY.listen(type -> MenuRegistry.registerScreenFactory(type, CarpentryScreen::new));
        VCMenus.TRADING.listen(type -> MenuRegistry.registerScreenFactory(type, TradingScreen::new));
        ClientLifecycleEvent.CLIENT_SETUP.register(minecraft -> setup());
    }

    private static void setup() {
        BlockEntityRendererRegistry.register(VCBlockEntities.WEAPON_RACK.get(), BlockRenderers.of(BlockRenderers::weaponRack));
        BlockEntityRendererRegistry.register(VCBlockEntities.PEDESTAL.get(), BlockRenderers.of(BlockRenderers::pedestal));
        BlockEntityRendererRegistry.register(VCBlockEntities.SHOP_SIGN.get(), BlockRenderers.of(BlockRenderers::shopSign));
        BlockEntityRendererRegistry.register(VCBlockEntities.BANNER.get(), BlockRenderers.of(BlockRenderers::banner));
        BlockEntityRendererRegistry.register(VCBlockEntities.WALL_BANNER.get(), BlockRenderers.of(BlockRenderers::wallBanner));
        BlockEntityRendererRegistry.register(VCBlockEntities.BIG_SIGN.get(), BlockRenderers.of(BlockRenderers::bigSign));
        BlockEntityRendererRegistry.register(VCBlockEntities.TOMBSTONE.get(), BlockRenderers.of(BlockRenderers::tombstone));
        BlockEntityRendererRegistry.register(VCBlockEntities.BOOK.get(), BlockRenderers.of(BlockRenderers::book));
        RenderTypeRegistry.register(RenderType.translucent(), VCBlocks.all(VCBlocks.CRYSTALS.values()));
        RenderTypeRegistry.register(RenderType.cutout(), VCBlocks.all(VCBlocks.BLOOD));
        RenderTypeRegistry.register(RenderType.cutout(), VCBlocks.all(VCBlocks.FURNITURE));
        registerItemProperties();
    }

    private static int rgb(int color) {
        float[] c = VCRender.color(color);
        return 0xFF000000 | (int) (c[0] * 255) << 16 | (int) (c[1] * 255) << 8 | (int) (c[2] * 255);
    }

    private static void registerColors() {
        List<RegistrySupplier<Item>> dyed = new ArrayList<>();
        dyed.addAll(VCItems.ORBS.values());
        dyed.addAll(VCItems.BROKEN_ORBS.values());
        dyed.addAll(VCWeapons.ELEMENTAL_STAFFS.values());
        ColorHandlerRegistry.registerItemColors((stack, tint) -> 0xFF000000 | Compat.sheepRgb(dyeOf(stack.getItem())), dyed.toArray(RegistrySupplier[]::new));
        ColorHandlerRegistry.registerItemColors((stack, tint) -> 0xFF000000 | Compat.sheepRgb(((CrystalBlock) ((BlockItem) stack.getItem()).getBlock()).color()),
                VCBlocks.CRYSTALS.values().toArray(RegistrySupplier[]::new));
        ColorHandlerRegistry.registerBlockColors((state, level, pos, tint) -> 0xFF000000 | ((CrystalBlock) state.getBlock()).color().getMapColor().col,
                VCBlocks.CRYSTALS.values().toArray(RegistrySupplier[]::new));
        ColorHandlerRegistry.registerItemColors((stack, tint) -> tint > 0 ? -1 : 0xFF000000 | Compat.armorColor(stack), VCArmors.DYEABLE.toArray(RegistrySupplier[]::new));
        List<RegistrySupplier<Block>> tinted = new ArrayList<>();
        tinted.addAll(VCBlocks.BANNERS.values());
        tinted.addAll(VCBlocks.WALL_BANNERS.values());
        tinted.addAll(VCBlocks.TALL_LAMPS.values());
        tinted.addAll(VCBlocks.WOOL_COUCHES.values());
        ColorHandlerRegistry.registerBlockColors((state, level, pos, tint) -> rgb(state.getValue(RotatedBlock.COLOR).getId()), tinted.toArray(RegistrySupplier[]::new));
        ColorHandlerRegistry.registerItemColors((stack, tint) -> rgb(((RotatedBlock) ((BlockItem) stack.getItem()).getBlock()).defaultColor()), tinted.toArray(RegistrySupplier[]::new));
    }

    private static DyeColor dyeOf(Item item) {
        if (item instanceof MiscItems.Colored colored) return colored.color();
        for (var entry : VCWeapons.ELEMENTAL_STAFFS.entrySet()) {
            if (entry.getValue().get() == item) return entry.getKey();
        }
        return DyeColor.WHITE;
    }

    private static void registerItemProperties() {
        ClampedItemPropertyFunction pull = (stack, level, entity, seed) ->
                entity != null && entity.getUseItem() == stack ? (stack.getUseDuration(
                        //? if >=1.21 {
                        /*entity
                        *///?}
                ) - entity.getUseItemRemainingTicks()) / 20.0F : 0.0F;
        ClampedItemPropertyFunction using = (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
        List<RegistrySupplier<Item>> charged = new ArrayList<>();
        charged.addAll(VCWeapons.GUNS.values());
        charged.addAll(VCWeapons.STAFFS.values());
        charged.addAll(VCWeapons.ELEMENTAL_STAFFS.values());
        charged.addAll(List.of(VCWeapons.MACHINE_GUN, VCWeapons.MUSKET, VCWeapons.CROSSBOW, VCWeapons.SLINGSHOT,
                VCItems.VIOLIN, VCItems.HARP, VCItems.CLARINET, VCItems.OCARINA));
        for (RegistrySupplier<Item> item : charged) {
            ItemPropertiesRegistry.register(item.get(), VariedCommodities.rl("minecraft", "pull"), pull);
            ItemPropertiesRegistry.register(item.get(), VariedCommodities.rl("minecraft", "pulling"), using);
        }
        List<RegistrySupplier<Item>> blocking = new ArrayList<>();
        blocking.addAll(VCWeapons.SHIELDS.values());
        blocking.addAll(VCWeapons.ROUND_SHIELDS.values());
        blocking.addAll(List.of(VCWeapons.HEATER_SHIELD, VCWeapons.CRESCENT_SHIELD, VCWeapons.SCUTUM_SHIELD, VCWeapons.TOWER_SHIELD, VCWeapons.MUSKET));
        for (RegistrySupplier<Item> item : blocking) {
            ItemPropertiesRegistry.register(item.get(), VariedCommodities.rl("minecraft", "blocking"), using);
        }
    }

    private static void openSignEditor(BlockPos pos) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level != null && minecraft.level.getBlockEntity(pos) instanceof BigSignBlockEntity sign
                && sign.getBlockState().getBlock() instanceof BigSignBlock block && block.hasText()) {
            minecraft.setScreen(new BigSignEditScreen(sign, block));
        }
    }

    private static void openBook(BlockPos pos, CompoundTag tag) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || tag == null) return;
        ItemStack book = Compat.loadStack(tag, minecraft.level.registryAccess());
        if (book.is(Items.WRITTEN_BOOK)) {
            minecraft.setScreen(new BookViewScreen(BookViewScreen.BookAccess.fromItem(book)));
        } else {
            minecraft.setScreen(new OpenBookScreen(pos, BookPages.pages(book)));
        }
    }

    private VCClient() {
    }
}
