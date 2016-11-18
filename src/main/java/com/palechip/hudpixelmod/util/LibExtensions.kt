package com.palechip.hudpixelmod.util

import com.mojang.realmsclient.gui.ChatFormatting

/**
 * Created by Elad on 10/29/2016.
 */
operator infix fun String.plus(other: ChatFormatting) = "$this$other"
operator infix fun ChatFormatting.plus(other: String) = "$this$other"