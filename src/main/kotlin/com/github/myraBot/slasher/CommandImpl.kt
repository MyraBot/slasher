package com.github.myraBot.slasher

import kotlin.reflect.KFunction

@Suppress("ArrayInDataClass")
internal data class CommandImpl(val name: String, val method: KFunction<*>)