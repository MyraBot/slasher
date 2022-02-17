@file:Suppress("unused")

package com.github.myraBot.slasher

import bot.myra.kommons.trace
import com.github.myraBot.diskord.common.entities.Role
import com.github.myraBot.diskord.common.entities.User
import com.github.myraBot.diskord.common.entities.applicationCommands.slashCommands.SlashCommandOptionType
import com.github.myraBot.diskord.common.entities.channel.ChannelData
import com.github.myraBot.diskord.common.entities.guild.Member
import com.github.myraBot.diskord.gateway.listeners.EventListener
import com.github.myraBot.diskord.gateway.listeners.ListenTo
import com.github.myraBot.diskord.gateway.listeners.impl.interactions.slashCommands.GuildSlashCommandEvent
import org.reflections.Reflections
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.*

class Handler : EventListener {
    private val cogs: MutableList<Cog> = mutableListOf()
    var commandPackage: String? = null

    @ListenTo(GuildSlashCommandEvent::class)
    suspend fun onMessage(event: GuildSlashCommandEvent) = handle(event)

    private suspend fun handle(event: GuildSlashCommandEvent) {
        val name = StringBuilder(event.command.name).apply {
            event.command.options.forEach {
                if (it.type == SlashCommandOptionType.SUB_COMMAND || it.type == SlashCommandOptionType.SUB_COMMAND_GROUP) {
                    this.append(" ${it.name}")
                }
            }
        }.toString()

        this.cogs
            .flatMap { it.commands }
            .filter { it.name == name }
            .forEach { command ->
                val cog: Cog = this@Handler.cogs.first { command in it.commands }
                val args: List<Any?> = command.method.valueParameters
                    .subList(1, command.method.valueParameters.size) // Don't resolve CommandContext parameter
                    .map { param ->
                        val option = event.command.options.firstOrNull { it.name == param.name } ?: return@map null
                        val klass = param.type.classifier as KClass<*>
                        resolveParam(klass, option.name, event)
                    }

                command.method.callSuspend(cog, CommandContext(event.interaction, command.method, command), *args.toTypedArray())
            }
    }

    private fun resolveParam(klass: KClass<*>, name: String, event: GuildSlashCommandEvent): Any? = when (klass) {
        String::class -> event.getOption<String>(name)
        Int::class -> event.getOption<Int>(name)
        Boolean::class -> event.getOption<Boolean>(name)
        User::class -> event.getOption<User>(name)
        Member::class -> event.getOption<Member>(name)
        ChannelData::class -> event.getOption<ChannelData>(name)
        Role::class -> event.getOption<Role>(name)
        Unit::class -> event.getOption<String>(name)
        Long::class -> event.getOption<Long>(name)
        else -> throw IllegalStateException("Unexpected option type")
    }


    fun loadCogs() {
        if (this.commandPackage == null) throw IllegalStateException("Command package is not set!")

        Reflections(commandPackage).getSubTypesOf(Cog::class.java)
            .filter { !it.isInterface }
            .map { it.kotlin.objectInstance }
            .forEach { cog ->
                if (cog == null) throw IllegalStateException("Make sure all command listeners are objects!")
                this.cogs.add(cog)
                loadCommands(cog)
            }
    }

    private fun loadCommands(cog: Cog) {
        cog::class.functions.filter { it.hasAnnotation<Command>() }
            .mapNotNull { loadCommand(it) }
            .let { cog.commands.addAll(it) }
    }

    private fun loadCommand(method: KFunction<*>): CommandImpl? {
        if (method.valueParameters.firstOrNull()?.type?.classifier != CommandContext::class) return null
        val commandInfo = method.findAnnotation<Command>() ?: return null
        return CommandImpl(commandInfo.name, method).also {
            trace(this::class) { "Registered command: ${commandInfo.name}" }
        }
    }
}

fun handler(handler: Handler.() -> Unit) = Handler().apply(handler)