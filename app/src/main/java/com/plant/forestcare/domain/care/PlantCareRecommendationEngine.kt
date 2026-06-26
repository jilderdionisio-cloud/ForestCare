package com.plant.forestcare.domain.care

import com.plant.forestcare.domain.model.GeminiPlantAnalysisResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlantCareRecommendationEngine @Inject constructor() {
    fun generateCarePlan(condition: PlantCondition): CarePlan {
        val plantText = "${condition.commonName} ${condition.scientificName} ${condition.apiDescription.orEmpty()}".lowercase()
        val location = condition.locationType.lowercase()
        val light = condition.lightExposure.lowercase()
        val leaves = condition.leafStatus.lowercase()
        val soil = condition.soilHumidity.lowercase()
        val watering = condition.currentWateringFrequency.lowercase()
        val disease = condition.diseaseDiagnosis
        val diseaseName = disease?.diseaseName.orEmpty()
        val diseaseText = "$diseaseName ${disease?.description.orEmpty()}".lowercase()

        return when {
            disease?.isHealthy == false -> CarePlan(
                diagnosis = listOfNotNull(
                    diseaseName.takeIf { it.isNotBlank() },
                    disease.description
                ).joinToString(": ").ifBlank { "Posible enfermedad detectada" },
                recommendedWateringDays = defaultWateringDays(plantText, watering),
                wateringRecommendation = "Mantén el riego controlado y evita estrés adicional durante el tratamiento.",
                lightRecommendation = "Aísla temporalmente la planta y mantenla con luz indirecta estable.",
                fertilizationRecommendation = "Evita fertilizar hasta controlar el problema detectado.",
                pruningRecommendation = "Revisa hojas afectadas y retira tejido muy dañado con herramientas limpias.",
                treatmentRecommendation = disease.treatmentRecommendation
                    ?: diseaseTreatmentFallback(diseaseText),
                nextReviewDays = if (isFungusSpotOrPest(diseaseText)) 3 else 3,
                urgency = CareUrgency.Urgent
            )

            leaves.contains("amarilla") && soil.contains("muy mojada") -> CarePlan(
                diagnosis = "Posible exceso de agua",
                recommendedWateringDays = 10,
                wateringRecommendation = "Reduce el riego y revisa que la maceta tenga buen drenaje.",
                lightRecommendation = "Mantén luz indirecta estable mientras se recupera.",
                fertilizationRecommendation = "No fertilices hasta que la humedad vuelva a un nivel normal.",
                pruningRecommendation = "Retira hojas muy dañadas o amarillas para evitar estrés adicional.",
                treatmentRecommendation = "Reduce riego, revisa drenaje y ventila el sustrato.",
                nextReviewDays = 3,
                urgency = CareUrgency.NeedsAttention
            )

            isSucculentOrCactus(plantText) && watering.contains("diario") -> CarePlan(
                diagnosis = "Posible exceso de riego",
                recommendedWateringDays = 12,
                wateringRecommendation = "Riega cada 10 a 14 días y solo cuando el sustrato esté seco.",
                lightRecommendation = "Ubícala en un punto luminoso, idealmente con varias horas de sol suave.",
                fertilizationRecommendation = "Fertiliza de forma ligera cada 6 a 8 semanas en temporada de crecimiento.",
                pruningRecommendation = "Retira hojas secas solo cuando estén completamente desprendibles.",
                treatmentRecommendation = "Suspende riegos frecuentes y deja secar completamente el sustrato.",
                nextReviewDays = 7,
                urgency = CareUrgency.NeedsAttention
            )

            isTropical(plantText) && location.contains("interior") && light.contains("poca") -> CarePlan(
                diagnosis = "Necesita más luz indirecta",
                recommendedWateringDays = 6,
                wateringRecommendation = "Mantén el sustrato ligeramente húmedo sin encharcar.",
                lightRecommendation = "Colócala cerca de una ventana con luz indirecta brillante.",
                fertilizationRecommendation = "Fertiliza cada 4 semanas con dosis moderada en temporada activa.",
                pruningRecommendation = "Poda hojas débiles para estimular crecimiento saludable.",
                treatmentRecommendation = "Mueve la planta gradualmente hacia una fuente de luz indirecta.",
                nextReviewDays = 7,
                urgency = CareUrgency.NeedsAttention
            )

            leaves.contains("seca") && soil.contains("seca") -> CarePlan(
                diagnosis = "Posible falta de agua",
                recommendedWateringDays = 4,
                wateringRecommendation = "Aumenta la frecuencia de riego y verifica la humedad antes de repetir.",
                lightRecommendation = "Evita sol directo fuerte hasta que la planta se estabilice.",
                fertilizationRecommendation = "Espera a que recupere hidratación antes de fertilizar.",
                pruningRecommendation = "Retira puntas secas si ya no se recuperan.",
                treatmentRecommendation = "Aumenta riego de forma gradual y verifica que el agua llegue a toda la raíz.",
                nextReviewDays = 5,
                urgency = CareUrgency.NeedsAttention
            )

            isHealthy(leaves, soil) -> CarePlan(
                diagnosis = "Planta estable",
                recommendedWateringDays = defaultWateringDays(plantText, watering),
                wateringRecommendation = "Mantén la rutina actual y ajusta según la humedad del sustrato.",
                lightRecommendation = defaultLightRecommendation(plantText, condition.lightExposure),
                fertilizationRecommendation = "Fertiliza cada 4 a 6 semanas en temporada de crecimiento.",
                pruningRecommendation = "Poda hojas secas o dañadas cuando aparezcan.",
                treatmentRecommendation = "Mantener rutina actual.",
                nextReviewDays = 7,
                urgency = CareUrgency.Healthy
            )

            else -> CarePlan(
                diagnosis = "Rutina de cuidado general sugerida",
                recommendedWateringDays = defaultWateringDays(plantText, watering),
                wateringRecommendation = "Riega cuando los primeros centímetros del sustrato se sientan secos.",
                lightRecommendation = defaultLightRecommendation(plantText, condition.lightExposure),
                fertilizationRecommendation = "Fertiliza una vez al mes con dosis baja si la planta está creciendo.",
                pruningRecommendation = "Revisa hojas dañadas semanalmente y retíralas con tijeras limpias.",
                treatmentRecommendation = "Observa cambios durante la semana y ajusta riego/luz si aparecen síntomas.",
                nextReviewDays = 7,
                urgency = CareUrgency.Healthy
            )
        }
    }

    fun applyGeminiAnalysis(
        basePlan: CarePlan,
        geminiAnalysis: GeminiPlantAnalysisResult?
    ): CarePlan {
        if (geminiAnalysis == null) return basePlan

        return basePlan.copy(
            diagnosis = geminiAnalysis.diagnosis.ifBlank { basePlan.diagnosis },
            wateringRecommendation = geminiAnalysis.wateringRecommendation.ifBlank { basePlan.wateringRecommendation },
            lightRecommendation = geminiAnalysis.lightRecommendation.ifBlank { basePlan.lightRecommendation },
            fertilizationRecommendation = geminiAnalysis.fertilizationRecommendation.ifBlank { basePlan.fertilizationRecommendation },
            pruningRecommendation = geminiAnalysis.pruningRecommendation.ifBlank { basePlan.pruningRecommendation },
            treatmentRecommendation = geminiAnalysis.treatmentRecommendation.ifBlank { basePlan.treatmentRecommendation },
            urgency = when (geminiAnalysis.riskLevel) {
                "alto" -> CareUrgency.Urgent
                "medio" -> CareUrgency.NeedsAttention
                else -> basePlan.urgency
            }
        )
    }

    private fun isSucculentOrCactus(text: String): Boolean {
        return listOf("suculenta", "succulent", "cactus", "echeveria", "aloe", "crassula", "sedum").any(text::contains)
    }

    private fun isTropical(text: String): Boolean {
        return listOf("tropical", "monstera", "philodendron", "pothos", "calathea", "ficus", "spathiphyllum").any(text::contains)
    }

    private fun isHealthy(leaves: String, soil: String): Boolean {
        return (leaves.contains("sana") || leaves.contains("verde")) && !soil.contains("muy mojada")
    }

    private fun isFungusSpotOrPest(text: String): Boolean {
        return listOf("hongo", "fung", "spot", "mancha", "plaga", "pest", "mildew", "rot").any(text::contains)
    }

    private fun diseaseTreatmentFallback(text: String): String {
        return if (isFungusSpotOrPest(text)) {
            "Aísla la planta, revisa hojas afectadas y evita mojar directamente las hojas. Programa revisión en 3 días."
        } else {
            "Aísla temporalmente la planta y revisa evolución en 3 días."
        }
    }

    private fun defaultWateringDays(plantText: String, watering: String): Int {
        return when {
            isSucculentOrCactus(plantText) -> 12
            watering.contains("diario") || watering.contains("2 días") -> 4
            watering.contains("semanal") -> 7
            else -> 6
        }
    }

    private fun defaultLightRecommendation(plantText: String, userLight: String): String {
        return when {
            isSucculentOrCactus(plantText) -> "Prefiere luz brillante y algunas horas de sol suave."
            isTropical(plantText) -> "Prefiere luz indirecta brillante y evitar sol directo fuerte."
            userLight.equals("No sabe", ignoreCase = true) -> "Empieza con luz indirecta brillante y observa la respuesta de las hojas."
            else -> "Mantén ${userLight.lowercase()} si la planta conserva buen color y crecimiento."
        }
    }
}
