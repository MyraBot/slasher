package com.github.myraBot.slasher.resolvers

import com.github.myraBot.slasher.CommandContext

interface Resolver<T> {
    suspend fun resolve(ctx: CommandContext, parameter: String?): Arg<T> {
        return if (parameter == null) Arg.ofNotGiven(ctx)
        else resolveIfNotNull(ctx, parameter)
    }

    suspend fun resolveIfNotNull(ctx: CommandContext, parameter: String): Arg<T>
}