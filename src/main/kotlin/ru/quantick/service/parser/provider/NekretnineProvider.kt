package ru.quantick.service.parser.provider

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import ru.quantick.model.ProviderEnum
import ru.quantick.model.RentAd

class NekretnineProvider : RentProvider {
    override suspend fun getLastAds(num: Int): List<RentAd> {
        val url =
            "https://www.nekretnine.rs/stambeni-objekti/stanovi/izdavanje-prodaja/izdavanje/grad/novi-sad/cena/_1500/lista/po-stranici/10/?order=2"
        val doc = Jsoup.connect(url).userAgent("Mozilla").get()

        val items = doc.select("div.advert-list > div.offer")

        val result = mutableListOf<RentAd>()
        for (item in items) {
            if ((item.selectFirst("div.flag-box")?.html() ?: "") != "") {
                continue
            }

            kotlin.runCatching {
                val price = parsePrice(item)
                val link = parseLink(item)
                val detailDoc = Jsoup.connect(link).userAgent("Mozilla").get()

                val structure = parseStructure(detailDoc)
                val id = parseId(link)
                val publishDate = parsePublishDate(item)
                val address = parseAddress(detailDoc)
                val size = parseSize(item)

                result.add(
                    RentAd(
                        source = ProviderEnum.NEKRETNINE,
                        id = id,
                        location = address,
                        size = size,
                        structure = structure,
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

    private fun parseId(link: String) = link
        .split("/")
        .let {
            it.get(it.count() - 2)
        }

    private fun parseStructure(detailDoc: Document) =
        detailDoc
            .selectFirst(".property__main-details > ul > li:nth-child(2) > span")
            ?.html()
            ?.substringAfter("br>")

    private fun parseAddress(detailDoc: Document) =
        detailDoc.select(".property__location > ul > li")
            .joinToString(" - ") {
                it.html().replace("&nbsp;", "")
            }.trimEnd('-', ' ')

    private fun parsePublishDate(item: Element) = item
        .selectFirst("div.offer-meta-info")
        ?.html()
        ?.split("|")?.first()?.trimEnd()

    private fun parseLink(item: Element) = "https://www.nekretnine.rs${
        item
            .selectFirst("h2.offer-title > a")
            ?.attr("href")
    }"

    private fun parseSize(item: Element) = item
        .selectFirst(".text-right p.offer-price > span")
        ?.html()
        ?.replace(" ", "")
        ?.substringBefore("m")
        ?.toInt() ?: 0

    private fun parsePrice(item: Element) = item
        .selectFirst("p.offer-price > span")
        ?.html()
        ?.substringBefore("EUR")
        ?.toFloat() ?: 0.0.toFloat()
}
