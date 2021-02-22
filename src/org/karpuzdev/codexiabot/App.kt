package org.karpuzdev.codexiabot

import me.koply.kcommando.CommandToRun
import me.koply.kcommando.KCommando
import me.koply.kcommando.integration.impl.jda.JDAIntegration
import net.dv8tion.jda.api.JDABuilder
import org.karpuzdev.codexiabot.commands.PingCommand
import org.karpuzdev.codexiabot.events.BasicEvents

import me.koply.kcommando.Parameters
import me.koply.kcommando.KInitializer
import net.dv8tion.jda.api.EmbedBuilder
import org.karpuzdev.codexiabot.data.DataManager
import org.karpuzdev.codexiabot.events.ReactionAdd
import org.karpuzdev.codexiabot.util.Util
import java.io.File

object App {

    private val params = Parameters()
    private val initializer = KInitializer(params)

    @JvmStatic
    fun main(args : Array<String>) {
        val map = System.getenv()

        if (!map.containsKey("token")) {
            println("Token env key is not found. Project is stoppink!1!")
            return
        }

        val jda = JDABuilder.createDefault(map["token"]).setAutoReconnect(true).build()
        jda.awaitReady()

        val dataFile = File("./data.json")

        KCommando(JDAIntegration(jda), initializer)
                .setPrefix(".")
                .setPackage(PingCommand::class.java.`package`.name)
                .setOwners("269140308208517130", "291168238140653578")
                .setCooldown(1000L)
                .setDataManager(DataManager.ManagerWrapper(dataFile, params)).build()

        jda.addEventListener(BasicEvents(), ReactionAdd())

        makeEmbed(params.commandMethods)
    }

    private val internalEmbed = EmbedBuilder()
    fun getHelpEmbed(): EmbedBuilder = EmbedBuilder(internalEmbed)

    private fun makeEmbed(map: MutableMap<String, CommandToRun>) {
        val sb = StringBuilder()
        val set = HashSet<CommandToRun>()

        for ((_,v) in map) {
            if (set.contains(v)) continue
            set.add(v)
            val aliases: Array<String> = v.clazz.info.aliases
            sb.append("`").append(Util.getArrayAsString(aliases, " - ")).append("` -> ").append(v.clazz.info.description).append("\n")
        }

        internalEmbed.addField("❯ Komutlar", sb.toString(), false)
                .addField("❯ Linkler", "[KCommando](https://github.com/MusaBrt/KCommando)", false)
    }
}