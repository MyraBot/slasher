package com.github.myraBot.slasher.resolvers.impl

import com.github.myraBot.slasher.CommandContext
import com.github.myraBot.slasher.MultiWord
import com.github.myraBot.slasher.resolvers.Arg
import com.github.myraBot.slasher.resolvers.Resolver

class MultiWordResolver : Resolver<MultiWord> {
    override suspend fun resolveIfNotNull(ctx: CommandContext, parameter: String): Arg<MultiWord> {
        return Arg.ofNullable(MultiWord(parameter), ctx)
    }
}