package ty.bre.stocklens.domain.repository

import kotlinx.coroutines.flow.Flow
import ty.bre.stocklens.domain.model.CompanyInfoModel
import ty.bre.stocklens.domain.model.CompanyListing
import ty.bre.stocklens.domain.model.IntradayInfoModel
import ty.bre.stocklens.util.Resource

interface StockRepository {
    suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>>

    suspend fun getIntradayInfo(
        symbol: String
    ): Resource<List<IntradayInfoModel>>

    suspend fun getCompanyInfo(
        symbol: String
    ): Resource<CompanyInfoModel>
}