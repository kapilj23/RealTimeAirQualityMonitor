package com.kapil.aqiapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AqiApiService {
    @GET("feed/{city}/")
    suspend fun getAqi(
        @Path("city") city: String,
        @Query("token") token: String
    ): AqiDto
    // Naya endpoint — lat/lng se AQI
    @GET("feed/geo:{lat};{lng}/")
    suspend fun getAqiByLocation(
        @Path("lat") lat: Double,
        @Path("lng") lng: Double,
        @Query("token") token: String
    ): AqiDto
}