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
    val configuration = ConfigurationInitiator.create()
    val notifier = TelegramNotifier(
        bot = Bot.createPolling(configuration.tgToken),
        formatter = HtmlFormatter()
    )

    val parser = Parser(
        providers = listOf(
            CityexpertProvider(),
            FourZidaProvider(),
            HalooglasiProvider(),
            NekretnineProvider(),
            SasomangeProvider(),
        ),
        dao = dao,
        notifier = notifier,
        configuration = configuration
    )

    return Scheduler(
        configuration = configuration,
        parser = parser
    )
}

