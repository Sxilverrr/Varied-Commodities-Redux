package com.sxilverr.variedcommoditiesredux.block;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

//? if >=1.21 {
/*import net.minecraft.core.component.DataComponents;
import net.minecraft.server.network.Filterable;
import net.minecraft.world.item.component.WritableBookContent;
import net.minecraft.world.item.component.WrittenBookContent;
*///?} else {
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
//?}

public final class BookPages {
    public static final int MAX_PAGES = 100;
    public static final int MAX_PAGE_LENGTH = 1024;

    public static List<String> pages(ItemStack book) {
        List<String> pages = new ArrayList<>();
        //? if >=1.21 {
        /*WritableBookContent writable = book.get(DataComponents.WRITABLE_BOOK_CONTENT);
        if (writable != null) writable.pages().forEach(p -> pages.add(p.raw()));
        WrittenBookContent written = book.get(DataComponents.WRITTEN_BOOK_CONTENT);
        if (written != null) written.pages().forEach(p -> pages.add(p.raw().getString()));
        *///?} else {
        if (book.getTag() == null) return pages;
        ListTag list = book.getTag().getList("pages", Tag.TAG_STRING);
        boolean json = book.is(Items.WRITTEN_BOOK);
        for (int i = 0; i < list.size(); i++) {
            String page = list.getString(i);
            if (json) {
                Component component = Component.Serializer.fromJsonLenient(page);
                page = component == null ? page : component.getString();
            }
            pages.add(page);
        }
        //?}
        return pages;
    }

    public static ItemStack writable(List<String> pages) {
        ItemStack book = new ItemStack(Items.WRITABLE_BOOK);
        List<String> clean = sanitize(pages);
        //? if >=1.21 {
        /*book.set(DataComponents.WRITABLE_BOOK_CONTENT, new WritableBookContent(clean.stream().map(Filterable::passThrough).toList()));
        *///?} else {
        ListTag list = new ListTag();
        clean.forEach(p -> list.add(StringTag.valueOf(p)));
        book.addTagElement("pages", list);
        //?}
        return book;
    }

    public static ItemStack written(List<String> pages, String title, String author) {
        ItemStack book = new ItemStack(Items.WRITTEN_BOOK);
        List<String> clean = sanitize(pages);
        String cleanTitle = title.length() > 32 ? title.substring(0, 32) : title;
        //? if >=1.21 {
        /*List<Filterable<Component>> content = clean.stream().map(p -> Filterable.passThrough((Component) Component.literal(p))).toList();
        book.set(DataComponents.WRITTEN_BOOK_CONTENT, new WrittenBookContent(Filterable.passThrough(cleanTitle), author, 0, content, true));
        *///?} else {
        ListTag list = new ListTag();
        clean.forEach(p -> list.add(StringTag.valueOf(Component.Serializer.toJson(Component.literal(p)))));
        book.addTagElement("pages", list);
        book.addTagElement("title", StringTag.valueOf(cleanTitle));
        book.addTagElement("author", StringTag.valueOf(author));
        //?}
        return book;
    }

    private static List<String> sanitize(List<String> pages) {
        List<String> clean = new ArrayList<>();
        for (String page : pages) {
            if (clean.size() >= MAX_PAGES) break;
            clean.add(page.length() > MAX_PAGE_LENGTH ? page.substring(0, MAX_PAGE_LENGTH) : page);
        }
        return clean;
    }

    private BookPages() {
    }
}
