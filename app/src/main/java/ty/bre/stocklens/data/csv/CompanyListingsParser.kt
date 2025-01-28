package ty.bre.stocklens.data.csv

import com.opencsv.CSVReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ty.bre.stocklens.domain.model.CompanyListing
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A parser for reading and converting CSV data into a list of CompanyListing objects.
 * This implementation uses the OpenCSV library to handle the CSV format.
 * The class is designed as a Singleton and is injected wherever needed using Dagger/Hilt.
 */
@Singleton
class CompanyListingsParser @Inject constructor() : CSVParser<CompanyListing> {

    /**
     * Parses the input CSV stream and converts it into a list of [CompanyListing] objects.
     *
     * @param stream The input stream of the CSV file to be parsed.
     * @return A list of [CompanyListing] objects extracted from the CSV file.
     */
    override suspend fun parse(stream: InputStream): List<CompanyListing> {
        val csvReader = CSVReader(InputStreamReader(stream))
        return withContext(Dispatchers.IO) {
            csvReader
                .readAll()
                .drop(1)
                .mapNotNull { line ->
                    val symbol = line.getOrNull(0)
                    val name = line.getOrNull(1)
                    val exchange = line.getOrNull(2)
                    CompanyListing(
                        name = name ?: return@mapNotNull null,
                        symbol = symbol ?: return@mapNotNull null,
                        exchange = exchange ?: return@mapNotNull null
                    )
                }
                .also { csvReader.close() }
        }
    }
}