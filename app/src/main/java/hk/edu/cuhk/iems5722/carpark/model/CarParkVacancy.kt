package hk.edu.cuhk.iems5722.carpark.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServiceCategory(
    @SerialName("category") val category: String,
    @SerialName("vacancy_type") val vacancyType: String,
    @SerialName("vacancy") val vacancy: Int,
    @SerialName("lastupdate") val lastUpdate: String
)

@Serializable
data class VehicleType(
    @SerialName("type") val type: String,
    @SerialName("service_category") val serviceCategories: List<ServiceCategory>
)

@Serializable
data class CarParkVacancy(
    @SerialName("park_id") val parkId: String,
    @SerialName("vehicle_type") val vehicleTypes: List<VehicleType>
) {
    fun getTotalVacancy(): Int {
        return vehicleTypes.flatMap { it.serviceCategories }.filter { it.vacancy >= 0 }.sumOf {
            it.vacancy
        }
    }

    fun getFirstValidVacancy(): Int {
        return vehicleTypes
            .flatMap { it.serviceCategories }
            .firstOrNull { it.vacancy >= 0 }
            ?.vacancy
            ?: -1
    }

    fun getLastUpdateTime(): String? {
        return vehicleTypes.flatMap { it.serviceCategories }.firstOrNull()?.lastUpdate
    }

    fun hasValidData(): Boolean {
        return vehicleTypes.flatMap { it.serviceCategories }.any { it.vacancy >= 0 }
    }

    fun getFirstServiceCategory(): ServiceCategory? {
        return vehicleTypes.flatMap { it.serviceCategories }.firstOrNull()
    }

    fun getVacancyDisplayText(): String {
        val serviceCategory =
            getFirstServiceCategory() ?: return "No data provided by the car park operator"

        return when (serviceCategory.vacancyType) {
            "A" ->
                when {
                    serviceCategory.vacancy == 0 -> "Full"
                    serviceCategory.vacancy > 0 ->
                        "${serviceCategory.vacancy} parking spaces available"

                    serviceCategory.vacancy == -1 -> "No data provided by the car park operator"
                    else -> "Unknown status"
                }

            "B" ->
                when {
                    serviceCategory.vacancy == 0 -> "Full"
                    serviceCategory.vacancy == 1 -> "Parking space available"
                    serviceCategory.vacancy == -1 -> "No data provided by the car park operator"
                    else -> "Unknown status"
                }

            "C" -> "Closed"
            else -> "Unknown status"
        }
    }

    fun isClosedOrNoData(): Boolean {
        val serviceCategory = getFirstServiceCategory() ?: return true
        return serviceCategory.vacancyType == "C" || serviceCategory.vacancy == -1
    }

    fun isFull(): Boolean {
        val serviceCategory = getFirstServiceCategory() ?: return false
        return serviceCategory.vacancy == 0 && serviceCategory.vacancyType != "C"
    }
}

@Serializable
data class CarParkVacancyResponse(@SerialName("car_park") val carParks: List<CarParkVacancy>)
