package ty.bre.stocklens.data.mapper

import ty.bre.stocklens.data.remote.dto.IntradayInfoDto
import ty.bre.stocklens.domain.model.IntradayInfoModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun IntradayInfoDto.toIntradayInfo(): IntradayInfoModel {
    val pattern = "yyyy-MM-dd HH:mm:ss"
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    val localDateTime = LocalDateTime.parse(timestamp, formatter)
    return IntradayInfoModel(
        date = localDateTime,
        close = close
    )
}