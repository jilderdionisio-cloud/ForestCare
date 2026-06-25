package com.plant.forestcare.domain.care

import com.plant.forestcare.data.GeminiPlantAnalysisRepository
import com.plant.forestcare.domain.model.GeminiPlantAnalysisInput
import com.plant.forestcare.domain.model.GeminiPlantAnalysisResult

class GeminiPlantAnalysisUseCase(
    private val repository: GeminiPlantAnalysisRepository = GeminiPlantAnalysisRepository()
) {
    suspend operator fun invoke(input: GeminiPlantAnalysisInput): Result<GeminiPlantAnalysisResult> {
        return runCatching { repository.analyzePlant(input) }
    }
}
