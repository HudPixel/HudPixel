/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/

package net.unaussprechlich.managedgui.lib.handler

import net.minecraft.util.ResourceLocation
import net.unaussprechlich.managedgui.lib.ManagedGuiLib

enum class ResourceHandler{

    STEVE_HEAD;

    val res = ResourceLocation(ManagedGuiLib.MODID, "/managedgui/$name.png")

}
