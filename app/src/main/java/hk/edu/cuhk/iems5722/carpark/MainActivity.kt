package hk.edu.cuhk.iems5722.carpark

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import hk.edu.cuhk.iems5722.carpark.ui.CarparkApp
import hk.edu.cuhk.iems5722.carpark.ui.theme.CarparkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        
        super.onCreate(savedInstanceState)
        
        setContent {
            CarparkTheme {
                val view = LocalView.current
                if (!view.isInEditMode) {
                    SideEffect {
                        val window = (view.context as? ComponentActivity)?.window
                        window?.let {
                            val insetsController = WindowCompat.getInsetsController(it, view)
                            insetsController?.apply {
                                hide(WindowInsetsCompat.Type.statusBars())
                                systemBarsBehavior =
                                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                            }
                        }
                    }
                }
                
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
