package ty.bre.stocklens.data.csv

import java.io.InputStream

/**
 * A generic interface for parsing CSV data.
 * This interface defines a contract for converting a CSV file into a list of objects of type [T].
 *
 * @param T The type of objects that the CSV data will be parsed into.
 */
interface CSVParser<T> {
    suspend fun parse(stream: InputStream): List<T>
}