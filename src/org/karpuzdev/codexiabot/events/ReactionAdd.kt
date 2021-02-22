package org.karpuzdev.codexiabot.events

import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class ReactionAdd: ListenerAdapter() {

    override fun onMessageReactionAdd(e: MessageReactionAddEvent) {
        if (e.user?.isBot!!) return

        // mesaj proje mesajıysa react'a göre xp eklenecek falan
    }

}