package com.kapil.aqiapp.data.repository

import com.kapil.aqiapp.data.remote.AqiApiService
import com.kapil.aqiapp.data.remote.AqiDto
import com.kapil.aqiapp.domain.model.AqiData
import com.kapil.aqiapp.domain.model.Pollutants
import com.kapil.aqiapp.domain.repository.AqiRepository
import javax.inject.Inject

class AqiRepositoryImpl @Inject constructor(
    private val apiService: AqiApiService
) : AqiRepository {

    override suspend fun getAqi(city: String): AqiData {
        val response = apiService.getAqi(
            city = city,
            token = "841d055361eb0be98f3b4ce2ede825bc93c6554a"
        )
        if (response.status != "ok") {
            throw Exception("City not found. Please try another city.")
        }
        return mapToDomain(response)
    }

    override suspend fun getAqiByLocation(lat: Double, lng: Double): AqiData {
        val response = apiService.getAqiByLocation(
            lat = lat,
            lng = lng,
            token = "841d055361eb0be98f3b4ce2ede825bc93c6554a"
        )
        if (response.status != "ok") {
            throw Exception("Unable to fetch AQI for your location.")
        }
        return mapToDomain(response)
    }
}
private fun mapToDomain(response: AqiDto): AqiData {
    return AqiData(
        aqi = response.data.aqi,
        cityName = response.data.city.name,
        pollutants = Pollutants(
            pm25 = response.data.iaqi?.pm25?.v,
            pm10 = response.data.iaqi?.pm10?.v,
            no2 = response.data.iaqi?.no2?.v,
            o3 = response.data.iaqi?.o3?.v,
            co = response.data.iaqi?.co?.v,
            so2 = response.data.iaqi?.so2?.v
        )
    )
}
