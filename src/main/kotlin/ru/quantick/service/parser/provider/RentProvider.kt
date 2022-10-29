package ru.quantick.service.parser.provider

import ru.quantick.model.RentAd

interface RentProvider {
    suspend fun getLastAds(num: Int): List<RentAd>
}
