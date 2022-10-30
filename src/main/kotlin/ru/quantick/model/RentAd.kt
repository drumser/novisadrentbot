package ru.quantick.model

enum class ProviderEnum {
    CITYEXPERT, FOUR_ZIDA, HALOOGLASI, NEKRETNINE, SASOMANGE
}

val sourceMap = mapOf(
    ProviderEnum.CITYEXPERT to "Cityexpert.rs",
    ProviderEnum.FOUR_ZIDA to "4zida.rs",
    ProviderEnum.HALOOGLASI to "halooglasi.com",
    ProviderEnum.NEKRETNINE to "nekretnine.rs",
    ProviderEnum.SASOMANGE to "sasomange.rs",
)

data class RentAd(
    val source: ProviderEnum,
    val id: String,
    val location: String,
    val size: Int,
    val structure: String?,
    val furnished: Boolean?,
    val firstPublished: String,
    val price: Float,
    val link: String? = null,
)

fun RentAd.getIdentifier() = "${this.source}_${this.id}"
fun RentAd.getSource() = sourceMap[this.source]
