package org.karpuzdev.codexiabot.commands

import me.koply.kcommando.integration.impl.jda.JDACommand
import me.koply.kcommando.internal.Commando
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.karpuzdev.codexiabot.App
import org.karpuzdev.codexiabot.util.Util

@Commando(name = "Help",
            aliases = ["help", "yardım"],
            description = "Bu komut!")
class HelpCommand: JDACommand() {

    override fun handle(e: MessageReceivedEvent): Boolean {
        e.channel.sendMessage(App.getHelpEmbed().setColor(Util.randomColor())
                .setAuthor(e.author.name, null, e.author.avatarUrl).build()).queue()
        return true
    }

}