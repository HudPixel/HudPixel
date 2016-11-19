package com.palechip.hudpixelmod.util

import com.mojang.realmsclient.gui.ChatFormatting
import net.minecraft.util.text.TextFormatting

/**
 * Created by Elad on 10/29/2016.
 */
operator infix fun String.plus(other: ChatFormatting) = "$this$other"
operator infix fun ChatFormatting.plus(other: String) = "$this$other"
operator infix fun String.plus(other: TextFormatting) = "$this$other"
operator infix fun TextFormatting.plus(other: String) = "$this$other"
operator infix fun String.plus(other: Int) = "$this$other"
operator infix fun Int.plus(other: String) = "$this$other"