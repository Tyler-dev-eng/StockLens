package ty.bre.stocklens.data.repository

import android.app.Application
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import ty.bre.stocklens.data.csv.CSVParser
import ty.bre.stocklens.data.local.StockDatabase
import ty.bre.stocklens.data.mapper.toCompanyInfo
import ty.bre.stocklens.data.mapper.toCompanyListing
import ty.bre.stocklens.data.mapper.toCompanyListingEntity
import ty.bre.stocklens.data.remote.StockApi
import ty.bre.stocklens.domain.model.CompanyInfoModel
import ty.bre.stocklens.domain.model.CompanyListing
import ty.bre.stocklens.domain.model.IntradayInfoModel
import ty.bre.stocklens.domain.repository.StockRepository
import ty.bre.stocklens.util.Resource
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [StockRepository] responsible for handling data operations.
 * Fetches data from local database or remote API, based on the provided parameters.
 *
 * @param api The [StockApi] for making remote API calls.
 * @param db The local [StockDatabase] for caching data.
 * @param parser The [CSVParser] for parsing company listings from CSV responses.
 * @param intradayInfoParser The [CSVParser] for parsing intraday info data from CSV responses.
 */
@Singleton
class StockRepositoryImpl @Inject constructor(
    private val api: StockApi,
    db: StockDatabase,
    private val parser: CSVParser<CompanyListing>,
    private val intradayInfoParser: CSVParser<IntradayInfoModel>,
    private val app: Application
) : StockRepository {

    private val dao = db.dao

    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {
            emit(Resource.Loading(true))
            val localListings = dao.searchCompanyListing(query)
            emit(
                Resource.Success(
                    data = localListings.map { it.toCompanyListing() }
                ))

            val isDbEmpty = localListings.isEmpty() && query.isBlank()
            val loadFromCache = !isDbEmpty && !fetchFromRemote
            if (loadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }
            val remoteListings = try {
                val response = api.getListings()
                parser.parse(response.byteStream())
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            }

            remoteListings?.let { listings ->
                dao.clearCompanyListings()
                dao.insertCompanyListings(listings.map { it.toCompanyListingEntity() })
                emit(
                    Resource.Success(
                        data = dao
                            .searchCompanyListing("")
                            .map { it.toCompanyListing() }
                    )
                )
                emit(Resource.Loading(false))
            }
        }
    }

    override suspend fun getIntradayInfo(symbol: String): Resource<List<IntradayInfoModel>> {
        return try {
            val response = api.getIntradayInfo(symbol)
            val results = intradayInfoParser.parse(response.byteStream())
            Resource.Success(results)
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load intraday info"
            )
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load intraday info"
            )
        }
    }

    override suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfoModel> {
        return try {
            val result = api.getCompanyInfo(symbol)
            Resource.Success(result.toCompanyInfo())
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load company info"
            )
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load company info"
            )
        }
    }
}