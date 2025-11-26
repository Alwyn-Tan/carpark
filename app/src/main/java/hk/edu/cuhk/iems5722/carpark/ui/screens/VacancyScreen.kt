package hk.edu.cuhk.iems5722.carpark.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import hk.edu.cuhk.iems5722.carpark.model.CarParkBasicInfo
import hk.edu.cuhk.iems5722.carpark.model.CarParkVacancy

@Composable
fun VacancyScreen(
    carPark: CarParkBasicInfo?,
    vacancyUiState: VacancyUiState,
    onBackClicked: () -> Unit,
    onRetryClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (vacancyUiState) {
        is VacancyUiState.Loading -> VacancyLoadingScreen(modifier = modifier)
        is VacancyUiState.Success -> VacancyDetailScreen(
            carPark = carPark,
            vacancy = vacancyUiState.vacancy,
            onBackClicked = onBackClicked,
            modifier = modifier
        )
        is VacancyUiState.Error -> VacancyErrorScreen(
            onBackClicked = onBackClicked,
            onRetryClicked = onRetryClicked,
            modifier = modifier
        )
        is VacancyUiState.NotFound -> VacancyNotFoundScreen(
            onBackClicked = onBackClicked,
            modifier = modifier
        )
    }
}

@Composable
fun VacancyLoadingScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Loading vacancy...",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Composable
fun VacancyDetailScreen(
    carPark: CarParkBasicInfo?,
    vacancy: CarParkVacancy,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        carPark?.let {
            Text(
                text = it.nameEn,
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            Text(
                text = "ID: ${it.parkId}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            
            Text(
                text = "Address: ${it.displayAddressEn}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            
            it.districtEn?.let { district ->
                Text(
                    text = "District: $district",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
        
        val vacancyCount = vacancy.getFirstValidVacancy()
        
        Text(
            text = if (vacancyCount >= 0) {
                "Current Vacancy: $vacancyCount"
            } else {
                "Vacancy: Data unavailable"
            },
            style = MaterialTheme.typography.headlineLarge.copy(
                color = if (vacancyCount >= 0) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.error
                }
            ),
            modifier = Modifier.padding(vertical = 16.dp)
        )
        
        if (vacancy.hasValidData()) {
            vacancy.getLastUpdateTime()?.let { lastUpdate ->
                Text(
                    text = "Last Update: $lastUpdate",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
        
        Button(
            onClick = onBackClicked,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Back")
        }
    }
}

@Composable
fun VacancyErrorScreen(
    onBackClicked: () -> Unit,
    onRetryClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Failed to load vacancy",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center
        )
        Button(
            onClick = onRetryClicked,
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Retry")
        }
        Button(
            onClick = onBackClicked,
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Back")
        }
    }
}

@Composable
fun VacancyNotFoundScreen(
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Vacancy information not found",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center
        )
        Button(
            onClick = onBackClicked,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Back")
        }
    }
}

