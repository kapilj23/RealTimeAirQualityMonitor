package com.kapil.aqiapp.data.remote

data class AqiDto(
    val status: String,
    val data: AqiDataDto
)

data class AqiDataDto(
    val aqi: Int,
    val city: CityDto
)

data class CityDto(
    val name: String
)