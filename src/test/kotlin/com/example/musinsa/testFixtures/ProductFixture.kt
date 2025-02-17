package com.example.musinsa.testFixtures

import com.example.musinsa.model.dto.ProductDto
import com.example.musinsa.model.enums.CategoryType
import com.example.musinsa.testFixtures.Fixture.fixture
import org.mockito.ArgumentMatchers.anyLong

class ProductFixture {
    companion object {
        fun generate(
            id: Long = anyLong(),
            category: CategoryType = fixture(),
            brandId: Long = anyLong(),
            price: Long = anyLong(),
        ) = ProductDto(
            id = id,
            category = category,
            brandId = brandId,
            price = price
        )
    }
}