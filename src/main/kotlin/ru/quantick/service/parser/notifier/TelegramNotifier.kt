package ru.quantick.service.parser.notifier

import com.elbekd.bot.Bot
import com.elbekd.bot.model.ChatId
import com.elbekd.bot.types.ParseMode
import ru.quantick.Configuration
import ru.quantick.model.RentAd
import ru.quantick.service.parser.formatter.HtmlFormatter

class TelegramNotifier(
    private val bot: Bot,
    private val formatter: HtmlFormatter,
) : Notifier {
    override suspend fun notify(channelId: String, rentAd: RentAd) {
        bot.sendMessage(
            chatId = ChatId.IntegerId(channelId.toLong()),
            text = formatter.format(rentAd),
            parseMode = ParseMode.Html
        )
    }
}
