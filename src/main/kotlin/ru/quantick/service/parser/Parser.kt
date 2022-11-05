package ru.quantick.service.parser

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.quantick.Configuration
import ru.quantick.database.ShowedAdsDAO
import ru.quantick.service.parser.notifier.Notifier
import ru.quantick.service.parser.provider.RentProvider

class Parser(
    private val providers: List<RentProvider>,
    private val dao: ShowedAdsDAO,
    private val notifier: Notifier,
    private val configuration: Configuration,
) {
    suspend fun handle(
        num: Int,
    ) = runBlocking {
        providers.forEach {
            launch {
                it.getLastAds(num).forEach {
                    dao.handle(it) {
                        notifier.notify(
                            channelId = configuration.tgChannelId.toString(),
                            rentAd = it
                        )
                        println("${it.source}[${it.id}] is new. Notified!")
                    }
                }
            }
        }
    }
}
