package com.sxilverr.variedcommoditiesredux.client.screen;

import com.sxilverr.variedcommoditiesredux.block.BookPages;
import com.sxilverr.variedcommoditiesredux.network.VCNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;

import java.util.ArrayList;
import java.util.List;

public class OpenBookScreen extends BookEditScreen {
    private final BlockPos pos;

    public OpenBookScreen(BlockPos pos, List<String> pages) {
        super(Minecraft.getInstance().player, BookPages.writable(pages), InteractionHand.MAIN_HAND);
        this.pos = pos;
    }

    public void save(List<String> pages, String title, boolean sign) {
        int last = pages.size();
        while (last > 0 && pages.get(last - 1).isEmpty()) last--;
        VCNetwork.sendToServer(new VCNetwork.SaveBook(pos, sign, sign ? title : "", new ArrayList<>(pages.subList(0, last))));
    }
}
