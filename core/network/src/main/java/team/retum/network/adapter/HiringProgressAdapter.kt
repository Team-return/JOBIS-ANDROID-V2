package team.retum.network.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import team.retum.common.enums.HiringProgress

class HiringProgressAdapter {
    @FromJson
    fun fromJson(value: String): HiringProgress =
        HiringProgress.entries.find { it.name == value } ?: HiringProgress.ETC

    @ToJson
    fun toJson(hiringProgress: HiringProgress): String = hiringProgress.name
}
