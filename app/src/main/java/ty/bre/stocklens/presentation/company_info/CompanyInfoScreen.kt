package ty.bre.stocklens.presentation.company_info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ty.bre.stocklens.presentation.ThemeManagerViewModel
import ty.bre.stocklens.ui.theme.BoldRed
import ty.bre.stocklens.ui.theme.DarkColorScheme
import ty.bre.stocklens.ui.theme.DarkElegance
import ty.bre.stocklens.ui.theme.ModernMinimalist
import ty.bre.stocklens.ui.theme.SophisticatedBlue
import ty.bre.stocklens.ui.theme.roboto
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun CompanyInfoScreen(
    viewModel: CompanyInfoViewModel = hiltViewModel(),
    themeManagerViewModel: ThemeManagerViewModel = hiltViewModel(),
    symbol: String,
    companyName: String,
) {
    val state = viewModel.state
    val themes = listOf(DarkColorScheme, BoldRed, SophisticatedBlue, ModernMinimalist, DarkElegance)
    var currentThemeIndex by remember { mutableIntStateOf(themeManagerViewModel.getTheme()) }
    val currentTheme = themes[currentThemeIndex]
    val targetDate = when (LocalDate.now().dayOfWeek) {
        java.time.DayOfWeek.MONDAY -> LocalDate.now().minusDays(3)  // Last Friday
        java.time.DayOfWeek.SATURDAY -> LocalDate.now().minusDays(1)  // Last Friday
        java.time.DayOfWeek.SUNDAY -> LocalDate.now().minusDays(2)  // Last Friday
        else -> LocalDate.now().minusDays(1)  // Yesterday
    }

    if (state.error == null) {
        MaterialTheme(colorScheme = currentTheme) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
            ) {
                state.company?.let { company ->
                    Text(
                        text = companyName,
                        style = TextStyle(
                            fontFamily = roboto,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = symbol,
                        style = TextStyle(
                            fontFamily = roboto,
                            fontStyle = FontStyle.Italic,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    HorizontalDivider(modifier = Modifier.fillMaxWidth())

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (company.industry == "") "Industry: Data not available" else "Industry: ${company.industry}",
                        style = TextStyle(
                            fontFamily = roboto,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (company.country == "") "Country: Data not available" else "Country: ${company.country}",
                        style = TextStyle(
                            fontFamily = roboto,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    HorizontalDivider(modifier = Modifier.fillMaxWidth())

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (company.description == "") "Data not available" else company.description,
                        style = TextStyle(
                            fontFamily = roboto,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )


                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Market Summary: ${targetDate.format(DateTimeFormatter.ofPattern("dd:MM:yyyy"))}",
                        style = TextStyle(
                            fontFamily = roboto,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    StockChart(
                        infos = state.stockInfos,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .align(CenterHorizontally)
                    )

                }
            }
        }
    }
    MaterialTheme(colorScheme = currentTheme) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Center
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            } else if (state.error != null) {
                Text(text = state.error, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}