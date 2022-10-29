package ru.quantick.service.parser.provider

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.quantick.model.ProviderEnum
import ru.quantick.model.RentAd
import ru.quantick.service.HttpClient

private const val HOST = "https://api.4zida.rs"

@Serializable
data class FourZidaSearchResponse(
    val total: Int,
    val ads: List<FourZidaAd>
)

@Serializable
data class FourZidaAd(
    @SerialName("m2")
    val size: Int,
    val furnished: String? = null,
    val id: String,
    val price: Float,
    val address: String? = null,
    val roomCount: Float,
    val structureAbbreviation: String,
    val createdAt: String,
    val urlPath: String? = null,
)

class FourZidaProvider : RentProvider {
    override suspend fun getLastAds(num: Int): List<RentAd> {
        val response = kotlin.runCatching {
            HttpClient
                .client()
                .get("$HOST/v6/search/apartments?for=rent&priceTo=1500&sort=createdAtDesc&page=1&placeIds[]=600")
                .body<FourZidaSearchResponse>()
        }.getOrElse { FourZidaSearchResponse(0, emptyList()) }

        return response.ads.map {
            RentAd(
                source = ProviderEnum.FOUR_ZIDA,
                id = it.id,
                location = it.address ?: "",
                size = it.size,
                structure = it.structureAbbreviation,
                furnished = it.furnished == "yes",
                firstPublished = it.createdAt,
                price = it.price,
                link = if (it.urlPath != null) "https://4zida.rs${it.urlPath}" else null
            )
        }.take(num)
    }
}
