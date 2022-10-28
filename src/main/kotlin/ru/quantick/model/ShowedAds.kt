package ru.quantick.model

import org.jetbrains.exposed.sql.Table

data class ShowedAd(val identifier: String)

object ShowedAds: Table() {
    private val id = integer("id").autoIncrement()
    val identifier = varchar("identifier", 128)

    override val primaryKey = PrimaryKey(id)
}
