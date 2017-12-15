/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/

package net.unaussprechlich.managedgui.lib.databases.player

import net.minecraft.util.ResourceLocation
import net.unaussprechlich.managedgui.lib.databases.player.data.Rank
import net.unaussprechlich.managedgui.lib.handler.ResourceHandler
import net.unaussprechlich.managedgui.lib.util.resolver.NameResolver
import net.unaussprechlich.managedgui.lib.util.resolver.PlayerHeadResolver
import java.util.*

/**
 * PlayerModel Created by Alexander on 27.02.2017.
 * Description:
 */
class PlayerModel(val nameHistory : NameResolver.NameHistory, val uuid: UUID) {

    val name : String = nameHistory.currentName

    var rank : Rank? = null

    var playerHead : ResourceLocation = ResourceHandler.STEVE_HEAD.res
        private set


    var rankName: String = ""
        get() {
            if (rank!!.rankFormatted.equals(rank!!.rankColor.toString(), ignoreCase = true)) return rank!!.rankColor.toString() + name
            return rank!!.rankFormatted + " " + nameHistory.currentName
        }

    fun loadPlayerHead(callback: (ResourceLocation) -> Unit){
        if(playerHead == ResourceHandler.STEVE_HEAD.res)
            callback.invoke(playerHead)
        else PlayerHeadResolver(name){ res ->
            playerHead = res ?: ResourceHandler.STEVE_HEAD.res
            callback.invoke(playerHead)
        }
    }


}
