package ru.quantick.dao

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import ru.quantick.model.ShowedAds

class ShowedAdsDAO {
    fun addShowed(identifier: String) = transaction {
        ShowedAds.insert { it[ShowedAds.identifier] = identifier }
    }

    fun isAlreadySent(identifier: String) = transaction {
        ShowedAds
            .select { ShowedAds.identifier eq identifier }
            .singleOrNull() != null
    }
}
