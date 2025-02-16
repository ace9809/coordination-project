package com.example.musinsa.domain

import com.example.musinsa.model.dto.BrandDto
import com.example.musinsa.projection.repository.BrandRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class BrandDomainService(
    private val brandRepository: BrandRepository
) {
    fun getBrand(brandId: Long): BrandDto? {
        val brand = brandRepository.findByIdOrNull(brandId) ?: return null
        return BrandDto.of(brand)
    }

    fun save(brandDto: BrandDto): BrandDto {
        return BrandDto.of(brandRepository.save(brandDto.toBrand()))
    }

    fun existsByName(name: String): Boolean {
        return brandRepository.existsByName(name)
    }

    fun deleteBrand(brandId: Long): Long {
        brandRepository.deleteById(brandId)

        return brandId
    }
}