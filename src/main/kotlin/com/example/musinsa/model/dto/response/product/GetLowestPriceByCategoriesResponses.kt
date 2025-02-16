package com.example.musinsa.model.dto.response.product

import io.swagger.v3.oas.annotations.media.Schema

data class GetLowestPriceByCategoriesResponses(
    @Schema(description = "카테고리 별 최저가 상품 리스트")
    val products: List<GetLowestPriceByCategoriesResponse>,
    val totalPrice: Long
)