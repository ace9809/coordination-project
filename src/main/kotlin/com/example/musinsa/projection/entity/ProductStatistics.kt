package com.example.musinsa.projection.entity

import com.example.musinsa.model.enums.CategoryType
import jakarta.persistence.*

@Entity
@Table(name = "product_statistics")
data class ProductStatistics(
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
