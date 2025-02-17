package com.example.musinsa.projection.entity

import jakarta.persistence.*

@Entity
@Table(name = "product_brand_statistics")
data class ProductBrandStatistic(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,

    @Column(name = "brand_id")
    val brandId: Long,

    @Column(name = "total_price")
    val totalPrice: Long
) : BaseEntity()
