package com.github.myraBot.slasher.resolvers.impl

import com.github.myraBot.slasher.CommandContext
import com.github.myraBot.slasher.DiscordRegex
import com.github.myraBot.slasher.resolvers.Arg
import com.github.myraBot.slasher.resolvers.Resolver
import com.github.myraBot.diskord.common.entities.Member

class MemberResolver : Resolver<Member> {
    override suspend fun resolveIfNotNull(ctx: CommandContext, parameter: String): Arg<Member> {
        return when {
            parameter.matches(DiscordRegex.id) || parameter.matches(DiscordRegex.userMention) -> {
                val id = parameter.removePrefix("<@").removePrefix("!").removeSuffix(">")
                Arg.ofNullable(ctx.guild.getMember(id), ctx)
            }
            // TODO
            /*
            parameter.matches(DiscordRegex.userAsTag) -> Arg.ofNullable(ctx.guild.memberCache.find {
                val userName = parameter.split("#")[0]
                val userDiscriminator = parameter.split("#")[1]

                val nameMatches = it.effectiveName == userName || it.user.name == userName
                nameMatches && it.user.discriminator == userDiscriminator
            }, ctx)
            parameter.matches(DiscordRegex.userName) -> Arg.ofNullable(value = ctx.guild.memberCache.find { it.effectiveName == parameter || it.user.name == parameter }, ctx)
            */
            else -> Arg.ofNotFound(ctx)
        }
    }
}