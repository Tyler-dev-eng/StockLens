package ty.bre.stocklens.data.remote

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query
import ty.bre.stocklens.BuildConfig

interface StockApi {

    @GET("query?function=LISTING_STATUS")
    suspend fun getListings(
        @Query("apikey") apikey: String = BuildConfig.API_KEY
    ): ResponseBody
}