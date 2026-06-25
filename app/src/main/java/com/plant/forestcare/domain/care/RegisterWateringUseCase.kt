package com.plant.forestcare.domain.care



import com.plant.forestcare.data.local.PlantDao

import com.plant.forestcare.data.repositories.CareHistoryRepository

import kotlinx.coroutines.flow.firstOrNull



/**

 * Caso de uso encargado de registrar la acción de riego manual.

 * Actualiza la fecha del próximo riego en la base de datos local y

 * registra el evento en el historial de cuidados.

 */

class RegisterWateringUseCase(

    private val plantDao: PlantDao,

    private val careHistoryRepository: CareHistoryRepository

) {



    suspend operator fun invoke(plantId: String, userId: String? = null) {

// a) Consumir el primer valor del flujo de la planta

        val plant = plantDao.getPlantById(plantId).firstOrNull()



        if (plant != null) {

            val now = System.currentTimeMillis()


// b) Calcular la fecha del próximo riego (Días -> Milisegundos)

// Se asume recommendedWateringDays (Int) en PlantEntity

            val nextWateringTimestamp = now + (plant.recommendedWateringDays * 24 * 60 * 60 * 1000L)



// c) Crear copia de la entidad y actualizar en Room

            val updatedPlant = plant.copy(

                nextWateringAt = nextWateringTimestamp,

                updatedAt = now

            )

            plantDao.updatePlant(updatedPlant)



// d) Registrar en el historial de cuidados

            careHistoryRepository.logCare(

                plantId = plantId,

                type = "watering",

                title = "Riego completado",

                description = "La planta fue regada manualmente por el usuario.",

                userId = userId

            )

        }

    }

}