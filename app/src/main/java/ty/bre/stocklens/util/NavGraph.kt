package ty.bre.stocklens.util

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ty.bre.stocklens.presentation.company_listings.CompanyListingsScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    startDestination: Screen,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Screen.CompanyListingsScreen> {
            CompanyListingsScreen(navController = navController)
        }
    }
}