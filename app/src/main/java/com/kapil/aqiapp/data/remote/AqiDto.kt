package com.kapil.aqiapp.data.remote

data class AqiDto(
    val status: String,
    val data: AqiDataDto
)

data class AqiDataDto(
    val aqi: Int,
    val city: CityDto,
    val iaqi: IaqiDto?  // pollutants data
)

data class CityDto(
    val name: String
)

data class IaqiDto(
    val pm25: PollutantDto?,
    val pm10: PollutantDto?,
    val no2: PollutantDto?,
    val o3: PollutantDto?,
    val co: PollutantDto?,
    val so2: PollutantDto?
)

data class PollutantDto(
    val v: Double
)