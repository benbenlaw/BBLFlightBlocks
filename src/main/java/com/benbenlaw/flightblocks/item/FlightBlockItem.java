package com.benbenlaw.flightblocks.item;

import com.benbenlaw.flightblocks.config.StartupConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Properties;
import java.util.function.Consumer;

public class FlightBlockItem extends BlockItem {

    public FlightBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltipAdder, TooltipFlag flag) {

        if (Screen.hasShiftDown()) {
            tooltipAdder.accept(
                    Component.translatable("tooltips.flightblocks.flightblocks.shift.held")
                            .append(String.valueOf(StartupConfig.flightBlockRange.get()))
                            .withStyle(ChatFormatting.YELLOW)
            );
        } else {
            tooltipAdder.accept(
                    Component.translatable("tooltips.bblcore.shift")
                            .withStyle(ChatFormatting.YELLOW)
            );
        }
    }
}
