package ty.bre.stocklens.presentation.company_info

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ty.bre.stocklens.domain.repository.StockRepository
import ty.bre.stocklens.util.Resource
import javax.inject.Inject

@HiltViewModel
class CompanyInfoViewModel @Inject constructor(
    private val repository: StockRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var state by mutableStateOf(CompanyInfoState())

    init {
        viewModelScope.launch {
            val symbol = savedStateHandle.get<String>("symbol") ?: return@launch
            state = state.copy(isLoading = true)
            val companyInfoResult = async { repository.getCompanyInfo(symbol = symbol) }
            val intradayInfoResult = async { repository.getIntradayInfo(symbol = symbol) }
            when (val result = companyInfoResult.await()) {
                is Resource.Error -> {
                    state = state.copy(
                        company = null,
                        isLoading = false,
                        error = result.message
                    )
                }

                is Resource.Success -> {
                    state = state.copy(
                        company = result.data,
                        isLoading = false,
                        error = null
                    )
                }

                else -> Unit
            }

            when (val result = intradayInfoResult.await()) {
                is Resource.Error -> {
                    state = state.copy(
                        stockInfos = emptyList(),
                        isLoading = false,
                        error = result.message
                    )
                }

                is Resource.Success -> {
                    state = state.copy(
                        stockInfos = result.data ?: emptyList(),
                        isLoading = false,
                        error = null
                    )
                }

                else -> Unit
            }
        }
    }
}