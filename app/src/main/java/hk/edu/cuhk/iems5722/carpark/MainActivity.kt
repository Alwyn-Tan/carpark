package hk.edu.cuhk.iems5722.carpark

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hk.edu.cuhk.iems5722.carpark.ui.theme.CarparkTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { CarparkTheme { MyApp(modifier = Modifier.fillMaxSize()) } }
    }
}

data class CarPark(val parkId: String, val nameEn: String, val displayAddressEn: String)

val carParkData =
        listOf(
                CarPark(
                        "tdc166p1",
                        "Phase 1 Carpark of Tin Ching Estate",
                        "Tin Ching Estate (Phase 1 Carpark), Yuen Long, New Territories"
                ),
                CarPark(
                        "tdc6p11",
                        "Cheung Shan Estate Carpark",
                        "Cheung Shan Estate Road West, Tsuen Wan, N.T."
                ),
                CarPark(
                        "tdc245p1",
                        "Hong Wah Court Car Park No.2",
                        "Lin Tak Road, Lam Tin, Kowloon"
                ),
                CarPark(
                        "tdc6p8",
                        "Kwai Shing West Shopping Centre Car Park",
                        "Kwai Luen Road, Kwai Chung, N.T."
                ),
                CarPark(
                        "tdc6p9",
                        "Lai King Estate Car Park C",
                        "Lai King Hill Road, Kwai Chung, N.T."
                ),
                CarPark(
                        "tdc8p3",
                        "MTR Hong Kong Station Car Park",
                        "FINANCE STREET, Central & Western District, Hong Kong"
                ),
                CarPark(
                        "tdcp10",
                        "Aberdeen Car Park",
                        "18 Aberdeen Reservoir Road, Aberdeen, Hong Kong"
                ),
                CarPark("tdcp11", "Tin Hau Car Park", "Lau Sin Street & Electric Road, Tin Hau"),
                CarPark(
                        "tdcp12",
                        "Sheung Fung Street Car Park",
                        "Sheung Fung Street, Wong Tai Sin, Kowloon"
                ),
                CarPark(
                        "tdcp2",
                        "Rumsey Street Car Park 2",
                        "Rumsey Street, Sheung Wan, Hong Kong"
                ),
                CarPark(
                        "tdcp3",
                        "Tsuen Wan Car Park",
                        "174-208 Castle Peak Road, Tsuen Wan, New Territories"
                ),
                CarPark("tdcp5", "City Hall Car Park 1", "Edinburgh Place, Central, Hong Kong"),
                CarPark("tdcp6", "Kwai Fong Car Park", "19 Kwai Yi Road, Kwai Fong"),
                CarPark(
                        "tdcp7",
                        "Kennedy Town Car Park",
                        "12 Rock Hill Street, Kennedy Town, Hong Kong"
                ),
                CarPark("tdcp8", "Star Ferry Car Park", "9 Edinburgh Place, Central, Hong Kong"),
                CarPark(
                        "tdcp9",
                        "Shau Kei Wan Car Park",
                        "1 Po Man Street, Shau Kei Wan, Hong Kong"
                ),
                CarPark(
                        "tdstt2p2",
                        "Value Parking Shan Mei Street",
                        "Junction of Shan Mei Street & Sui Wo Road, Fo Tan"
                ),
                CarPark(
                        "tdc5p1",
                        "Hopewell Centre",
                        "183 Queen's Road East, Wan Chai, Hong Kong (Entrance on Kennedy Road)"
                ),
                CarPark(
                        "tdc5p3",
                        "Wu Chung House",
                        "213 Queen's Road East, Wan Chai District, Hong Kong"
                ),
                CarPark("tdc5p4", "Panda Place", "3 Tsuen Wah Street, Tsuen Wan, Hong Kong"),
                CarPark(
                        "tdc17p1",
                        "Xiqu Centre Car Park",
                        "88 Austin Road West, Tsim Sha Tsui, Kowloon"
                ),
                CarPark("tdstt30", "Hoi Ting Road", "Hoi Ting Road, Yau Ma Tei, Kowloon"),
                CarPark(
                        "tdc1p5",
                        "Lee Garden Three Car Park",
                        "1 Sunning Road, Causeway Bay, Hong Kong"
                ),
                CarPark(
                        "tdc1p4",
                        "Hysan Place Car Park",
                        "500 Hennessy Road, Wan Chai District, Hong Kong"
                ),
                CarPark(
                        "tdc1p3",
                        "Leighton Centre Car Park",
                        "77 LEIGHTON ROAD, Wan Chai District, Hong Kong"
                ),
                CarPark(
                        "tdc1p2",
                        "LEE GARDEN TWO CAR PARK",
                        "28 YUN PING ROAD, Wan Chai District, Hong Kong"
                ),
                CarPark(
                        "tdc1p1",
                        "LEE GARDEN ONE CAR PARK",
                        "33 HYSAN AVENUE, Wan Chai District, Hong Kong"
                )
        )

