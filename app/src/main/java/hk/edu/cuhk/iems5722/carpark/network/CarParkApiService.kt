package hk.edu.cuhk.iems5722.carpark.network

import hk.edu.cuhk.iems5722.carpark.model.CarParkBasicInfoResponse
import hk.edu.cuhk.iems5722.carpark.model.CarParkVacancyResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CarParkApiService {

    @GET("basic_info_all.json") suspend fun getCarParksBasicInfo(): CarParkBasicInfoResponse

    @GET("vacancy_{parkId}.json")
    suspend fun getCarParkVacancyByParkId(@Path("parkId") parkId: String): CarParkVacancyResponse
}
