package org.tamzi.utils

import kotlinx.datetime.*
import kotlinx.serialization.Serializable
import kotlinx.coroutines.*

fun main() {
    Printer("Hello, world!").printMessage()
    Printer("this is utils!").printMessage()
}


@Serializable
class Printer(val message: String) {
    fun printMessage() = runBlocking {
        val now: Instant = Clock.System.now()
        launch {
            delay(1000L)
            println(now.toString())
        }
        println(message)
    }
}