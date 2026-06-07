package com.kapil.aqiapp.domain.usecase

import com.kapil.aqiapp.domain.model.AqiData
import com.kapil.aqiapp.domain.repository.AqiRepository
import javax.inject.Inject

class GetAqiUseCase @Inject constructor(
    private val repository: AqiRepository
) {
    suspend operator fun invoke(city: String): AqiData {
        return repository.getAqi(city)
    }
}