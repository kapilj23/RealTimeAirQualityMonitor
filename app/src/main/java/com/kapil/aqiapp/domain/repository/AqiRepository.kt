package com.kapil.aqiapp.domain.repository

import com.kapil.aqiapp.domain.model.AqiData

interface AqiRepository {
    suspend fun getAqi(city: String): AqiData
    suspend fun getAqiByLocation(lat: Double, lng: Double): AqiData
}