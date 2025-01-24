package ty.bre.stocklens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ty.bre.stocklens.ui.theme.StockLensTheme
import ty.bre.stocklens.util.Screen
import ty.bre.stocklens.util.SetupNavGraph

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StockLensTheme {
                val navController = rememberNavController()
                SetupNavGraph(
                    navController = navController,
                    startDestination = Screen.CompanyListingsScreen
                )
            }
        }
    }
}