package ty.bre.stocklens.util
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object CompanyListingsScreen: Screen()
}