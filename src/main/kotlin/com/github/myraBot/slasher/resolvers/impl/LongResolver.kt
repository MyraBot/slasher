package com.github.myraBot.slasher.resolvers.impl

import com.github.myraBot.slasher.CommandContext
import com.github.myraBot.slasher.resolvers.Arg
import com.github.myraBot.slasher.resolvers.Resolver

class LongResolver : Resolver<Long> {
    override suspend fun resolveIfNotNull(ctx: CommandContext, parameter: String): Arg<Long> {
        return Arg.ofNullable(parameter.toLongOrNull(), ctx)
    }
}