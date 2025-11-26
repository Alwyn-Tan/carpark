package hk.edu.cuhk.iems5722.carpark.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import hk.edu.cuhk.iems5722.carpark.model.CarParkBasicInfo
import hk.edu.cuhk.iems5722.carpark.model.CarParkVacancy
import hk.edu.cuhk.iems5722.carpark.model.carparkPhotoHttps

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
        is VacancyUiState.Success ->
                VacancyDetailScreen(
                        carPark = carPark,
                        vacancy = vacancyUiState.vacancy,
                        onBackClicked = onBackClicked,
                        modifier = modifier
                )
        is VacancyUiState.Error ->
                VacancyErrorScreen(
                        onBackClicked = onBackClicked,
                        onRetryClicked = onRetryClicked,
                        modifier = modifier
                )
        is VacancyUiState.NotFound ->
                VacancyNotFoundScreen(onBackClicked = onBackClicked, modifier = modifier)
    }
}

@Composable
fun VacancyLoadingScreen(modifier: Modifier = Modifier) {
    Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
    ) { Text(text = "Loading vacancy...", style = MaterialTheme.typography.headlineMedium) }
}

@Composable
fun VacancyDetailScreen(
        carPark: CarParkBasicInfo?,
        vacancy: CarParkVacancy,
        onBackClicked: () -> Unit,
        modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        carPark?.let { info ->
            // 停车场图片（统一使用 HTTPS 链接）
            AsyncImage(
                    model = info.carparkPhotoHttps,
                    contentDescription = "Car park photo",
                    modifier =
                            Modifier.fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .padding(bottom = 16.dp),
                    contentScale = ContentScale.Crop
            )

            Text(
                    text = info.nameEn,
                    style =
                            MaterialTheme.typography.headlineMedium.copy(
                                    color = MaterialTheme.colorScheme.primary
                            ),
                    modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                    text = "ID: ${info.parkId}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 4.dp)
            )

            Text(
                    text = "Address: ${info.displayAddressEn}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 4.dp)
            )

            info.districtEn?.let { district ->
                Text(
                        text = "District: $district",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }

        val vacancyCount = vacancy.getFirstValidVacancy()

        Text(
                text =
                        if (vacancyCount >= 0) {
                            "Current Vacancy: $vacancyCount"
                        } else {
                            "Vacancy: Data unavailable"
                        },
                style =
                        MaterialTheme.typography.headlineLarge.copy(
                                color =
                                        if (vacancyCount >= 0) {
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

        // 按钮行：Google Maps 和 Back
        Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val context = LocalContext.current

            // Google Maps 按钮
            carPark?.let { info ->
                Button(
                        onClick = { openGoogleMaps(context, info.displayAddressEn) },
                        modifier = Modifier.weight(1f)
                ) { Text("Open in Google Maps") }
            }

            // Back 按钮
            Button(onClick = onBackClicked, modifier = Modifier.weight(1f)) { Text("Back") }
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
        Button(onClick = onRetryClicked, modifier = Modifier.padding(8.dp)) { Text("Retry") }
        Button(onClick = onBackClicked, modifier = Modifier.padding(8.dp)) { Text("Back") }
    }
}

@Composable
fun VacancyNotFoundScreen(onBackClicked: () -> Unit, modifier: Modifier = Modifier) {
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
        Button(onClick = onBackClicked, modifier = Modifier.padding(top = 16.dp)) { Text("Back") }
    }
}

/** 打开 Google Maps 并搜索指定地址 */
private fun openGoogleMaps(context: Context, address: String) {
    try {
        // 构建 Google Maps URL
        val encodedAddress = Uri.encode(address)
        val googleMapsUri =
                Uri.parse("https://www.google.com/maps/search/?api=1&query=$encodedAddress")

        val intent = Intent(Intent.ACTION_VIEW, googleMapsUri)
        intent.setPackage("com.google.android.apps.maps")

        // 如果 Google Maps 应用未安装，则使用浏览器打开
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            // 回退到浏览器
            val browserIntent = Intent(Intent.ACTION_VIEW, googleMapsUri)
            context.startActivity(browserIntent)
        }
    } catch (e: Exception) {
        // 处理异常，如果无法打开地图
        e.printStackTrace()
    }
}
