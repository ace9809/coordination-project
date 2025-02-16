package com.example.musinsa.projection.entity

import com.example.musinsa.model.enums.CategoryType
import jakarta.persistence.*

@Entity
@Table(name = "products")
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    val category: CategoryType,

    @Column(name = "brand_id")
    val brandId: Long,

    @Column(name = "price")
    val price: Long
) : BaseEntity()
