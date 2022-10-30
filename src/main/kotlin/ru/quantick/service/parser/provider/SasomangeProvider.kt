package ru.quantick.service.parser.provider

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import ru.quantick.model.ProviderEnum
import ru.quantick.model.RentAd

class SasomangeProvider : RentProvider {
    override suspend fun getLastAds(num: Int): List<RentAd> {
        val url =
            "https://sasomange.rs/c/stanovi-iznajmljivanje/f/novi-sad?productsFacets.facets=priceValue:(*-1500),"
        val doc = Jsoup.connect(url).userAgent("Mozilla").get()

        val items = doc.select(".js-grid-view-item li.product-single-item:not(.promotion)")

        val result = mutableListOf<RentAd>()
        for (item in items) {
            kotlin.runCatching {
                val price = parsePrice(item)
                val link = parseLink(item)
                val id = parseId(link) ?: return@runCatching
                val publishDate = parsePublishDate(item)
                val address = parseAddress(item)
                val size = parseSize(item)

                result.add(
                    RentAd(
                        source = ProviderEnum.SASOMANGE,
                        id = id,
                        location = address ?: "",
                        size = size,
                        structure = null,
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

    private fun parseId(link: String) =
        "/p/(\\d+).*".toRegex().find(link)?.groupValues?.get(1)

    private fun parseStructure(detailDoc: Document) =
        detailDoc
            .selectFirst(".property__main-details > ul > li:nth-child(2) > span")
            ?.html()
            ?.substringAfter("br>")

    private fun parseAddress(item: Element) =
        item.selectFirst(".top-section .pin-item > span")?.html()

    private fun parsePublishDate(item: Element) = item
        .selectFirst("time.pin-item > span")
        ?.html()

    private fun parseLink(item: Element) = "https://sasomange.rs${
        item
            .selectFirst("a.product-link")
            ?.attr("href")
    }"

    private fun parseSize(item: Element) = item
        .selectFirst("ul.highlighted-attributes > li:nth-child(2) span")
        ?.html()
        ?.substringBefore("m²")
        ?.trim()
        ?.toInt() ?: 0

    private fun parsePrice(item: Element) = item
        .selectFirst("div.price-wrapper > p.product-price")
        ?.html()
        ?.replace(".", "")
        ?.replace("&nbsp;", "")
        ?.substringBefore("€")
        ?.replace(",", ".")
        ?.toFloat() ?: 0.0.toFloat()
}
