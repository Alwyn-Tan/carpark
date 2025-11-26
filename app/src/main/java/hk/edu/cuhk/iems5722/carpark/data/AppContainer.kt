package hk.edu.cuhk.iems5722.carpark.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import hk.edu.cuhk.iems5722.carpark.network.CarParkApiService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

interface AppContainer {
    val carParkRepository: CarParkRepository
}

class DefaultAppContainer : AppContainer {
    
    private val baseUrl = "https://resource.data.one.gov.hk/td/carpark/"
    
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .build()
    
    private val carParkApiService: CarParkApiService by lazy {
        retrofit.create(CarParkApiService::class.java)
    }
    
    override val carParkRepository: CarParkRepository by lazy {
        NetworkCarParkRepository(carParkApiService)
    }
}

