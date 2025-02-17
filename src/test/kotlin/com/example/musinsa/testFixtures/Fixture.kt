package com.example.musinsa.testFixtures

import com.appmattus.kotlinfixture.decorator.nullability.NeverNullStrategy
import com.appmattus.kotlinfixture.decorator.nullability.nullabilityStrategy
import com.appmattus.kotlinfixture.decorator.optional.NeverOptionalStrategy
import com.appmattus.kotlinfixture.decorator.optional.optionalStrategy
import com.appmattus.kotlinfixture.kotlinFixture

object Fixture {
    val fixture = kotlinFixture() {
        nullabilityStrategy(NeverNullStrategy)
        optionalStrategy(NeverOptionalStrategy)
    }
}