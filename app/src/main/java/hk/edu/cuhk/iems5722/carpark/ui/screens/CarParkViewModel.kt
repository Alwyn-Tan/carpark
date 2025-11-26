package hk.edu.cuhk.iems5722.carpark.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import hk.edu.cuhk.iems5722.carpark.CarparkApplication
import hk.edu.cuhk.iems5722.carpark.data.CarParkRepository
import hk.edu.cuhk.iems5722.carpark.model.CarParkBasicInfo
import hk.edu.cuhk.iems5722.carpark.model.CarParkVacancy
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface CarParkListUiState {
    data class Success(val carParks: List<CarParkBasicInfo>) : CarParkListUiState
    object Error : CarParkListUiState
    object Loading : CarParkListUiState
}

sealed interface VacancyUiState {
    data class Success(val vacancy: CarParkVacancy) : VacancyUiState
    object Error : VacancyUiState
    object Loading : VacancyUiState
    object NotFound : VacancyUiState
}

class CarParkViewModel(
    private val carParkRepository: CarParkRepository
) : ViewModel() {

    var carParkListUiState: CarParkListUiState by mutableStateOf(CarParkListUiState.Loading)
        private set

    var vacancyUiState: VacancyUiState by mutableStateOf(VacancyUiState.Loading)
        private set

    init {
        getCarParks()
    }

    fun getCarParks() {
        viewModelScope.launch {
            carParkListUiState = CarParkListUiState.Loading
            carParkListUiState = try {
                val carParks = carParkRepository.getCarParks()
                CarParkListUiState.Success(carParks)
            } catch (e: IOException) {
                CarParkListUiState.Error
            } catch (e: HttpException) {
                CarParkListUiState.Error
            } catch (e: Exception) {
                CarParkListUiState.Error
            }
        }
    }

    fun getVacancy(parkId: String) {
        viewModelScope.launch {
            vacancyUiState = VacancyUiState.Loading
            vacancyUiState = try {
                val vacancy = carParkRepository.getVacancyByParkId(parkId)
                if (vacancy != null) {
                    VacancyUiState.Success(vacancy)
                } else {
                    VacancyUiState.NotFound
                }
            } catch (e: IOException) {
                VacancyUiState.Error
            } catch (e: HttpException) {
                VacancyUiState.Error
            } catch (e: Exception) {
                VacancyUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CarparkApplication)
                val carParkRepository = application.container.carParkRepository
                CarParkViewModel(carParkRepository = carParkRepository)
            }
        }
    }
}

