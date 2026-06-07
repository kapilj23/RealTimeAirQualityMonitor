package com.kapil.aqiapp.data.remote

data class AqiDto(
    val status: String,
    val data: AqiDataDto
)

data class AqiDataDto(
    val aqi: Int,
    val city: CityDto,
    val iaqi: IaqiDto?,
    val forecast: ForecastDto?
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
data class ForecastDto(
    val daily: DailyForecastDto?
)

data class DailyForecastDto(
    val pm25: List<ForecastDayDto>?,
    val pm10: List<ForecastDayDto>?,
    val o3: List<ForecastDayDto>?,
    val uvi: List<ForecastDayDto>?
)

data class ForecastDayDto(
    val avg: Int,
    val day: String,  // "2024-01-15" format
    val max: Int,
    val min: Int
)