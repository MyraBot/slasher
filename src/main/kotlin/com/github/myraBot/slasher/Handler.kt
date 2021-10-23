@file:Suppress("unused")

package com.github.myraBot.slasher

import com.github.myraBot.slasher.commandInfo.Command
import com.github.myraBot.slasher.resolvers.Arg
import com.github.myraBot.slasher.resolvers.Resolvers
import com.github.myraBot.diskord.common.entities.Guild
import com.github.myraBot.diskord.gateway.listeners.EventListener
import com.github.myraBot.diskord.gateway.listeners.ListenTo
import com.github.myraBot.diskord.gateway.listeners.impl.MessageCreateEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.reflections.Reflections
import kotlin.reflect.KFunction
import kotlin.reflect.full.*

class Handler : EventListener() {
    private val cogs: MutableList<Cog> = mutableListOf()

    lateinit var coroutineScope: CoroutineScope
    var commandPackage: String? = null
    var defaultPrefixes: MutableList<String> = mutableListOf()
    var guildPrefixes: (suspend (Guild) -> MutableList<String>)? = null
    var ignoreSystemMessages: Boolean = false
    var ignoreBotMessages: Boolean = false

    @ListenTo(MessageCreateEvent::class)
    fun onMessage(event: MessageCreateEvent) {
        coroutineScope.launch { handle(event) }
    }

    private suspend fun handle(event: MessageCreateEvent) {
        if (ignoreSystemMessages && (event.message.isWebhook || event.user.system)) return
        if (ignoreBotMessages && event.user.bot) return
        if (event.member == null) {
            //TODO Handle null members
            throw Exception("The member is null... Sorry lol")
        }
        val member = event.member!!
        val guild = event.guild!!

        val prefixes = guildPrefixes?.invoke(guild) ?: defaultPrefixes
        val prefix: String = prefixes.firstOrNull { event.message.content.startsWith(it) } ?: return

        this.cogs.flatMap { it.commands }.forEach { command ->
            val messageWithoutPrefix: String = event.message.content.substring(prefix.length)

            val executor: String = mutableListOf(command.name, *command.aliases)
                .filter { it.length <= messageWithoutPrefix.length }
                .firstOrNull { it.equals(messageWithoutPrefix.substring(0, it.length), ignoreCase = true) } ?: return@forEach

            val commandArguments: String? = if (messageWithoutPrefix.length == executor.length) null else messageWithoutPrefix.substring(executor.length + 1)
            val args: MutableList<String> = commandArguments?.split("\\s+".toRegex())?.toMutableList() ?: mutableListOf()
            args.removeAll { it.isBlank() }

            val cog: Cog = this.cogs.first { command in it.commands }
            coroutineScope.launch {
                try {
                    val ctx = CommandContext(event, command.method, command, executor, member)

                    val resolvedArgs: List<Arg<*>> = command.method.valueParameters
                        .subList(1, command.method.valueParameters.size) // Don't resolve CommandContext parameter
                        .mapIndexed { index, parameter ->
                            Resolvers.resolve(ctx, parameter, args.getOrNull(index), commandArguments)
                        }

                    command.method.callSuspend(cog, ctx, *resolvedArgs.toTypedArray())
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun loadCogs() {
        if (this.commandPackage == null) throw IllegalStateException("Command package is not set!")
        if (!this::coroutineScope.isInitialized) throw IllegalStateException("No coroutine scope is set!")

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
        return CommandImpl(commandInfo.name, commandInfo.aliases, commandInfo.args, commandInfo.description, method)
    }
}

fun handler(handler: Handler.() -> Unit) = Handler().apply(handler)