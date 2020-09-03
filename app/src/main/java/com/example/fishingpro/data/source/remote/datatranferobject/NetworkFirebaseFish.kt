package com.example.fishingpro.data.source.remote.datatranferobject

data class NetworkFirebaseFish(
    var fishId: Int,
    var fishName: String,
    var fishDescription: String
) {
    constructor(): this(0,"None", "None")
}