package com.github.myraBot.slasher.resolvers.impl

import com.github.myraBot.diskord.common.entities.User
import com.github.myraBot.slasher.CommandContext
import com.github.myraBot.slasher.DiscordRegex
import com.github.myraBot.slasher.resolvers.Arg
import com.github.myraBot.slasher.resolvers.Resolver

class UserResolver : Resolver<User> {
    override suspend fun resolveIfNotNull(ctx: CommandContext, parameter: String): Arg<User> {
        return when {
            parameter.matches(DiscordRegex.id) || parameter.matches(DiscordRegex.userMention) -> {
                val id = parameter.removePrefix("<@").removePrefix("!").removeSuffix(">")
                Arg.ofNullable(ctx.diskord.getUser(id), ctx)
            }
            // TODO
            /*
            parameter.matches(DiscordRegex.userAsTag) -> Arg.ofNullable(ctx.bot.userCache.find {
                val userName = parameter.split("#")[0]
                val userDiscriminator = parameter.split("#")[1]

                it.name == userName && it.discriminator == userDiscriminator
            }, ctx)
            parameter.matches(DiscordRegex.userName) -> Arg.ofNullable(ctx.bot.userCache.find { it.name == parameter }, ctx)
            */
            else -> Arg.ofNotFound(ctx)
        }
    }
}