package ru.quantick.service.provider

import ru.quantick.model.RentAd

interface RentAdInterface {
    suspend fun getLastAds(num: Int): List<RentAd>
}
