package com.example.musinsa.brand

import com.example.musinsa.domain.BrandDomainService
import com.example.musinsa.service.BrandService
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.mockk
import io.mockk.spyk

class BrandServiceTest : BehaviorSpec({

    val brandDomainService = mockk<BrandDomainService>()

    val sut = spyk(
        BrandService(brandDomainService),
        recordPrivateCalls = true
    )

})