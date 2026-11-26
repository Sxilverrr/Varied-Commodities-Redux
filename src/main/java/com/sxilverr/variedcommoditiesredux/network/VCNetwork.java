package com.sxilverr.variedcommoditiesredux.network;

import com.sxilverr.variedcommoditiesredux.VCRecipes;
import com.sxilverr.variedcommoditiesredux.VariedCommodities;
import com.sxilverr.variedcommoditiesredux.block.BigSignBlock;
import com.sxilverr.variedcommoditiesredux.block.BookPages;
import com.sxilverr.variedcommoditiesredux.block.entity.BigSignBlockEntity;
import com.sxilverr.variedcommoditiesredux.block.entity.BookBlockEntity;
import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public final class VCNetwork {
    public interface VCPacket extends CustomPacketPayload {
        String id();

        void write(RegistryFriendlyByteBuf buf);

        @Override
        default Type<? extends CustomPacketPayload> type() {
            return new Type<>(VariedCommodities.id(id()));
        }
    }

    public record OpenSignEditor(BlockPos pos) implements VCPacket {
        public static final String ID = "open_sign_editor";

        public static OpenSignEditor read(RegistryFriendlyByteBuf buf) {
            return new OpenSignEditor(buf.readBlockPos());
        }

        @Override
        public String id() {
            return ID;
        }

        @Override
        public void write(RegistryFriendlyByteBuf buf) {
            buf.writeBlockPos(pos);
        }
    }

    public record OpenBook(BlockPos pos, ItemStack book) implements VCPacket {
        public static final String ID = "open_book";

        public static OpenBook read(RegistryFriendlyByteBuf buf) {
            return new OpenBook(buf.readBlockPos(), ItemStack.OPTIONAL_STREAM_CODEC.decode(buf));
        }

        @Override
        public String id() {
            return ID;
        }

        @Override
        public void write(RegistryFriendlyByteBuf buf) {
            buf.writeBlockPos(pos);
            ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, book);
        }
    }

    public record SyncRecipes(List<VCRecipes.Entry> entries) implements VCPacket {
        public static final String ID = "sync_recipes";
        private static final StreamCodec<RegistryFriendlyByteBuf, List<VCRecipes.Entry>> CODEC = VCRecipes.Entry.STREAM_CODEC.apply(ByteBufCodecs.list());

        public static SyncRecipes read(RegistryFriendlyByteBuf buf) {
            return new SyncRecipes(CODEC.decode(buf));
        }

        @Override
        public String id() {
            return ID;
        }

        @Override
        public void write(RegistryFriendlyByteBuf buf) {
            CODEC.encode(buf, entries);
        }
    }

    public record SaveSign(BlockPos pos, List<String> lines) implements VCPacket {
        public static final String ID = "save_sign";

        public static SaveSign read(RegistryFriendlyByteBuf buf) {
            BlockPos pos = buf.readBlockPos();
            int count = Math.min(buf.readVarInt(), 16);
            List<String> lines = new ArrayList<>();
            for (int i = 0; i < count; i++) lines.add(buf.readUtf(384));
            return new SaveSign(pos, lines);
        }

        @Override
        public String id() {
            return ID;
        }

        @Override
        public void write(RegistryFriendlyByteBuf buf) {
            buf.writeBlockPos(pos);
            buf.writeVarInt(lines.size());
            for (String line : lines) buf.writeUtf(line, 384);
        }
    }

    public record SaveBook(BlockPos pos, boolean sign, String title, List<String> pages) implements VCPacket {
        public static final String ID = "save_book";

        public static SaveBook read(RegistryFriendlyByteBuf buf) {
            BlockPos pos = buf.readBlockPos();
            boolean sign = buf.readBoolean();
            String title = buf.readUtf(32);
            int count = Math.min(buf.readVarInt(), BookPages.MAX_PAGES);
            List<String> pages = new ArrayList<>();
            for (int i = 0; i < count; i++) pages.add(buf.readUtf(BookPages.MAX_PAGE_LENGTH));
            return new SaveBook(pos, sign, title, pages);
        }

        @Override
        public String id() {
            return ID;
        }

        @Override
        public void write(RegistryFriendlyByteBuf buf) {
            buf.writeBlockPos(pos);
            buf.writeBoolean(sign);
            buf.writeUtf(title, 32);
            buf.writeVarInt(pages.size());
            for (String page : pages) buf.writeUtf(page, BookPages.MAX_PAGE_LENGTH);
        }
    }

    public static void init() {
        serverbound(SaveSign.ID, SaveSign::read, VCNetwork::saveSign);
        serverbound(SaveBook.ID, SaveBook::read, VCNetwork::saveBook);
        if (Platform.getEnvironment() == Env.SERVER) {
            NetworkManager.registerS2CPayloadType(type(OpenSignEditor.ID), codec(OpenSignEditor::read));
            NetworkManager.registerS2CPayloadType(type(OpenBook.ID), codec(OpenBook::read));
            NetworkManager.registerS2CPayloadType(type(SyncRecipes.ID), codec(SyncRecipes::read));
        }
    }

    private static <T extends VCPacket> CustomPacketPayload.Type<T> type(String id) {
        return new CustomPacketPayload.Type<>(VariedCommodities.id(id));
    }

    private static <T extends VCPacket> StreamCodec<RegistryFriendlyByteBuf, T> codec(Function<RegistryFriendlyByteBuf, T> reader) {
        return StreamCodec.of((buf, packet) -> packet.write(buf), reader::apply);
    }

    private static <T extends VCPacket> void serverbound(String id, Function<RegistryFriendlyByteBuf, T> reader, BiConsumer<T, ServerPlayer> handler) {
        NetworkManager.registerReceiver(NetworkManager.c2s(), type(id), codec(reader), (packet, context) -> context.queue(() -> handler.accept(packet, (ServerPlayer) context.getPlayer())));
    }

    public static <T extends VCPacket> void clientbound(String id, Function<RegistryFriendlyByteBuf, T> reader, Consumer<T> handler) {
        NetworkManager.registerReceiver(NetworkManager.s2c(), type(id), codec(reader), (packet, context) -> context.queue(() -> handler.accept(packet)));
    }

    public static void send(ServerPlayer player, VCPacket packet) {
        NetworkManager.sendToPlayer(player, packet);
    }

    public static void sendToServer(VCPacket packet) {
        NetworkManager.sendToServer(packet);
    }

    public static void openSignEditor(ServerPlayer player, BlockPos pos) {
        send(player, new OpenSignEditor(pos));
    }

    public static void openBook(ServerPlayer player, BlockPos pos, ItemStack book) {
        send(player, new OpenBook(pos, book));
    }

    public static void syncRecipes(ServerPlayer player) {
        send(player, new SyncRecipes(VCRecipes.entries(player.level().getServer().getRecipeManager())));
    }

    private static void saveSign(SaveSign packet, ServerPlayer player) {
        BlockPos pos = packet.pos();
        if (!player.level().isLoaded(pos) || player.distanceToSqr(Vec3.atCenterOf(pos)) > 64.0) return;
        if (player.level().getBlockEntity(pos) instanceof BigSignBlockEntity sign && !sign.waxed
                && sign.getBlockState().getBlock() instanceof BigSignBlock block && block.hasText()) {
            List<String> lines = packet.lines();
            sign.setLines(lines.subList(0, Math.min(lines.size(), block.lineCount())).toArray(new String[0]));
            sign.sync();
        }
    }

    private static void saveBook(SaveBook packet, ServerPlayer player) {
        BlockPos pos = packet.pos();
        if (!player.level().isLoaded(pos) || player.distanceToSqr(Vec3.atCenterOf(pos)) > 64.0) return;
        if (!(player.level().getBlockEntity(pos) instanceof BookBlockEntity be) || be.book.is(Items.WRITTEN_BOOK)) return;
        be.book = packet.sign() && !packet.title().isBlank()
                ? BookPages.written(packet.pages(), packet.title().trim(), player.getName().getString())
                : BookPages.writable(packet.pages());
        be.sync();
    }

    private VCNetwork() {
    }
}
