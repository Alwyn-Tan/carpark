package hk.edu.cuhk.iems5722.carpark.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hk.edu.cuhk.iems5722.carpark.ui.screens.CarParkListScreen
import hk.edu.cuhk.iems5722.carpark.ui.screens.CarParkListUiState
import hk.edu.cuhk.iems5722.carpark.ui.screens.CarParkViewModel
import hk.edu.cuhk.iems5722.carpark.ui.screens.OnboardingScreen
import hk.edu.cuhk.iems5722.carpark.ui.screens.VacancyScreen

object Routes {
    const val ON_BOARDING = "onboarding"
    const val HOME = "home"
    const val VACANCY = "vacancy/{parkId}"
    
    fun vacancy(parkId: String) = "vacancy/$parkId"
}

@Composable
fun CarparkApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    
    val carParkViewModel: CarParkViewModel = viewModel(
        factory = CarParkViewModel.Factory
    )
    
    Surface(modifier) {
        NavHost(
            navController = navController,
            startDestination = Routes.ON_BOARDING,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(Routes.ON_BOARDING) {
                OnboardingScreen(
                    onContinueClicked = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.ON_BOARDING) { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
            
            composable(Routes.HOME) {
                CarParkListScreen(
                    carParkListUiState = carParkViewModel.carParkListUiState,
                    onVacancyClicked = { parkId ->
                        carParkViewModel.getVacancy(parkId)
                        navController.navigate(Routes.vacancy(parkId))
                    },
                    onRetryClicked = {
                        carParkViewModel.getCarParks()
                    }
                )
            }
            
            composable(Routes.VACANCY) { backStackEntry ->
                val parkId = backStackEntry.arguments?.getString("parkId")
                
                val carPark = when (val state = carParkViewModel.carParkListUiState) {
                    is CarParkListUiState.Success -> {
                        state.carParks.find { it.parkId == parkId }
                    }
                    else -> null
                }
                
                VacancyScreen(
                    carPark = carPark,
                    vacancyUiState = carParkViewModel.vacancyUiState,
                    onBackClicked = {
                        navController.popBackStack()
                    },
                    onRetryClicked = {
                        parkId?.let { carParkViewModel.getVacancy(it) }
                    },
                    modifier = Modifier
                )
            }
        }
    }
}

