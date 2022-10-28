package ru.quantick.service.provider

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import ru.quantick.model.ProviderEnum
import ru.quantick.model.RentAd

class HalooglasiProvider : RentAdInterface {
    override suspend fun getLastAds(num: Int): List<RentAd> {
        val url = "https://www.halooglasi.com/nekretnine/izdavanje-stanova/novi-sad?cena_d_to=1500&cena_d_unit=4"
        val doc = Jsoup.connect(url).userAgent("Mozilla").get()

        val items = doc.select("#ad-list-2 > div.col-md-12")

        val result = mutableListOf<RentAd>()
        for (item in items) {
            if (item.selectFirst(".row > .Standard") == null) {
                continue
            }

            kotlin.runCatching {
                val price = parsePrice(item)
                val link = parseLink(item)
                val id = item.selectFirst(".row > div:nth-child(1)")?.id() ?: link.substring(IntRange(0, 120))

                val publishDate = parsePublishDate(item)
//            val productTitle = parseProductTitle(item)
                val address = parseAddress(item)
                val size = parseSize(item)
                val structure = parseStructure(item)

                result.add(
                    RentAd(
                        source = ProviderEnum.HALOOGLASI,
                        id = id,
                        location = address,
                        size = size?.trim('m')?.toInt() ?: 0,
                        structure = structure ?: "unknown",
                        furnished = null,
                        firstPublished = publishDate ?: "",
                        price = price,
                        link = link,
                    )
                )
            }

            if (result.count() >= num) {
                break
            }
        }

        return result
    }

    private fun parseStructure(item: Element) = item
        .selectFirst(".product-features > li:nth-child(2) .value-wrapper")
        ?.html()?.substringBefore("<span")?.replace("&nbsp;", "")

    private fun parseSize(item: Element) = item
        .selectFirst(".product-features > li:nth-child(1) .value-wrapper")
        ?.html()?.replaceAfter("m", "")?.replace("&nbsp;", "")

    private fun parseAddress(item: Element) = item.select(".subtitle-places > li")
        .joinToString(" - ") {
            it.html().replace("&nbsp;", "")
        }.trimEnd('-', ' ')

    private fun parseProductTitle(item: Element) = item.selectFirst(".product-title > a")?.html()

    private fun parsePublishDate(item: Element) = item
        .selectFirst("div.pi-img-wrapper-under span.publish-date")
        ?.html()

    private fun parseLink(item: Element) = "https://www.halooglasi.com${
        item
            .selectFirst("figure.pi-img-wrapper > a")
            ?.attr("href")
    }"

    private fun parsePrice(item: Element) = (item
        .selectFirst(".central-feature span")
        ?.dataset()
        ?.let { it["value"] }?.replace(".", "")?.toFloat()
        ?: 0.0).toFloat()
}
