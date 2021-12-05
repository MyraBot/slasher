package com.github.myraBot.slasher

import com.github.myraBot.diskord.common.entities.User
import com.github.myraBot.diskord.common.entities.applicationCommands.Interaction
import com.github.myraBot.diskord.common.entities.applicationCommands.slashCommands.SlashCommand
import com.github.myraBot.diskord.common.entities.channel.TextChannel
import com.github.myraBot.diskord.common.entities.guild.Guild
import com.github.myraBot.diskord.common.entities.guild.Member
import com.github.myraBot.diskord.gateway.listeners.impl.interactions.SlashCommandEvent
import com.github.myraBot.diskord.rest.behaviors.InteractionCreateBehavior
import kotlin.reflect.KFunction

@Suppress("unused")
class CommandContext internal constructor(
        val event: SlashCommandEvent,
        val method: KFunction<*>,
        private val _command: CommandImpl,
) : InteractionCreateBehavior {
    override val interaction: Interaction = event.interaction
    val command: SlashCommand = event.command

    val channel: TextChannel = event.channel
    val user: User = event.member.user

    val guild: Guild = event.guild
    val member: Member = event.member
    suspend fun getBotMember() = event.guild.getBotMember()
}