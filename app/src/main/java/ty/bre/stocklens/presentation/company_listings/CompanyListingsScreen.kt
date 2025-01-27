package ty.bre.stocklens.presentation.company_listings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import ty.bre.stocklens.R
import ty.bre.stocklens.ui.theme.BoldRed
import ty.bre.stocklens.ui.theme.DarkColorScheme
import ty.bre.stocklens.ui.theme.DarkElegance
import ty.bre.stocklens.ui.theme.ModernMinimalist
import ty.bre.stocklens.ui.theme.SophisticatedBlue
import ty.bre.stocklens.util.Screen
import kotlin.math.roundToInt

@Composable
fun CompanyListingsScreen(
    navController: NavHostController,
    viewModel: CompanyListingsViewModel = hiltViewModel()
) {
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = viewModel.state.isRefreshing)
    val state = viewModel.state

    // State to track the FABs position
    var fabOffsetX by remember { mutableFloatStateOf(0f) }
    var fabOffsetY by remember { mutableFloatStateOf(0f) }

    // List of available themes
    val themes = listOf(DarkColorScheme, BoldRed, SophisticatedBlue, ModernMinimalist, DarkElegance)
    var currentThemeIndex by remember { mutableIntStateOf(0) }

    // Get the current theme
    val currentTheme = themes[currentThemeIndex]

    MaterialTheme(colorScheme = currentTheme) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = {
                        viewModel.onEvent(CompanyListingsEvent.OnSearchQueryChange(it))
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    placeholder = {
                        Text(text = "Search...")
                    },
                    leadingIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.search),
                            contentDescription = "Search",
                            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
                        )
                    },
                    maxLines = 1,
                    singleLine = true
                )
                SwipeRefresh(
                    state = swipeRefreshState,
                    onRefresh = {
                        viewModel.onEvent(CompanyListingsEvent.Refresh)
                    }
                ) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(state.companies.size) { i ->
                            val company = state.companies[i]
                            CompanyItem(
                                company = company, modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        navController.navigate(Screen.CompanyInfoScreen(symbol = company.symbol))
                                    }
                                    .padding(16.dp)
                            )
                            if (i < state.companies.size) {
                                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                            }
                        }
                    }
                }
            }

            // Draggable FAB
            FloatingActionButton(
                onClick = {
                    currentThemeIndex =
                        (currentThemeIndex + 1) % themes.size // Cycle through themes
                },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .offset { IntOffset(fabOffsetX.roundToInt(), fabOffsetY.roundToInt()) }
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume() // Consume the gesture
                            fabOffsetX += dragAmount.x
                            fabOffsetY += dragAmount.y
                        }
                    }
                    .padding(16.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.theme_selector),
                    contentDescription = "Draggable FAB",
                    tint = White
                )
            }
        }
    }
}