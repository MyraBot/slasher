package com.github.myraBot.slasher.commandInfo

import kotlin.reflect.KClass

annotation class Data(
    val optional: Boolean,
    val type: KClass<*>,
    val description: String = ""
)