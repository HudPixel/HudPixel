/*##############################################################################

           Copyright © 2016-2017 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/
/*##############################################################################

           Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED

 #############################################################################*/
/*******************************************************************************
 *
 *
 *           Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 *
 ******************************************************************************/

import eladkay.hudpixel.command.HpCommandBase
import net.minecraft.command.ICommandSender

/**
 * Created by Elad on 3/2/2017.
 */
object TestCommand : HpCommandBase() {

    override fun canCommandSenderUseCommand(sender: ICommandSender?): Boolean {
        return true
    }

    override fun processCommand(sender: ICommandSender?, args: Array<out String>?) {



    }

    override fun getCommandName(): String {
        return "test"
    }

    override fun getCommandUsage(sender: ICommandSender?): String {
        return "/test [msg]"
    }
}