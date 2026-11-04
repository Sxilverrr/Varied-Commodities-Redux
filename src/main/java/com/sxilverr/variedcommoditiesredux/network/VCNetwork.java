package com.sxilverr.variedcommoditiesredux.network;

import com.sxilverr.variedcommoditiesredux.Compat;
import com.sxilverr.variedcommoditiesredux.VariedCommodities;
import com.sxilverr.variedcommoditiesredux.block.BigSignBlock;
import com.sxilverr.variedcommoditiesredux.block.BookPages;
import com.sxilverr.variedcommoditiesredux.block.entity.BigSignBlockEntity;
import com.sxilverr.variedcommoditiesredux.block.entity.BookBlockEntity;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

//? if >=1.21 {
/*import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
*///?} else {
import io.netty.buffer.Unpooled;
//?}

public final class VCNetwork {
    //? if >=1.21 {
    /*public interface VCPacket extends CustomPacketPayload {
        String id();

        void write(FriendlyByteBuf buf);

        @Override
        default Type<? extends CustomPacketPayload> type() {
            return new Type<>(VariedCommodities.id(id()));
        }
    }
    *///?} else {
    public interface VCPacket {
        String id();

        void write(FriendlyByteBuf buf);
    }
    //?}

    public record OpenSignEditor(BlockPos pos) implements VCPacket {
        public static final String ID = "open_sign_editor";

        public static OpenSignEditor read(FriendlyByteBuf buf) {
            return new OpenSignEditor(buf.readBlockPos());
        }

        @Override
        public String id() {
            return ID;
        }

        @Override
        public void write(FriendlyByteBuf buf) {
            buf.writeBlockPos(pos);
        }
    }

    public record OpenBook(BlockPos pos, CompoundTag book) implements VCPacket {
        public static final String ID = "open_book";

        public static OpenBook read(FriendlyByteBuf buf) {
            return new OpenBook(buf.readBlockPos(), buf.readNbt());
        }

        @Override
        public String id() {
            return ID;
        }

        @Override
        public void write(FriendlyByteBuf buf) {
            buf.writeBlockPos(pos);
            buf.writeNbt(book);
        }
    }

    public record SaveSign(BlockPos pos, List<String> lines) implements VCPacket {
        public static final String ID = "save_sign";

        public static SaveSign read(FriendlyByteBuf buf) {
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
        public void write(FriendlyByteBuf buf) {
            buf.writeBlockPos(pos);
            buf.writeVarInt(lines.size());
            for (String line : lines) buf.writeUtf(line, 384);
        }
    }

    public record SaveBook(BlockPos pos, boolean sign, String title, List<String> pages) implements VCPacket {
        public static final String ID = "save_book";

        public static SaveBook read(FriendlyByteBuf buf) {
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
        public void write(FriendlyByteBuf buf) {
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
        //? if >=1.21 {
        /*if (Platform.getEnvironment() == Env.SERVER) {
            NetworkManager.registerS2CPayloadType(type(OpenSignEditor.ID), codec(OpenSignEditor::read));
            NetworkManager.registerS2CPayloadType(type(OpenBook.ID), codec(OpenBook::read));
        }
        *///?}
    }

    //? if >=1.21 {
    /*private static <T extends VCPacket> CustomPacketPayload.Type<T> type(String id) {
        return new CustomPacketPayload.Type<>(VariedCommodities.id(id));
    }

    private static <T extends VCPacket> StreamCodec<RegistryFriendlyByteBuf, T> codec(Function<FriendlyByteBuf, T> reader) {
        return StreamCodec.of((buf, packet) -> packet.write(buf), reader::apply);
    }

    private static <T extends VCPacket> void serverbound(String id, Function<FriendlyByteBuf, T> reader, BiConsumer<T, ServerPlayer> handler) {
        NetworkManager.registerReceiver(NetworkManager.c2s(), type(id), codec(reader), (packet, context) -> context.queue(() -> handler.accept(packet, (ServerPlayer) context.getPlayer())));
    }

    public static <T extends VCPacket> void clientbound(String id, Function<FriendlyByteBuf, T> reader, Consumer<T> handler) {
        NetworkManager.registerReceiver(NetworkManager.s2c(), type(id), codec(reader), (packet, context) -> context.queue(() -> handler.accept(packet)));
    }

    public static void send(ServerPlayer player, VCPacket packet) {
        NetworkManager.sendToPlayer(player, packet);
    }

    public static void sendToServer(VCPacket packet) {
        NetworkManager.sendToServer(packet);
    }
    *///?} else {
    private static <T extends VCPacket> void serverbound(String id, Function<FriendlyByteBuf, T> reader, BiConsumer<T, ServerPlayer> handler) {
        NetworkManager.registerReceiver(NetworkManager.c2s(), VariedCommodities.id(id), (buf, context) -> {
            T packet = reader.apply(buf);
            context.queue(() -> handler.accept(packet, (ServerPlayer) context.getPlayer()));
        });
    }

    public static <T extends VCPacket> void clientbound(String id, Function<FriendlyByteBuf, T> reader, Consumer<T> handler) {
        NetworkManager.registerReceiver(NetworkManager.s2c(), VariedCommodities.id(id), (buf, context) -> {
            T packet = reader.apply(buf);
            context.queue(() -> handler.accept(packet));
        });
    }

    public static void send(ServerPlayer player, VCPacket packet) {
        NetworkManager.sendToPlayer(player, VariedCommodities.id(packet.id()), encode(packet));
    }

    public static void sendToServer(VCPacket packet) {
        NetworkManager.sendToServer(VariedCommodities.id(packet.id()), encode(packet));
    }

    private static FriendlyByteBuf encode(VCPacket packet) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        packet.write(buf);
        return buf;
    }
    //?}

    public static void openSignEditor(ServerPlayer player, BlockPos pos) {
        send(player, new OpenSignEditor(pos));
    }

    public static void openBook(ServerPlayer player, BlockPos pos, ItemStack book) {
        send(player, new OpenBook(pos, Compat.saveStack(book, player.level().registryAccess())));
    }

    private static void saveSign(SaveSign packet, ServerPlayer player) {
        BlockPos pos = packet.pos();
        if (!player.level().isLoaded(pos) || player.distanceToSqr(pos.getCenter()) > 64.0) return;
        if (player.level().getBlockEntity(pos) instanceof BigSignBlockEntity sign && !sign.waxed
                && sign.getBlockState().getBlock() instanceof BigSignBlock block && block.hasText()) {
            List<String> lines = packet.lines();
            sign.setLines(lines.subList(0, Math.min(lines.size(), block.lineCount())).toArray(new String[0]));
            sign.sync();
        }
    }

    private static void saveBook(SaveBook packet, ServerPlayer player) {
        BlockPos pos = packet.pos();
        if (!player.level().isLoaded(pos) || player.distanceToSqr(pos.getCenter()) > 64.0) return;
        if (!(player.level().getBlockEntity(pos) instanceof BookBlockEntity be) || be.book.is(Items.WRITTEN_BOOK)) return;
        be.book = packet.sign() && !packet.title().isBlank()
                ? BookPages.written(packet.pages(), packet.title().trim(), player.getGameProfile().getName())
                : BookPages.writable(packet.pages());
        be.sync();
    }

    private VCNetwork() {
    }
}
