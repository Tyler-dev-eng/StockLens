package ty.bre.stocklens.data.mapper

import ty.bre.stocklens.data.local.CompanyListingEntity
import ty.bre.stocklens.data.remote.dto.CompanyInfoDto
import ty.bre.stocklens.domain.model.CompanyInfoModel
import ty.bre.stocklens.domain.model.CompanyListing

fun CompanyListingEntity.toCompanyListing(): CompanyListing {
    return CompanyListing(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyListing.toCompanyListingEntity(): CompanyListingEntity {
    return CompanyListingEntity(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyInfoDto.toCompanyInfo(): CompanyInfoModel {
    return CompanyInfoModel(
        symbol = symbol ?: "",
        description = description ?: "",
        name = name ?: "",
        country = country ?: "",
        industry = industry ?: ""
    )
}