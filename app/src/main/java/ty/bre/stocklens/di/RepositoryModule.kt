package ty.bre.stocklens.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ty.bre.stocklens.data.csv.CSVParser
import ty.bre.stocklens.data.csv.CompanyListingsParser
import ty.bre.stocklens.data.csv.IntradayInfoParser
import ty.bre.stocklens.data.repository.StockRepositoryImpl
import ty.bre.stocklens.domain.model.CompanyListing
import ty.bre.stocklens.domain.model.IntradayInfoModel
import ty.bre.stocklens.domain.repository.StockRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCompanyListingsParser(
        companyListingsParser: CompanyListingsParser
    ): CSVParser<CompanyListing>

    @Binds
    @Singleton
    abstract fun bindIntradayInfoParser(
        intradayInfoParser: IntradayInfoParser
    ): CSVParser<IntradayInfoModel>

    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepositoryImpl: StockRepositoryImpl
    ): StockRepository

}