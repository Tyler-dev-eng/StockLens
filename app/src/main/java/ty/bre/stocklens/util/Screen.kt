package ty.bre.stocklens.util

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object CompanyListingsScreen : Screen()

    @Serializable
    data class CompanyInfoScreen(
        val symbol: String,
        val companyName: String,
    ) : Screen()
}