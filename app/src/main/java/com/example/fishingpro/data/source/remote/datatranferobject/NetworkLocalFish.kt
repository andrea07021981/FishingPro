package com.example.fishingpro.data.source.remote.datatranferobject

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

class NetworkLocalFish : ArrayList<NetworkLocalFishItem>()

data class NetworkLocalFishItem(
    val nameEnglish: String,
    val riskGroupEnglish: String,
    val familyScientificName: String,
    val nameFrench: String,
    val riskGroupFrench: String,
    val nameScientific: String,
    val taxonomicSerialNum: Int
)

fun NetworkLocalFishItem.asFirebaseModel(): NetworkFirebaseFish {
    return NetworkFirebaseFish(
        fishId = taxonomicSerialNum,
        fishName = nameEnglish,
        fishDescription = nameScientific
    )
}