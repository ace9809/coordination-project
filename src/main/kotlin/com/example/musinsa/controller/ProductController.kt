package com.example.musinsa.controller

import com.example.musinsa.model.Response
import com.example.musinsa.model.dto.request.CreateProductRequest
import com.example.musinsa.model.dto.request.UpdateProductRequest
import com.example.musinsa.model.dto.response.CreateProductResponse
import com.example.musinsa.model.dto.response.DeleteProductResponse
import com.example.musinsa.model.dto.response.UpdateProductResponse
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
    @GetMapping("/categories/lowest-price")
    fun getLowestPriceByCategories() {

    }

//    @Operation(summary = "최저가 브랜드의 모든 카테고리 상품 가격 및 총액 조회 API")
//    @DeleteMapping("/brands/lowest-total")
//    fun getBrandWitLowestTotal()
//
//    @GetMapping("/categories/min-max-brands")
//    fun getMinMaxPriceBrand()

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