package ty.bre.stocklens.data.csv

import com.opencsv.CSVReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ty.bre.stocklens.data.mapper.toIntradayInfoModel
import ty.bre.stocklens.data.remote.dto.IntradayInfoDto
import ty.bre.stocklens.domain.model.IntradayInfoModel
import java.io.InputStream
import java.io.InputStreamReader
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IntradayInfoParser @Inject constructor() : CSVParser<IntradayInfoModel> {
    override suspend fun parse(stream: InputStream): List<IntradayInfoModel> {
        val csvReader = CSVReader(InputStreamReader(stream))
        return withContext(Dispatchers.IO) {
            csvReader
                .readAll()
                .drop(1)
                .mapNotNull { line ->
                    val timestamp = line.getOrNull(0) ?: return@mapNotNull null
                    val close = line.getOrNull(4) ?: return@mapNotNull null
                    val dto = IntradayInfoDto(timestamp = timestamp, close = close.toDouble())
                    dto.toIntradayInfoModel()
                }
                .filter { it.date.dayOfMonth == LocalDateTime.now().minusDays(1).dayOfMonth }
                .sortedBy { it.date.hour }
                .also { csvReader.close() }
        }
    }
}