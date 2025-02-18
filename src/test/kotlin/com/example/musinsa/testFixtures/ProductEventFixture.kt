package com.example.musinsa.testFixtures

import com.example.musinsa.product.model.dto.ProductDto
import com.example.musinsa.product.model.dto.ProductEventDto
import com.example.musinsa.product.model.enums.ProductEventType
import com.example.musinsa.testFixtures.Fixture.fixture

class ProductEventFixture {
    companion object {
        fun generate(
            product: ProductDto = fixture(),
            prevProduct: ProductDto? = null,
            type: ProductEventType = fixture()
        ) = ProductEventDto(
            product = product,
            prevProduct = prevProduct,
            type = type
        )
    }
}