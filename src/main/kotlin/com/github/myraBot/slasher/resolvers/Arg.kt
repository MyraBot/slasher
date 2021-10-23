package com.github.myraBot.slasher.resolvers

import com.github.m5rian.discord.objects.entities.User
import com.github.myraBot.slasher.CommandContext
import com.github.myraBot.diskord.common.entities.Member

data class Arg<T>(internal val _value: T?, val found: Boolean, val given: Boolean, internal val ctx: CommandContext) {
    val value: T get() = _value!!

    companion object {
        fun <T> ofNullable(value: T?, ctx: CommandContext): Arg<T> {
            return if (value == null) Arg(_value = value, found = false, given = true, ctx = ctx)
            else Arg(_value = value, found = true, given = true, ctx = ctx)
        }
        fun <T> ofNotFound(ctx: CommandContext): Arg<T> = Arg(_value = null, found = false, given = true, ctx = ctx)
        fun <T> ofNotGiven(ctx: CommandContext): Arg<T> = Arg(_value = null, found = false, given = false, ctx = ctx)
    }
}

fun Arg<User>.getOrDefault(): User = this._value ?: this.ctx.user
fun Arg<Member>.getOrDefault(): Member = this._value ?: this.ctx.member
