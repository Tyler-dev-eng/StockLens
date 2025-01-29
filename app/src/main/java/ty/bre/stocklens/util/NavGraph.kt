package ty.bre.stocklens.util

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import ty.bre.stocklens.presentation.company_info.CompanyInfoScreen
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

        composable<Screen.CompanyInfoScreen> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.CompanyInfoScreen>()
            CompanyInfoScreen(symbol = args.symbol, companyName = args.companyName)
        }
    }
}