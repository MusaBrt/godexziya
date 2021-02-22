package org.karpuzdev.codexiabot.commands

import me.koply.kcommando.integration.impl.jda.JDACommand
import me.koply.kcommando.internal.Commando
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

@Commando(name = "Ping!",
        aliases = ["ping"],
        description="Pong!")
class PingCommand : JDACommand() {

    override fun handle(e: MessageReceivedEvent): Boolean {
        e.textChannel.sendMessage("Pong!").queue()
        return true
    }
}