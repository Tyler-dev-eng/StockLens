package ty.bre.stocklens.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ty.bre.stocklens.BuildConfig
import ty.bre.stocklens.data.local.StockDatabase
import ty.bre.stocklens.data.remote.StockApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideStockApi() : StockApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(StockApi::class.java)
    }

    @Provides
    @Singleton
    fun provideStockDatabase(app: Application) : StockDatabase {
        return Room.databaseBuilder(
            context = app,
            klass = StockDatabase::class.java,
            name = "stockdb.db"
        ).build()
    }
}