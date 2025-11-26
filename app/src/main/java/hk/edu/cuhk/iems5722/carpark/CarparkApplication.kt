package hk.edu.cuhk.iems5722.carpark

import android.app.Application
import hk.edu.cuhk.iems5722.carpark.data.AppContainer
import hk.edu.cuhk.iems5722.carpark.data.DefaultAppContainer

class CarparkApplication : Application() {
    lateinit var container: AppContainer
    
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}

