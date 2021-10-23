package com.github.myraBot.slasher.resolvers.impl

import com.github.myraBot.slasher.CommandContext
import com.github.myraBot.slasher.resolvers.Arg
import com.github.myraBot.slasher.resolvers.Resolver

class DoubleResolver : Resolver<Double> {
    override suspend fun resolveIfNotNull(ctx: CommandContext, parameter: String): Arg<Double> {
        return Arg.ofNullable(parameter.replace(",", ".").toDoubleOrNull(), ctx)
    }
}