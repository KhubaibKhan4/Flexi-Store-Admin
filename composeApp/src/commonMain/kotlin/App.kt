import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import di.appModule
import org.koin.compose.KoinApplication
import presentation.screens.dashboard.Dashboard

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun App() {
    KoinApplication(
        application = {
            modules(appModule)
        }
    ) {
        val windowSizeClass = calculateWindowSizeClass()
        MaterialTheme {
            Dashboard(windowSizeClass)
        }
    }
}