package ru.quantick.service.parser.formatter

import ru.quantick.model.RentAd
import ru.quantick.model.getSource

class HtmlFormatter : Formatter {
    override fun format(rentAd: RentAd) =
        "<b>New AD:</b>\n" +
                "<b>Source:</b> ${rentAd.getSource()}\n" +
                "<b>Link:</b> ${rentAd.link}\n" +
                "<b>Location:</b> ${rentAd.location}\n" +
                "<b>Size:</b> ${rentAd.size}\n" +
                "<b>Structure:</b> ${rentAd.structure}\n" +
                "<b>Furnished:</b> ${rentAd.furnished ?: "unknown"}\n" +
                "<b>Published at:</b> ${rentAd.firstPublished}\n\n" +
                "<b>PRICE: ${rentAd.price}</b>"
}
