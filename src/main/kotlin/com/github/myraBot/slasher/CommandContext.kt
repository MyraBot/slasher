package com.github.myraBot.slasher

import com.github.myraBot.diskord.common.entities.User
import com.github.myraBot.diskord.common.entities.applicationCommands.Interaction
import com.github.myraBot.diskord.common.entities.channel.TextChannel
import com.github.myraBot.diskord.common.entities.guild.Guild
import com.github.myraBot.diskord.common.entities.guild.Member
import com.github.myraBot.diskord.gateway.listeners.impl.interactions.SlashCommandEvent
import com.github.myraBot.diskord.rest.behaviors.InteractionCreateBehavior
import kotlin.reflect.KFunction

@Suppress("unused")
class CommandContext internal constructor(
        _event: SlashCommandEvent,
        _method: KFunction<*>,
        _command: CommandImpl,
        override val interaction: Interaction = _event.interaction
) : InteractionCreateBehavior {
    @Suppress("MemberVisibilityCanBePrivate")
    val event: SlashCommandEvent = _event
    val name: String = _command.name
    val method: KFunction<*> = _method

    val channel: TextChannel = event.channel
    val user: User = event.member.user

    val guild: Guild = event.guild
    val member: Member = event.member
    suspend fun getBotMember() = event.guild.getBotMember()
}