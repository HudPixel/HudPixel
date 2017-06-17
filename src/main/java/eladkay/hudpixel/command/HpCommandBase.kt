package eladkay.hudpixel.command

import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraftforge.client.ClientCommandHandler

/**
 * Created by Elad on 3/13/2017.
 */
abstract class HpCommandBase : CommandBase() {
    init {
        ClientCommandHandler.instance.registerCommand(this)
    }

    override fun canCommandSenderUseCommand(sender: ICommandSender?): Boolean {
        return true
    }
}