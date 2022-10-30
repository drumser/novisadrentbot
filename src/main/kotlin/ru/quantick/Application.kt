package ru.quantick

import com.elbekd.bot.Bot
import kotlinx.coroutines.runBlocking
import ru.quantick.database.DatabaseFactory
import ru.quantick.database.ShowedAdsDAO
import ru.quantick.service.parser.formatter.HtmlFormatter
import ru.quantick.service.parser.notifier.TelegramNotifier
import ru.quantick.service.parser.Parser
import ru.quantick.service.parser.provider.CityexpertProvider
import ru.quantick.service.parser.provider.FourZidaProvider
import ru.quantick.service.parser.provider.HalooglasiProvider
import ru.quantick.service.parser.provider.NekretnineProvider
import ru.quantick.service.parser.provider.SasomangeProvider

fun main() {
    DatabaseFactory.init()
    val scheduler = createScheduler()

    println("App started")
    runBlocking {
        scheduler.run()
    }
}

private fun createScheduler(): Scheduler {
    val dao = ShowedAdsDAO()
    val parser = Parser(
        listOf(
            CityexpertProvider(),
            FourZidaProvider(),
            HalooglasiProvider(),
            NekretnineProvider(),
            SasomangeProvider(),
        )
    )
    val configuration = ConfigurationInitiator.create()
    val notifier = TelegramNotifier(
        bot = Bot.createPolling(configuration.tgToken),
        formatter = HtmlFormatter()
    )

    return Scheduler(
        configuration = configuration,
        notifier = notifier,
        dao = dao,
        parser = parser
    )
}

