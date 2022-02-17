package com.github.myraBot.slasher

import com.github.myraBot.diskord.common.entities.applicationCommands.Interaction
import com.github.myraBot.diskord.common.entities.guild.Guild
import com.github.myraBot.diskord.common.entities.guild.Member
import com.github.myraBot.diskord.gateway.listeners.impl.interactions.slashCommands.GuildSlashCommandEvent
import com.github.myraBot.diskord.rest.behaviors.InteractionCreateBehavior
import kotlinx.coroutines.runBlocking
import kotlin.reflect.KFunction

@Suppress("unused")
class CommandContext internal constructor(
    data: Interaction,
    val method: KFunction<*>,
    private val _command: CommandImpl,
) : GuildSlashCommandEvent(data), InteractionCreateBehavior {
    val guild: Guild get() = runBlocking { super.getGuild().awaitNonNull() }
    val botMember: Member get() = runBlocking { guild.getBotMember().awaitNonNull() }
}