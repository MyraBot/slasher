package com.github.myraBot.slasher.resolvers.impl

import com.github.myraBot.slasher.CommandContext
import com.github.myraBot.slasher.resolvers.Arg
import com.github.myraBot.slasher.resolvers.Resolver

class IntResolver : Resolver<Int> {
    override suspend fun resolveIfNotNull(ctx: CommandContext, parameter: String): Arg<Int> {
        return Arg.ofNullable(parameter.toIntOrNull(), ctx)
    }
}