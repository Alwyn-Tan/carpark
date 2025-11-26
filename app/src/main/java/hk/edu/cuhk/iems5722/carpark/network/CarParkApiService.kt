package hk.edu.cuhk.iems5722.carpark.network

import hk.edu.cuhk.iems5722.carpark.model.CarParkBasicInfoResponse
import hk.edu.cuhk.iems5722.carpark.model.CarParkVacancyResponse
import retrofit2.http.GET

interface CarParkApiService {
    
    @GET("basic_info_all.json")
    suspend fun getCarParksBasicInfo(): CarParkBasicInfoResponse
    
    @GET("vacancy_all.json")
    suspend fun getCarParksVacancy(): CarParkVacancyResponse
}

