package com.example.musinsa.product.model.dto.response.product

import io.swagger.v3.oas.annotations.media.Schema

data class GetMinTotalPriceBrandResponses(
    @Schema(description = "최저가 브랜드 상품 목록")
    val products: GetMinTotalPriceBrandResponse,
)