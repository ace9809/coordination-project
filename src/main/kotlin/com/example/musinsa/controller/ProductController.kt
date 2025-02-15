package com.example.musinsa.controller

import com.example.musinsa.model.Response
import com.example.musinsa.model.dto.request.ProductRequest
import com.example.musinsa.projection.repository.ProductRepository
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
}