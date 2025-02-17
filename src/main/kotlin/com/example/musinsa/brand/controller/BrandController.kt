package com.example.musinsa.brand.controller

import com.example.musinsa.Response
import com.example.musinsa.brand.model.dto.request.brand.CreateBrandRequest
import com.example.musinsa.brand.model.dto.request.brand.UpdateBrandRequest
import com.example.musinsa.brand.model.dto.response.brand.CreateBrandResponse
import com.example.musinsa.brand.model.dto.response.brand.DeleteBrandResponse
import com.example.musinsa.brand.model.dto.response.brand.UpdateBrandResponse
import com.example.musinsa.brand.service.BrandService
import com.example.musinsa.product.model.dto.response.product.DeleteProductResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "브랜드 API (v1)")
@RequestMapping("/api/v1/brands")
@RestController
class BrandController(
    private val brandService: BrandService
) {
    @Operation(summary = "브랜드 생성 API")
    @PostMapping
    fun createBrand(
        @RequestBody createBrandRequest: CreateBrandRequest
    ): Response<CreateBrandResponse> {
        return Response(true, brandService.createBrand(createBrandRequest))
    }

    @Operation(summary = "브랜드 수정 API")
    @PutMapping("/{brandId}")
    fun updateBrand(
        @PathVariable brandId: Long,
        @RequestBody updateBrandRequest: UpdateBrandRequest
    ): Response<UpdateBrandResponse> {
        return Response(true, brandService.updateBrand(brandId, updateBrandRequest))
    }

    @Operation(summary = "브랜드 삭제 API")
    @DeleteMapping("/{brandId}")
    fun deleteBrand(
        @PathVariable brandId: Long,
    ): Response<DeleteBrandResponse> {
        return Response(true, brandService.deleteBrand(brandId))
    }
}