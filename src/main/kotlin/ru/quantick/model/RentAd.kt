package ru.quantick.model

enum class ProviderEnum {
    CITYEXPERT, FOUR_ZIDA
}

data class RentAd(
    val source: ProviderEnum,
    val id: String,
    val location: String,
    val size: Int,
    val structure: String,
    val furnished: Boolean,
    val firstPublished: String,
    val price: Float,
    val link: String? = null,
)

fun RentAd.getIdentifier() = "${this.source}_${this.id}"
