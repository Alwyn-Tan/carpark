package hk.edu.cuhk.iems5722.carpark.data

import hk.edu.cuhk.iems5722.carpark.model.CarParkBasicInfo
import hk.edu.cuhk.iems5722.carpark.model.CarParkVacancy
import hk.edu.cuhk.iems5722.carpark.network.CarParkApiService

interface CarParkRepository {
    suspend fun getCarParks(): List<CarParkBasicInfo>
    
    suspend fun getVacancies(): List<CarParkVacancy>
    
    suspend fun getVacancyByParkId(parkId: String): CarParkVacancy?
}

class NetworkCarParkRepository(
    private val carParkApiService: CarParkApiService
) : CarParkRepository {
    
    override suspend fun getCarParks(): List<CarParkBasicInfo> {
        val response = carParkApiService.getCarParksBasicInfo()
        return response.carParks
    }
    
    override suspend fun getVacancies(): List<CarParkVacancy> {
        val response = carParkApiService.getCarParksVacancy()
        return response.carParks
    }
    
    override suspend fun getVacancyByParkId(parkId: String): CarParkVacancy? {
        val response = carParkApiService.getCarParksVacancy()
        return response.carParks.find { it.parkId == parkId }
    }
}

