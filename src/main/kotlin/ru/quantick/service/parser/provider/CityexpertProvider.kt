package ru.quantick.service.parser.provider

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import ru.quantick.model.ProviderEnum
import ru.quantick.model.RentAd
import ru.quantick.service.HttpClient

private const val HOST_URL = "https://cityexpert.rs"


@Serializable
data class CityExpertSearchResponse(val result: List<Flat>)

@Serializable
data class Flat(
    val propId: Int,
    val location: String,
    val street: String,
    val size: Int,
    val structure: String,
    val furnished: Int,
    val firstPublished: String,
    val price: Float
)

class CityexpertProvider() : RentProvider {
    override suspend fun getLastAds(num: Int): List<RentAd> = HttpClient.client().get("$HOST_URL/api/Search") {
        url {
            parameters.append(
                "req",
                "{\"ptId\":[1],\"cityId\":2,\"rentOrSale\":\"r\",\"maxPrice\":1500,\"searchSource\":\"regular\",\"sort\":\"datedsc\"}"
            )
        }
        accept(ContentType.Application.Json)
    }.body<CityExpertSearchResponse>().result.map {
        it.location.split(", ").map { coor -> coor.toFloat() }.let { coordinate ->
            RentAd(
                source = ProviderEnum.CITYEXPERT,
                id = it.propId.toString(),
                location = it.street,
                size = it.size,
                structure = it.structure,
                furnished = it.furnished == 1,
                firstPublished = it.firstPublished,
                price = it.price
            )
        }
    }.take(num)
}
