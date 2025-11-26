package hk.edu.cuhk.iems5722.carpark

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import hk.edu.cuhk.iems5722.carpark.ui.CarparkApp
import hk.edu.cuhk.iems5722.carpark.ui.theme.CarparkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarparkTheme {
                CarparkApp(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CarparkAppPreview() {
    CarparkTheme {
        CarparkApp(Modifier.fillMaxSize())
    }
}
