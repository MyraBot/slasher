package com.github.myraBot.slasher

import com.github.myraBot.slasher.commandInfo.Data
import kotlin.reflect.KFunction

@Suppress("ArrayInDataClass")
internal data class CommandImpl(val name: String, val aliases: Array<String>, val args: Array<Data>, val description: String, val method: KFunction<*>)