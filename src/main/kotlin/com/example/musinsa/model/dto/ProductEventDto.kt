package com.example.musinsa.model.dto

import com.example.musinsa.model.enums.ProductEventType

data class ProductEventDto(
    val product: ProductDto,
    val type: ProductEventType
)