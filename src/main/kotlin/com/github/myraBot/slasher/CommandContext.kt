package com.github.myraBot.slasher

import com.github.myraBot.diskord.common.entities.*
import com.github.myraBot.diskord.common.entities.channel.MessageChannel
import com.github.myraBot.diskord.gateway.listeners.impl.MessageCreateEvent
import com.github.myraBot.slasher.commandInfo.Data
import kotlin.reflect.KFunction

@Suppress("unused")
class CommandContext internal constructor(
        _event: MessageCreateEvent,
        _method: KFunction<*>,
        _command: CommandImpl,
        _executor: String,
        _member: Member,
) {
    @Suppress("MemberVisibilityCanBePrivate")
    val event: MessageCreateEvent = _event
    val name: String = _command.name
    val aliases: Array<String> = _command.aliases
    var args: Array<Data> = _command.args
    val description: String = _command.description
    val executor: String = _executor
    val method: KFunction<*> = _method

    val diskord: Diskord = Diskord

    val guild: Guild = event.guild!!
    val member: Member = _member
    val channel: MessageChannel = event.channel
    val message: Message = event.message

    val user: User = event.user
}