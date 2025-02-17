package com.example.musinsa.brand

import com.example.musinsa.exception.BrandException
import com.example.musinsa.brand.model.dto.request.brand.CreateBrandRequest
import com.example.musinsa.brand.model.dto.request.brand.UpdateBrandRequest
import com.example.musinsa.brand.model.dto.response.brand.CreateBrandResponse
import com.example.musinsa.brand.model.dto.response.brand.UpdateBrandResponse
import com.example.musinsa.product.model.dto.response.product.DeleteProductResponse
import com.example.musinsa.testFixtures.BrandFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk

class BrandServiceTest : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    val brandDomainService = mockk<BrandDomainService>(relaxed = true)
    val sut = spyk(BrandService(brandDomainService), recordPrivateCalls = true)

    given("새로운 브랜드 생성 요청이 들어오면") {
        val createBrandRequest = CreateBrandRequest(name = "나이스 고스트 클럽")
        val brandDto = BrandFixture.generate(name = createBrandRequest.name)
        val savedBrandDto = brandDto.copy(id = 1L)
        val brandResponse = CreateBrandResponse(id = 1L, name = createBrandRequest.name)

        every { brandDomainService.existsByName(createBrandRequest.name) } returns false
        every { brandDomainService.save(brandDto) } returns savedBrandDto

        `when`("새 브랜드를 생성할 때") {
            val response = sut.createBrand(createBrandRequest)
            then("브랜드가 성공적으로 생성되어야 한다.") {
                response shouldBe brandResponse
            }
        }

        `when`("중복된 브랜드 이름으로 새로운 브랜드를 생성하려 할 때") {
            every { brandDomainService.existsByName(createBrandRequest.name) } returns true
            then("exception이 발생해야 한다.") {
                val exception = shouldThrow<BrandException> {
                    sut.createBrand(createBrandRequest)
                }
                exception.message shouldBe "이미 존재하는 브랜드 이름입니다."
            }
        }
    }

    /*** 브랜드 업데이트 테스트 ***/
    given("브랜드를 업데이트할 때") {
        val brandId = 1L
        val existingBrandDto = BrandFixture.generate(id = brandId, name = "기존 브랜드")
        val updateBrandRequest = UpdateBrandRequest(name = "업데이트된 브랜드")
        val updatedBrandDto = existingBrandDto.copy(name = updateBrandRequest.name)
        val updateBrandResponse = UpdateBrandResponse(id = brandId, name = updateBrandRequest.name)

        every { brandDomainService.getBrand(brandId) } returns existingBrandDto
        every { brandDomainService.existsByName(updateBrandRequest.name) } returns false
        every { brandDomainService.save(any()) } returns updatedBrandDto

        `when`("브랜드 이름을 변경하면") {
            val response = sut.updateBrand(brandId, updateBrandRequest)
            then("정상적으로 업데이트되어야 한다.") {
                response shouldBe updateBrandResponse
            }
        }

        `when`("존재하지 않는 브랜드를 업데이트하려 하면") {
            val nonExistentBrandId = 999L
            every { brandDomainService.getBrand(nonExistentBrandId) } returns null
            then("BrandException이 발생해야 한다.") {
                val exception = shouldThrow<BrandException> {
                    sut.updateBrand(nonExistentBrandId, updateBrandRequest)
                }
                exception.message shouldBe "존재하지 않는 브랜드입니다."
            }
        }

        `when`("업데이트할 브랜드 이름이 중복될 때") {
            every { brandDomainService.existsByName(updateBrandRequest.name) } returns true
            then("BrandException이 발생해야 한다.") {
                val exception = shouldThrow<BrandException> {
                    sut.updateBrand(brandId, updateBrandRequest)
                }
                exception.message shouldBe "이미 존재하는 브랜드 이름입니다."
            }
        }
    }

    /*** 브랜드 삭제 테스트 ***/
    given("브랜드를 삭제할 때") {
        val brandId = 1L
        val existingBrandDto = BrandFixture.generate(id = brandId, name = "삭제할 브랜드")

        every { brandDomainService.getBrand(brandId) } returns existingBrandDto
        every { brandDomainService.deleteBrand(brandId) } returns brandId

        `when`("브랜드 삭제를 요청하면") {
            val response = sut.deleteBrand(brandId)
            then("정상적으로 삭제되어야 한다.") {
                response shouldBe DeleteProductResponse(productId = brandId)
            }
        }

        `when`("존재하지 않는 브랜드를 삭제할 때") {
            val nonExistentBrandId = 999L
            every { brandDomainService.getBrand(nonExistentBrandId) } returns null
            then("BrandException이 발생해야 한다.") {
                val exception = shouldThrow<BrandException> {
                    sut.deleteBrand(nonExistentBrandId)
                }
                exception.message shouldBe "존재하지 않는 브랜드입니다."
            }
        }
    }
})
