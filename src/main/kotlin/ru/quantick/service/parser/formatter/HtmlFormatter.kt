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
                (if (rentAd.structure != null)
                    "<b>Structure:</b> ${rentAd.structure}\n"
                else "") +
                (if (rentAd.furnished != null)
                    "<b>Furnished:</b> ${rentAd.furnished}\n"
                else "") +
                "<b>Published at:</b> ${rentAd.firstPublished}\n\n" +
                "<b>PRICE: ${rentAd.price}</b>"
}
