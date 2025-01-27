package ty.bre.stocklens.domain.model

import java.time.LocalDateTime

data class IntradayInfoModel(
    val date: LocalDateTime,
    val close: Double
)
