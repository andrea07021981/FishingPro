package com.example.fishingpro.data.domain

data class LocalUser(
    var userUID: String,
    var FirstName: String,
    var LastName: String
) {
    constructor(): this("","", "")
}