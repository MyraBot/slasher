package com.github.myraBot.slasher

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Command(
    val name: String
)
