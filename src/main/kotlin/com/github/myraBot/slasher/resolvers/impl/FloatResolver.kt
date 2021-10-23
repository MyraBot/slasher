package com.github.myraBot.slasher.resolvers.impl

import com.github.myraBot.slasher.CommandContext
import com.github.myraBot.slasher.resolvers.Arg
import com.github.myraBot.slasher.resolvers.Resolver

class FloatResolver : Resolver<Float> {
    override suspend fun resolveIfNotNull(ctx: CommandContext, parameter: String): Arg<Float> {
        return Arg.ofNullable(parameter.replace(",", ".").toFloatOrNull(), ctx)
    }
}