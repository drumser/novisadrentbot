package ru.quantick.service.parser.notifier

import ru.quantick.model.RentAd

interface Notifier {
    suspend fun notify(channelId: String, rentAd: RentAd)
}
