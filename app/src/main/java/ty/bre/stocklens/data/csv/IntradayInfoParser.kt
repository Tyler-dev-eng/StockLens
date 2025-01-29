package ty.bre.stocklens.data.csv

import com.opencsv.CSVReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ty.bre.stocklens.data.mapper.toIntradayInfo
import ty.bre.stocklens.data.remote.dto.IntradayInfoDto
import ty.bre.stocklens.domain.model.IntradayInfoModel
import java.io.InputStream
import java.io.InputStreamReader
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A parser for reading and converting CSV data into a list of CompanyListing objects.
 * This implementation uses the OpenCSV library to handle the CSV format.
 * The class is designed as a Singleton and is injected wherever needed using Dagger/Hilt.
 */
@Singleton
class IntradayInfoParser @Inject constructor() : CSVParser<IntradayInfoModel> {

    /**
     * Parses the input CSV stream and converts it into a list of [IntradayInfoModel].
     *
     * @param stream The input stream of the CSV file to be parsed.
     * @return A list of [IntradayInfoModel] objects filtered and sorted by specific criteria.
     */
    override suspend fun parse(stream: InputStream): List<IntradayInfoModel> {
        val csvReader = CSVReader(InputStreamReader(stream))
        val targetDate = when (LocalDate.now().dayOfWeek) {
            java.time.DayOfWeek.MONDAY -> LocalDate.now().minusDays(3)  // Last Friday
            java.time.DayOfWeek.SATURDAY -> LocalDate.now().minusDays(1)  // Last Friday
            java.time.DayOfWeek.SUNDAY -> LocalDate.now().minusDays(2)  // Last Friday
            else -> LocalDate.now().minusDays(1)  // Yesterday
        }

        return withContext(Dispatchers.IO) {
            csvReader
                .readAll()
                .drop(1)
                .mapNotNull { line ->
                    val timestamp = line.getOrNull(0) ?: return@mapNotNull null
                    val close = line.getOrNull(4) ?: return@mapNotNull null
                    val dto = IntradayInfoDto(timestamp, close.toDouble())
                    dto.toIntradayInfo()
                }
                .filter {
                    it.date.toLocalDate() == targetDate
                }
                .sortedBy {
                    it.date.hour
                }
                .also {
                    csvReader.close()
                }
        }
    }
}