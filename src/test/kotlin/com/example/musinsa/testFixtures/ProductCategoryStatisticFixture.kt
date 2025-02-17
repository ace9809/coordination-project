package com.example.musinsa.testFixtures

import com.example.musinsa.model.dto.ProductCategoryStatisticDto
import com.example.musinsa.model.enums.CategoryType
import com.example.musinsa.projection.entity.ProductCategoryStatistic
import com.example.musinsa.testFixtures.Fixture.fixture
import org.mockito.ArgumentMatchers.anyLong

class ProductCategoryStatisticFixture {
    companion object {
        fun generate(
            id: Long = anyLong(),
            category: CategoryType = fixture(),
            minBrandId: Long = anyLong(),
            minProductId: Long = anyLong(),
            minPrice: Long = anyLong(),
            maxBrandId: Long = anyLong(),
            maxProductId: Long = anyLong(),
            maxPrice: Long = anyLong(),
        ) = ProductCategoryStatisticDto(
            id = id,
            category = category,
            minBrandId = minBrandId,
            minPrice = minPrice,
            minProductId = minProductId,
            maxBrandId = maxBrandId,
            maxPrice = maxPrice,
            maxProductId = maxProductId
        )
    }
}