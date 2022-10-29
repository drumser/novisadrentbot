package ru.quantick.service.parser.formatter

import ru.quantick.model.RentAd

interface Formatter {
    fun format(rentAd: RentAd): String
}
