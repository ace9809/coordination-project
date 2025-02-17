package com.example.musinsa.testFixtures

import com.example.musinsa.model.dto.BrandDto
import com.example.musinsa.testFixtures.Fixture.fixture
import org.mockito.ArgumentMatchers.anyLong

class BrandFixture {
    companion object {
        fun generate(
            id: Long = anyLong(),
            name: String = fixture()
        ) = BrandDto(
            id = id,
            name = name
        )
    }
}