package com.example.randomuserapp.model

data class RandomUserResponse (
    val results: List<RandomUser>
)

data class RandomUser (
    val name: Name,
    val location: Location,
    val phone: String,
    val picture: Picture
)

data class Name (
    val title: String,
    val first: String,
    val last: String
)

data class Location (
    val city: String,
    val state: String,
    val country: String,
    val street: Street
)

data class Street (
    val number: Int,
    val name: String
)

data class Picture (
    val large: String,
    val medium: String,
    val thumbnailL: String
)