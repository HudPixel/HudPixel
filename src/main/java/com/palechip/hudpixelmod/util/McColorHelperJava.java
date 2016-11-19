package com.palechip.hudpixelmod.util;

import net.minecraft.util.text.TextFormatting;

import static net.minecraft.util.text.TextFormatting.DARK_GRAY;
import static net.minecraft.util.text.TextFormatting.DARK_GREEN;
import static net.minecraft.util.text.TextFormatting.DARK_RED;

public interface McColorHelperJava {
    /**
     * A class can just extend another class .... so I used a interface to ensure
     * there will be no conflict while extending
     */

    TextFormatting GOLD = TextFormatting.GOLD;
    TextFormatting WHITE = TextFormatting.WHITE;
    TextFormatting RED = TextFormatting.RED;
    TextFormatting BLUE = TextFormatting.BLUE;
    TextFormatting D_RED = DARK_RED;
    TextFormatting GRAY = TextFormatting.GRAY;
    TextFormatting GREEN = TextFormatting.GREEN;
    TextFormatting D_GRAY = DARK_GRAY;
    TextFormatting YELLOW = TextFormatting.YELLOW;
    TextFormatting OBFUSCATED = TextFormatting.OBFUSCATED;
    TextFormatting ITALIC = TextFormatting.ITALIC;
    TextFormatting AQUA = TextFormatting.AQUA;
    TextFormatting D_GREEN = DARK_GREEN;
}
