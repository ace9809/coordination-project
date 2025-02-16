package com.example.musinsa.model.dto.response.product

import com.example.musinsa.model.enums.CategoryType
import io.swagger.v3.oas.annotations.media.Schema

data class GetMinMaxPriceResponses(
    @Schema(description = "카테고리", example = "상의")
    val category: CategoryType,
    @Schema(description = "최저가 상품")
    val minProduct: GetMinMaxPriceResponse,
    @Schema(description = "최고가 상품")
    val maxProduct: GetMinMaxPriceResponse,
)