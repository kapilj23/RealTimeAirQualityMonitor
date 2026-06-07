package com.kapil.aqiapp.data.repository

import com.kapil.aqiapp.data.remote.AqiApiService
import com.kapil.aqiapp.domain.model.AqiData
import com.kapil.aqiapp.domain.repository.AqiRepository
import javax.inject.Inject

class AqiRepositoryImpl @Inject constructor(
    private val apiService: AqiApiService
) : AqiRepository {

    override suspend fun getAqi(city: String): AqiData {
        val response = apiService.getAqi(
            city = city,
            token = "841d055361eb0be98f3b4ce2ede825bc93c6554a"  // paste your token here
        )
        return AqiData(
            aqi = response.data.aqi,
            cityName = response.data.city.name
        )
    }
}