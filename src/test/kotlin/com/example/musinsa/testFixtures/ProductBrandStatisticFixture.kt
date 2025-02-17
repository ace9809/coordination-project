package com.example.musinsa.testFixtures

import com.example.musinsa.model.dto.ProductBrandStatisticDto
import org.mockito.ArgumentMatchers.anyLong

class ProductBrandStatisticFixture {
    companion object {
        fun generate(
            id: Long = anyLong(),
            brandId: Long = anyLong(),
            totalPrice: Long = anyLong()
        ) = ProductBrandStatisticDto(
            id = id,
            brandId = brandId,
            totalPrice = totalPrice
        )
    }
}
