package ty.bre.stocklens.presentation.company_info

import ty.bre.stocklens.domain.model.CompanyInfoModel
import ty.bre.stocklens.domain.model.IntradayInfoModel

data class CompanyInfoState(
    val stockInfos: List<IntradayInfoModel> = emptyList(),
    val company: CompanyInfoModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
