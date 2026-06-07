package com.kapil.aqiapp.domain.usecase

import com.kapil.aqiapp.domain.model.AqiData
import com.kapil.aqiapp.domain.repository.AqiRepository
import javax.inject.Inject

class GetAqiByLocationUseCase @Inject constructor(
    private val repository: AqiRepository
) {
    suspend operator fun invoke(lat: Double, lng: Double): AqiData {
        return repository.getAqiByLocation(lat, lng)
    }
}