object Routes {
    const val ON_BOARDING = "onboarding"
    const val HOME = "home"
    const val VACANCY = "vacancy/{parkId}"
}

@Composable
fun MyApp(modifier: Modifier = Modifier) {

    val navController = rememberNavController()
    Surface(modifier) {
        NavHost(
                navController,
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
                Greetings(
                        carParks = carParkData,
                        onVacancyClicked = { parkId -> navController.navigate("vacancy/$parkId") }
                )
            }
            composable(Routes.VACANCY) { backStackEntry ->
                val parkId = backStackEntry.arguments?.getString("parkId")
                val targetCarPark = carParkData.find { it.parkId == parkId }
                if (targetCarPark != null) {
                    VacancyScreen(
                            carPark = targetCarPark,
                            onBackClicked = { navController.popBackStack() },
                            modifier = Modifier
                    )
                } else {
                    Text(
                            text = "Car Park Not Found",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
        }
    }
}

@Composable
fun OnboardingScreen(onContinueClicked: () -> Unit, modifier: Modifier = Modifier) {
    Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to Car Park Lists!")
        Button(modifier = Modifier.padding(vertical = 24.dp), onClick = onContinueClicked) {
            Text("Continue")
        }
    }
}

@Composable
private fun Greetings(
        modifier: Modifier = Modifier,
        carParks: List<CarPark>,
        onVacancyClicked: (String) -> Unit
) {
    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        items(items = carParks) { carPark ->
            Greeting(carPark = carPark, onVacancyClicked = { onVacancyClicked(carPark.parkId) })
        }
    }
}

@Composable
private fun Greeting(
        carPark: CarPark,
        onVacancyClicked: () -> Unit,
        modifier: Modifier = Modifier
) {
    Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) { CardContent(carPark, onVacancyClicked = onVacancyClicked) }
}

@Composable
private fun CardContent(carPark: CarPark, onVacancyClicked: () -> Unit) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Row(
            modifier =
                    Modifier.padding(12.dp)
                            .animateContentSize(
                                    animationSpec =
                                            spring(
                                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                                    stiffness = Spring.StiffnessLow
                                            )
                            )
    ) {
        Column(modifier = Modifier.weight(1f).padding(12.dp)) {
            Text(
                    text = carPark.nameEn,
                    style =
                            MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.ExtraBold
                            )
            )
            if (expanded) {
                Text(
                        text = "ID: ${carPark.parkId}",
                )
                Text(
                        text = "Name: ${carPark.nameEn}",
                )
                Text(
                        text = "Address: ${carPark.displayAddressEn}",
                )
            }
        }
        Button(
                onClick = onVacancyClicked,
                colors = ButtonDefaults.buttonColors(androidx.compose.ui.graphics.Color(0xFF1974C2))
        ) { Text("Vacancy") }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun OnboardingPreview() {
    CarparkTheme { OnboardingScreen(onContinueClicked = {}) }
}

@Preview(
        showBackground = true,
        widthDp = 320,
        uiMode = UI_MODE_NIGHT_YES,
        name = "GreetingPreviewDark"
)
@Preview(showBackground = true, widthDp = 320)
@Composable
fun GreetingPreview() {
    CarparkTheme { Greetings(modifier = Modifier, carParks = carParkData, onVacancyClicked = {}) }
}

@Preview
@Composable
fun MyAppPreview() {
    CarparkTheme { MyApp(Modifier.fillMaxSize()) }
}

@Composable
fun VacancyScreen(carPark: CarPark, onBackClicked: () -> Unit, modifier: Modifier) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
                text = "Name: ${carPark.nameEn}",
                style =
                        MaterialTheme.typography.headlineMedium.copy(
                                color = MaterialTheme.colorScheme.primary
                        ),
                modifier = Modifier.padding(vertical = 4.dp)
        )

        Text(
                text = "ID: ${carPark.parkId}",
                style =
                        MaterialTheme.typography.headlineMedium.copy(
                                color = MaterialTheme.colorScheme.primary
                        ),
                modifier = Modifier.padding(vertical = 4.dp)
        )

        Text(
                text = "Address: ${carPark.displayAddressEn}",
                style =
                        MaterialTheme.typography.headlineMedium.copy(
                                color = MaterialTheme.colorScheme.primary
                        ),
                modifier = Modifier.padding(vertical = 4.dp)
        )

        val vacancyCount = remember { Random.nextInt(0, 101) }
        Text(
                text = "Current Vacancy: $vacancyCount",
                style =
                        MaterialTheme.typography.headlineMedium.copy(
                                color = MaterialTheme.colorScheme.primary
                        ),
                modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun VacancyScreenPreview() {
    CarparkTheme {
        VacancyScreen(carPark = carParkData.first(), onBackClicked = {}, modifier = Modifier)
    }
}
