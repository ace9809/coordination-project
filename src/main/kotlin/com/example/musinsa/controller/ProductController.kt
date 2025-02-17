package com.example.musinsa.controller

import com.example.musinsa.model.Response
import com.example.musinsa.model.dto.request.product.CreateProductRequest
import com.example.musinsa.model.dto.request.product.UpdateProductRequest
import com.example.musinsa.model.dto.response.product.*
import com.example.musinsa.model.enums.CategoryType
import com.example.musinsa.service.ProductService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "상품 API (v1)")
@RequestMapping("/api/v1/products")
@RestController
class ProductController(
    private val productService: ProductService
) {
    @Operation(summary = "카테고리별 최저가격 브랜드의 상품 조회 API")
    @GetMapping("/category-min-prices")
    fun getCategoryMinPrices(): Response<GetCategoryMinPricesResponses> {
        return Response(true, productService.getCategoryMinPrices())
    }

    @Operation(summary = "최저가 브랜드의 모든 카테고리 상품 가격 및 총액 조회 API")
    @DeleteMapping("/min-total-price-brand")
    fun getMinTotalPriceBrand(): Response<GetMinTotalPriceBrandResponses> {
        return Response(true, productService.getMinTotalPriceBrand())
    }

    @Operation(summary = "특정 카테고리 최저, 최고 가격 브랜드의 상품 조회 API")
    @GetMapping("/min-max-price")
    fun getMinMaxPrice(
        @RequestParam category: CategoryType,
    ): Response<GetMinMaxPriceResponses> {
        return Response(true, productService.getMinMaxPrice(category))
    }

    @Operation(summary = "상품 생성 API")
    @PostMapping
    fun createProduct(
        @RequestBody createProductRequest: CreateProductRequest
    ): Response<CreateProductResponse> {
        return Response(true, productService.createProduct(createProductRequest))
    }

    @Operation(summary = "상품 수정 API")
    @PutMapping("/{productId}")
    fun updateProduct(
        @PathVariable productId: Long,
        @RequestBody updateProductRequest: UpdateProductRequest
    ): Response<UpdateProductResponse> {
        return Response(true, productService.updateProduct(productId, updateProductRequest))
    }

    @Operation(summary = "상품 삭제 API")
    @DeleteMapping("/{productId}")
    fun deleteProduct(
        @PathVariable productId: Long,
    ): Response<DeleteProductResponse> {
        return Response(true, productService.deleteProduct(productId))
    }
}