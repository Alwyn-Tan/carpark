package hk.edu.cuhk.iems5722.carpark.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import hk.edu.cuhk.iems5722.carpark.model.CarParkBasicInfo
import hk.edu.cuhk.iems5722.carpark.model.carparkPhotoHttps

@Composable
fun CarParkListScreen(
    carParkListUiState: CarParkListUiState,
    onVacancyClicked: (String) -> Unit,
    onRetryClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (carParkListUiState) {
        is CarParkListUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is CarParkListUiState.Success ->
            CarParkList(
                carParks = carParkListUiState.carParks,
                onVacancyClicked = onVacancyClicked,
                modifier = modifier
            )

        is CarParkListUiState.Error ->
            ErrorScreen(onRetryClicked = onRetryClicked, modifier = modifier.fillMaxSize())
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) { Text(text = "Loading car parks...", style = MaterialTheme.typography.headlineMedium) }
}

@Composable
fun ErrorScreen(onRetryClicked: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Failed to load car parks",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )
        Button(onClick = onRetryClicked) { Text("Retry") }
    }
}

@Composable
fun CarParkList(
    carParks: List<CarParkBasicInfo>,
    onVacancyClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        items(items = carParks, key = { it.parkId }) { carPark ->
            CarParkCard(carPark = carPark, onVacancyClicked = { onVacancyClicked(carPark.parkId) })
        }
    }
}

@Composable
fun CarParkCard(
    carPark: CarParkBasicInfo,
    onVacancyClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) { CarParkCardContent(carPark = carPark, onVacancyClicked = onVacancyClicked) }
}

@Composable
private fun CarParkCardContent(carPark: CarParkBasicInfo, onVacancyClicked: () -> Unit) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .animateContentSize(
                    animationSpec =
                        spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val photoUrl = remember(carPark.carparkPhoto) { carPark.carparkPhotoHttps }

        AsyncImage(
            model = photoUrl,
            contentDescription = "Photo of ${carPark.nameEn}",
            modifier = Modifier
                .width(100.dp)
                .height(75.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
        )

        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = carPark.nameEn,
                style =
                    MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                maxLines = 2,
                modifier = Modifier.padding(end = 8.dp)
            )

            if (expanded) {
                Text(text = "ID: ${carPark.parkId}", style = MaterialTheme.typography.bodySmall)
                Text(
                    text = carPark.displayAddressEn,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2
                )
                carPark.districtEn?.let {
                    Text(text = "District: $it", style = MaterialTheme.typography.bodySmall)
                }
                carPark.contactNo?.takeIf { it.isNotEmpty() }?.let {
                    Text(text = "Contact: $it", style = MaterialTheme.typography.bodySmall)
                }
                carPark.height?.takeIf { it > 0 }?.let {
                    Text(text = "Height Limit: ${it}m", style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        Button(
            onClick = onVacancyClicked,
            colors =
                ButtonDefaults.buttonColors(androidx.compose.ui.graphics.Color(0xFF1974C2)),
            modifier = Modifier.align(Alignment.CenterVertically)
        ) { Text("Vacancy", style = MaterialTheme.typography.labelMedium) }
    }
}
