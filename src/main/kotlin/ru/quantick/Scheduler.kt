package ru.quantick

import kotlinx.coroutines.delay
import ru.quantick.database.ShowedAdsDAO
import ru.quantick.service.parser.notifier.Notifier
import ru.quantick.service.parser.Parser

class Scheduler(
    private val configuration: Configuration,
    private val notifier: Notifier,
    private val dao: ShowedAdsDAO,
    private val parser: Parser,
) {
    suspend fun run() {
        while (true) {
            println("Start scheduling")
            parser.getLastAds(configuration.lastAdsLimit)
                .forEach {
                    dao.handle(it) {
                        notifier.notify(
                            channelId = configuration.tgChannelId.toString(),
                            rentAd = it
                        )
                    }
                }

            println("End scheduling")
            delay(60000)
        }
    }
}
