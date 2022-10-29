package ru.quantick

private const val DEFAULT_LAST_ADS_LIMIT = 10

class ConfigurationInitiator {
    companion object {
        fun create() = Configuration(
            tgToken = System.getenv("TG_TOKEN"),
            tgChannelId = System.getenv("TG_CHANNEL_ID").toLong(),
            lastAdsLimit = System.getenv("LAST_ADS_LIMIT")?.toInt() ?: DEFAULT_LAST_ADS_LIMIT
        )
    }
}

data class Configuration(
    val tgToken: String,
    val tgChannelId: Long,
    val lastAdsLimit: Int,
)
