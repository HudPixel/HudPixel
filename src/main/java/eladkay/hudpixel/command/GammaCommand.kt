package eladkay.hudpixel.command

import eladkay.hudpixel.util.ChatMessageComposer
import eladkay.hudpixel.util.plus
import net.minecraft.client.Minecraft
import net.minecraft.command.ICommandSender
import net.minecraft.util.EnumChatFormatting

/**
 * Created by Elad on 6/17/2017.
 */
object GammaCommand : HpCommandBase() {
    /**
     * Callback when the command is invoked
     */
    override fun processCommand(sender: ICommandSender, args: Array<out String>) {
        if(args.size != 1) {
            ChatMessageComposer(EnumChatFormatting.RED + getCommandUsage(sender)).send()
        } else {
            val arg0 = args[0].toIntOrNull()
            if(arg0 == null) {
                ChatMessageComposer(EnumChatFormatting.RED + getCommandUsage(sender)).send()
            } else {
                ChatMessageComposer(EnumChatFormatting.GREEN + "Set gamma to %$arg0").send()
                Minecraft.getMinecraft().gameSettings.gammaSetting = arg0 / 100f
            }
        }
    }

    /**
     * Gets the name of the command
     */
    override fun getCommandName(): String {
        return "gamma"
    }

    /**
     * Gets the usage string for the command.
     */
    override fun getCommandUsage(sender: ICommandSender?): String {
        return "/gamma <percent of gamma>"
    }
}