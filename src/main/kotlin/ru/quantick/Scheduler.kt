package ru.quantick

import kotlinx.coroutines.delay
import ru.quantick.service.parser.Parser

class Scheduler(
    private val configuration: Configuration,
    private val parser: Parser,
) {
    suspend fun run() {
        while (true) {
            println("Start scheduling")
            parser.handle(
                num = configuration.lastAdsLimit,
            )
            println("End scheduling")
            delay(60000)
        }
    }
}
