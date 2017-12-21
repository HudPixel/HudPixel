/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/

package net.unaussprechlich.project.connect.handlers

import net.minecraft.util.ResourceLocation
import net.unaussprechlich.managedgui.lib.ManagedGuiLib

enum class ResourceHandlerConnect{

    HUDPIXEL_LOGO, DISCORD_LOGO, DISCORD_LOGO_2;

    val res = ResourceLocation(ManagedGuiLib.MODID, "/connect/$name.png")

}
