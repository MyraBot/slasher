package com.github.myraBot.slasher

import com.github.m5rian.discord.objects.entities.User
import com.github.myraBot.slasher.commandInfo.Data
import com.github.myraBot.diskord.common.entities.Application
import com.github.myraBot.diskord.common.entities.Guild
import com.github.myraBot.diskord.common.entities.Member
import com.github.myraBot.diskord.common.entities.Message
import com.github.myraBot.diskord.common.entities.channel.TextChannel
import com.github.myraBot.diskord.gateway.listeners.impl.MessageCreateEvent
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

    suspend fun bot(): Application = event.bot()
    suspend fun botMember(): Member? = event.guild?.botMember()

    val guild: Guild = event.guild!!
    val member: Member = _member
    suspend fun channel(): TextChannel = event.channel()
    val message: Message = event.message

    val user: User = event.user
}