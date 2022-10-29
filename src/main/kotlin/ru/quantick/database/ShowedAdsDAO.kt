package ru.quantick.database

import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import ru.quantick.model.RentAd
import ru.quantick.model.ShowedAds
import ru.quantick.model.getIdentifier

class ShowedAdsDAO {
    fun handle(
        rentAd: RentAd,
        action: suspend () -> Unit
    ) = transaction {
        if (ShowedAds
                .select { ShowedAds.identifier eq rentAd.getIdentifier() }
                .singleOrNull() != null
        ) {
            return@transaction
        }

        runBlocking {
            action()
        }

        ShowedAds.insert { it[ShowedAds.identifier] = rentAd.getIdentifier() }
    }
}
