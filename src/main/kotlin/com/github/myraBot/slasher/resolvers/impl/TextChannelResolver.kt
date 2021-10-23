package com.github.myraBot.slasher.resolvers.impl

import com.github.myraBot.slasher.CommandContext
import com.github.myraBot.slasher.DiscordRegex
import com.github.myraBot.slasher.resolvers.Arg
import com.github.myraBot.slasher.resolvers.Resolver
import com.github.myraBot.diskord.common.entities.channel.TextChannel

class TextChannelResolver : Resolver<TextChannel> {
    override suspend fun resolveIfNotNull(ctx: CommandContext, parameter: String): Arg<TextChannel> {
        return when {
            parameter.matches(DiscordRegex.id) || parameter.matches(DiscordRegex.textChannelMention) -> {
                val id = parameter.removePrefix("<#").removeSuffix(">")
                Arg.ofNullable(ctx.guild.getChannel<TextChannel>(id), ctx)
            }
            //TODO else -> Arg.ofNullable(ctx.guild.textChannels.find { it.name.contains(parameter, ignoreCase = true) }, ctx)
            else -> Arg.ofNotFound(ctx)
        }
    }
}