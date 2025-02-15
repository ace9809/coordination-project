package com.example.musinsa.projection.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "product_statistics")
data class ProductStatistics(
    @Column(name = "category", columnDefinition = "varchar(25) not null")
    val category: String,

    @Column(name = "min_brand", columnDefinition = "varchar(25) not null")
    val minBrand: String,

    @Column(name = "min_price")
    val minPrice: Long,

    @Column(name = "max_brand", columnDefinition = "varchar(25) not null")
    val maxBrand: String,

    @Column(name = "max_price")
    val maxPrice: Long,

    @Column(name = "total_price")
    val totalPrice: Long,
) : BaseEntity()
