package ru.quantick.service

import ru.quantick.model.RentAd
import ru.quantick.service.provider.CityexpertProvider
import ru.quantick.service.provider.FourZidaProvider

class RentService {
    private val providers = listOf(
        CityexpertProvider(),
        FourZidaProvider()
    )
    suspend fun getLastAds(num: Int): List<RentAd> {
        val result = mutableListOf<RentAd>()
        for (provider in providers) {
            result.addAll(provider.getLastAds(num))
        }

        return result
    }
}