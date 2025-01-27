package ty.bre.stocklens.domain.model

data class CompanyInfoModel(
    val symbol: String,
    val description: String,
    val name: String,
    val country: String,
    val industry: String,
)
