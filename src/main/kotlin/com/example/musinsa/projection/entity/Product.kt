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
    @Column(name = "category", columnDefinition = "varchar(25) not null")
    val category: CategoryType,

    @Column(name = "brand", columnDefinition = "varchar(25) not null")
    val brand: String,

    @Column(name = "price",columnDefinition = "varchar(25) not null")
    val price: Long
) : BaseEntity()
