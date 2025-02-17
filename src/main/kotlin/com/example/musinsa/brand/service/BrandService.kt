package com.example.musinsa.brand.service

import com.example.musinsa.brand.domain.BrandDomainService
import com.example.musinsa.brand.model.dto.request.brand.CreateBrandRequest
import com.example.musinsa.brand.model.dto.request.brand.UpdateBrandRequest
import com.example.musinsa.brand.model.dto.response.brand.CreateBrandResponse
import com.example.musinsa.brand.model.dto.response.brand.DeleteBrandResponse
import com.example.musinsa.brand.model.dto.response.brand.UpdateBrandResponse
import com.example.musinsa.exception.BrandError
import com.example.musinsa.exception.BrandException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BrandService(
    private val brandDomainService: BrandDomainService
) {
    @Transactional
    fun createBrand(createBrandRequest: CreateBrandRequest): CreateBrandResponse {
        if (brandDomainService.existsByName(createBrandRequest.name)) throw BrandException(BrandError.DUPLICATE_NAME_EXCEPTION)
        val brand = brandDomainService.save(createBrandRequest.toBrandDto())
        return CreateBrandResponse(
            id = brand.id,
            name = brand.name
        )
    }

    @Transactional
    fun updateBrand(brandId: Long, updateBrandRequest: UpdateBrandRequest): UpdateBrandResponse {
        brandDomainService.getBrand(brandId) ?: throw BrandException(BrandError.NOT_FOUND_BRAND_EXCEPTION)
        if (brandDomainService.existsByName(updateBrandRequest.name)) throw BrandException(BrandError.DUPLICATE_NAME_EXCEPTION)

        val updateBrand = brandDomainService.save(updateBrandRequest.toBrandDto(brandId))
        return UpdateBrandResponse(
            id = updateBrand.id,
            name = updateBrand.name
        )
    }

    // TODO: 추후 요구사항에 따라서 브랜드 삭제시 상품 삭제 및 통계 테이블 어떻게 할지 결정
    @Transactional
    fun deleteBrand(brandId: Long): DeleteBrandResponse {
        val brand = brandDomainService.getBrand(brandId) ?: throw BrandException(BrandError.NOT_FOUND_BRAND_EXCEPTION)
        val deleteProductId = brandDomainService.deleteBrand(brand.id)
        return DeleteBrandResponse(
            brandId = deleteProductId
        )
    }
}