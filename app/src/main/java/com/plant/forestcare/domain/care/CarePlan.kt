package com.plant.forestcare.domain.care

data class CarePlan(
    val diagnosis: String,
    val recommendedWateringDays: Int,
    val wateringRecommendation: String,
    val lightRecommendation: String,
    val fertilizationRecommendation: String,
    val pruningRecommendation: String,
    val treatmentRecommendation: String,
    val nextReviewDays: Int,
    val urgency: CareUrgency
)

enum class CareUrgency(val storageValue: String, val displayName: String) {
    Healthy("saludable", "Saludable"),
    NeedsAttention("requiere_atencion", "Requiere atención"),
    Urgent("cuidado_urgente", "Cuidado urgente");

    companion object {
        fun fromStorage(value: String): CareUrgency {
            return entries.firstOrNull { it.storageValue == value } ?: Healthy
        }
    }
}
