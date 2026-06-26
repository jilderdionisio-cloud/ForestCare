package com.plant.forestcare.domain.care

import com.plant.forestcare.data.GeminiPlantAnalysisRepository
import com.plant.forestcare.domain.model.GeminiPlantAnalysisInput
import com.plant.forestcare.domain.model.GeminiPlantAnalysisResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiPlantAnalysisUseCase @Inject constructor(
    private val repository: GeminiPlantAnalysisRepository
) {
    suspend operator fun invoke(input: GeminiPlantAnalysisInput): Result<GeminiPlantAnalysisResult> {
        return runCatching { repository.analyzePlant(input) }
    }
}
