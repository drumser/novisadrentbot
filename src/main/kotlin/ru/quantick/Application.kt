package ru.quantick

import com.elbekd.bot.Bot
import com.elbekd.bot.model.ChatId
import com.elbekd.bot.types.ParseMode
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import ru.quantick.dao.ShowedAdsDAO
import ru.quantick.model.getIdentifier
import ru.quantick.model.getSource
import ru.quantick.service.RentService


fun main() {
    DatabaseFactory.init()
    val dao = ShowedAdsDAO()

    val token = System.getenv("TG_TOKEN")
    val bot = Bot.createPolling(token)

    val rentService = RentService()
    println("App started")

    runBlocking {
        while (true) {
            println("Start scheduling")
            var counter = 0
            for (item in rentService.getLastAds(10)) {
                if (dao.isAlreadySent(item.getIdentifier())) {
                    continue
                }

                val text = "<b>New AD:</b>\n" +
                        "<b>Source:</b> ${item.getSource()}\n" +
                        "<b>Link:</b> ${item.link}\n" +
                        "<b>Location:</b> ${item.location}\n" +
                        "<b>Size:</b> ${item.size}\n" +
                        "<b>Structure:</b> ${item.structure}\n" +
                        "<b>Furnished:</b> ${item.furnished ?: "unknown"}\n" +
                        "<b>Published at:</b> ${item.firstPublished}\n\n" +
                        "<b>PRICE: ${item.price}</b>"
                bot.sendMessage(
                    ChatId.IntegerId(-1001605224538),
                    text,
                    parseMode = ParseMode.Html
                )

                dao.addShowed(item.getIdentifier())
                counter++
            }
            println("End scheduling. New ads: ${counter}")
            delay(60000)
        }
    }
}

