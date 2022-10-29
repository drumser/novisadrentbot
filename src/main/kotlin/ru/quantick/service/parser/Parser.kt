package ru.quantick.service.parser

import ru.quantick.model.RentAd
import ru.quantick.service.parser.provider.RentProvider

class Parser(private val providers: List<RentProvider>) {
    suspend fun getLastAds(num: Int): List<RentAd> {
        val result = mutableListOf<RentAd>()
        for (provider in providers) {
            result.addAll(provider.getLastAds(num))
        }

        return result
    }
}
