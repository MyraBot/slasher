package com.github.myraBot.slasher

import com.github.myraBot.diskord.common.entities.User
import com.github.myraBot.diskord.common.entities.channel.MessageChannel
import com.github.myraBot.diskord.common.entities.guild.Member
import com.github.myraBot.diskord.common.entities.guild.SimpleGuild
import com.github.myraBot.diskord.common.entities.message.Message
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

    val channel: MessageChannel = event.channel
    val message: Message = event.message
    val user: User = event.user

    val guild: SimpleGuild = event.guild!!
    val member: Member = _member
    suspend fun getBotMember() = event.guild!!.getBotMember()
}