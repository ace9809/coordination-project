package com.example.musinsa.brand.entity

import jakarta.persistence.*

@Entity
@Table(name = "brands")
data class Brand(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,

    @Column(name = "name", columnDefinition = "varchar(25) not null")
    val name: String,
) : BaseEntity()
