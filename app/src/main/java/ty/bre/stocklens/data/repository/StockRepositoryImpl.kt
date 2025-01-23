package ty.bre.stocklens.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import ty.bre.stocklens.data.local.StockDatabase
import ty.bre.stocklens.data.mapper.toCompanyListing
import ty.bre.stocklens.data.remote.StockApi
import ty.bre.stocklens.domain.model.CompanyListing
import ty.bre.stocklens.domain.repository.StockRepository
import ty.bre.stocklens.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    val api : StockApi,
    db : StockDatabase,
): StockRepository {

    private val dao = db.dao

    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {
            emit(Resource.Loading(true))
            val localListings = dao.searchCompanyListing(query)
            emit(Resource.Success(
                data = localListings.map { it.toCompanyListing() }
            ))

            val isDbEmpty = localListings.isEmpty() && query.isBlank()
            val loadFromCache = !isDbEmpty && !fetchFromRemote
            if(loadFromCache){
                emit(Resource.Loading(false))
                return@flow
            }
            val remoteListings = try {
                val response = api.getListings()
                response.byteStream()
            } catch (e: IOException){
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
            } catch (e: HttpException){
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
            }
        }
    }
}