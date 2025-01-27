package ty.bre.stocklens.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

//Classic Green (Growth-Oriented)
val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF2ECC71),
    background = Color(0xFF1C1C1E),
    onPrimary = Color(0xFFFFFFFF),
    onBackground = Color(0xFFD1D1D6)
)

//Bold Red (Risk/Profit Focus)
val BoldRed = darkColorScheme(
    primary = Color(0xFFE74C3C),
    background = Color(0xFF2B2B2B),
    onPrimary = Color(0xFFFFFFFF),
    onBackground = Color(0xFFF4F4F4)
)

//Sophisticated Blue (Trust and Stability)
val SophisticatedBlue = darkColorScheme(
    primary = Color(0xFF3498DB),
    background = Color(0xFF1B2430),
    onPrimary = Color(0xFFFFFFFF),
    onBackground = Color(0xFFBDC3C7)
)

//Modern Minimalist (Neutral)
val ModernMinimalist = darkColorScheme(
    primary = Color(0xFFF39C12),
    background = Color(0xFF121212),
    onPrimary = Color(0xFF000000),
    onBackground = Color(0xFFE0E0E0)
)

//Dark Elegance (Premium Look)
val DarkElegance = darkColorScheme(
    primary = Color(0xFF8E44AD),
    background = Color(0xFF101820),
    onPrimary = Color(0xFFFFFFFF),
    onBackground = Color(0xFFAAB7C4)
)

@Composable
fun StockLensTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}