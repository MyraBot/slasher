package com.github.myraBot.slasher.commandInfo

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Command(
    val name: String,
    val aliases: Array<String> = [],
    val description: String = "",
    val args: Array<Data> = []
)
