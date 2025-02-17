package com.example.musinsa.product.entity

import com.example.musinsa.product.model.enums.CategoryType
import jakarta.persistence.*

@Entity
@Table(name = "product_category_statistics")
data class ProductCategoryStatistic(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    val category: CategoryType,

    @Column(name = "min_brand_id")
    val minBrandId: Long,

    @Column(name = "min_product_id")
    val minProductId: Long,

    @Column(name = "min_price")
    val minPrice: Long,

    @Column(name = "max_brand_id")
    val maxBrandId: Long,

    @Column(name = "max_product_id")
    val maxProductId: Long,

    @Column(name = "max_price")
    val maxPrice: Long
) : BaseEntity()
