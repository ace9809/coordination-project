package com.example.musinsa.product

import com.example.musinsa.brand.domain.BrandDomainService
import com.example.musinsa.product.domain.ProductBrandStatisticDomainService
import com.example.musinsa.product.domain.ProductCategoryStatisticDomainService
import com.example.musinsa.product.domain.ProductDomainService
import com.example.musinsa.exception.*
import com.example.musinsa.product.model.dto.request.product.CreateProductRequest
import com.example.musinsa.product.model.dto.request.product.UpdateProductRequest
import com.example.musinsa.product.model.enums.CategoryType
import com.example.musinsa.product.model.enums.ProductEventType
import com.example.musinsa.product.model.dto.response.product.*
import com.example.musinsa.product.service.ProductService
import com.example.musinsa.testFixtures.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.springframework.context.ApplicationEventPublisher

class ProductServiceTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    val productDomainService = mockk<ProductDomainService>(relaxed = true)
    val productCategoryStatisticDomainService = mockk<ProductCategoryStatisticDomainService>(relaxed = true)
    val productBrandStatisticDomainService = mockk<ProductBrandStatisticDomainService>(relaxed = true)
    val brandDomainService = mockk<BrandDomainService>(relaxed = true)
    val applicationEventPublisher = mockk<ApplicationEventPublisher>(relaxed = true)

    val sut = spyk(
        ProductService(
            productDomainService,
            productCategoryStatisticDomainService,
            productBrandStatisticDomainService,
            brandDomainService,
            applicationEventPublisher
        ),
        recordPrivateCalls = true
    )

    given("카테고리별 최저 가격 조회 요청인 경우") {
        val brandDto = BrandFixture.generate(id = 1, name = "A")
        val topProductDto =
            ProductFixture.generate(id = 1, category = CategoryType.TOP, brandId = brandDto.id, price = 5000)
        val bagProductDto =
            ProductFixture.generate(id = 1, category = CategoryType.BAG, brandId = brandDto.id, price = 5000)
        val productCategoryStatistics = listOf(
            ProductCategoryStatisticFixture.generate(
                id = 1,
                category = CategoryType.TOP,
                minBrandId = brandDto.id,
                maxBrandId = brandDto.id,
                minPrice = topProductDto.price,
                maxPrice = topProductDto.price,
                minProductId = topProductDto.id,
                maxProductId = topProductDto.id
            ),
            ProductCategoryStatisticFixture.generate(
                id = 1,
                category = CategoryType.BAG,
                minBrandId = brandDto.id,
                maxBrandId = brandDto.id,
                minPrice = bagProductDto.price,
                maxPrice = bagProductDto.price,
                minProductId = bagProductDto.id,
                maxProductId = bagProductDto.id
            ),

            )
        val getCategoryMinPricesResponses = GetCategoryMinPricesResponses(
            products = productCategoryStatistics.map {
                GetCategoryMinPricesResponse(
                    brandId = it.minBrandId,
                    brandName = brandDto.name,
                    category = it.category.name,
                    categoryName = it.category.displayName,
                    price = it.minPrice
                )
            },
            totalPrice = productCategoryStatistics.sumOf { it.minPrice }
        )
        every { productCategoryStatisticDomainService.getAllProductStatistics() } returns productCategoryStatistics
        every { brandDomainService.getBrandIdIn(any()) } returns listOf(brandDto)

        `when`("카테고리별 최저 가격 목록을 요청하면") {
            val response = sut.getCategoryMinPrices()

            then("정상적으로 조회되어야 한다") {
                response shouldBe getCategoryMinPricesResponses
            }
        }
    }

    given("가장 저렴한 총 가격의 브랜드를 조회하는 경우") {
        val brandDto = BrandFixture.generate(id = 1, name = "A")
        val productBrandStatisticDto = ProductBrandStatisticFixture.generate(brandId = brandDto.id, totalPrice = 15000)
        val products = listOf(
            ProductFixture.generate(id = 1, brandId = brandDto.id, category = CategoryType.TOP, price = 5000),
            ProductFixture.generate(id = 2, brandId = brandDto.id, category = CategoryType.PANTS, price = 10000)
        )
        val totalPrice = products.sumOf { it.price }

        every { productBrandStatisticDomainService.getTopByOrderByTotalPriceAsc() } returns productBrandStatisticDto
        every { productDomainService.getAllByBrandId(brandDto.id) } returns products
        every { brandDomainService.getBrand(brandDto.id) } returns brandDto

        val getMinTotalPriceBrandResponses = GetMinTotalPriceBrandResponses(
            products = GetMinTotalPriceBrandResponse(
                brandName = brandDto.name,
                categories = products.map {
                    GetCategoriesResponse(
                        category = it.category.name,
                        categoryName = it.category.displayName,
                        price = it.price
                    )
                },
                totalPrice = totalPrice
            )
        )

        `when`("상품 브랜드의 최소 가격을 조회하면") {
            val response = sut.getMinTotalPriceBrand()

            then("해당 브랜드의 총 가격이 가장 저렴한 브랜드가 반환되어야 한다") {
                response shouldBe getMinTotalPriceBrandResponses
            }
        }

        `when`("상품 브랜드 통계를 찾을 수 없는 경우") {
            every { productBrandStatisticDomainService.getTopByOrderByTotalPriceAsc() } returns null

            then("예외가 발생해야 한다") {
                val exception = shouldThrow<ProductBrandStatisticException> {
                    sut.getMinTotalPriceBrand()
                }

                exception.message shouldBe "최저 가격에 판매하는 브랜드가 존재하지 않습니다."
            }
        }

        `when`("브랜드 정보를 찾을 수 없는 경우") {
            val productBrandStatisticDto = ProductBrandStatisticFixture.generate(brandId = 1, totalPrice = 15000)

            every { productBrandStatisticDomainService.getTopByOrderByTotalPriceAsc() } returns productBrandStatisticDto
            every { productDomainService.getAllByBrandId(productBrandStatisticDto.brandId) } returns emptyList()
            every { brandDomainService.getBrand(productBrandStatisticDto.brandId) } returns null

            then("예외가 발생해야 한다") {
                val exception = shouldThrow<BrandException> {
                    sut.getMinTotalPriceBrand()
                }

                exception.message shouldBe "존재하지 않는 브랜드입니다."
            }
        }
    }

    given("특정 카테고리의 최소/최대 가격 조회 요청인 경우") {
        val category = CategoryType.TOP
        val maxBrandDto = BrandFixture.generate(id = 1, name = "A")
        val minBrandDto = BrandFixture.generate(id = 2, name = "B")
        val maxTopProductDto =
            ProductFixture.generate(id = 1, category = category, brandId = maxBrandDto.id, price = 5000)
        val minTopProductDto =
            ProductFixture.generate(id = 2, category = category, brandId = minBrandDto.id, price = 15000)
        val productCategoryStatistic = ProductCategoryStatisticFixture.generate(
            id = 1,
            category = category,
            minBrandId = minBrandDto.id,
            maxBrandId = maxBrandDto.id,
            minPrice = minTopProductDto.price,
            maxPrice = maxTopProductDto.price,
            minProductId = minTopProductDto.id,
            maxProductId = maxTopProductDto.id
        )
        every { productCategoryStatisticDomainService.getProductStatistic(category) } returns productCategoryStatistic
        every { brandDomainService.getBrandIdIn(listOf(minBrandDto.id, maxBrandDto.id)) } returns listOf(maxBrandDto, minBrandDto)
        val getCategoryMinPricesResponses = GetMinMaxPriceResponses(
            category = productCategoryStatistic.category.name,
            categoryName = productCategoryStatistic.category.displayName,
            minProduct = GetMinMaxPriceResponse(
                brandName = minBrandDto.name,
                price = productCategoryStatistic.minPrice
            ),
            maxProduct = GetMinMaxPriceResponse(
                brandName = maxBrandDto.name,
                price = productCategoryStatistic.maxPrice
            )
        )

        `when`("카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회하는 경우") {
            val response = sut.getMinMaxPrice(category = category.name)

            then("정상적으로 조회되어야 한다") {
                response shouldBe getCategoryMinPricesResponses
            }
        }
    }

    given("유효하지 않은 카테고리 이름이 주어지면") {
        val invalidCategoryName = "INVALID_CATEGORY"

        `when`("잘못된 카테고리 이름으로 조회하면") {
            val exception = shouldThrow<ProductException> {
                sut.getMinMaxPrice(invalidCategoryName)
            }
            then("예외가 발생해야 한다.") {
                exception.message shouldBe "잘못된 카테고리입니다."
            }
        }
    }

    given("카테고리에 대한 통계가 존재하지 않으면") {
        val category = CategoryType.BAG
        every { productCategoryStatisticDomainService.getProductStatistic(category) } returns null

        `when`("카테고리 통계를 조회하면") {
            val exception = shouldThrow<ProductCategoryStatisticException> {
                sut.getMinMaxPrice(category = category.name)
            }
            then("ProductCategoryStatisticException이 발생해야 한다") {
                exception.message shouldBe "상품 최저가/최고가 정보가 존재하지 않습니다."
            }
        }
    }

    given("상품을 생성하는 경우") {
        val brandDto = BrandFixture.generate(id = 1, name = "A")
        val category = CategoryType.TOP
        val createProductRequest = CreateProductRequest(category = category.name, brandId = brandDto.id, price = 5000)
        val productDto = ProductFixture.generate(category = category, brandId = brandDto.id, price = 5000)
        val createdProductDto = productDto.copy(id = productDto.id)
        val productEventDto = ProductEventFixture.generate(productDto, null, ProductEventType.CREATE)

        every { productDomainService.existsByCategoryAndBrandId(category, createProductRequest.brandId) } returns false
        every { brandDomainService.getBrand(createProductRequest.brandId) } returns brandDto
        every { productDomainService.save(createdProductDto) } returns productDto
        every { applicationEventPublisher.publishEvent(productEventDto) } returns Unit

        `when`("생성 요청을 하면") {
            val response = sut.createProduct(createProductRequest)
            val createProductResponse = CreateProductResponse(
                id = productDto.id,
                brandId = brandDto.id,
                category = productDto.category,
                price = productDto.price
            )

            then("상품이 정상적으로 생성되어야 한다.") {
                response shouldBe createProductResponse
            }

            then("상품 생성 이벤트가 발송되어야 한다.") {
                verify { applicationEventPublisher.publishEvent(productEventDto) }
            }
        }

        `when`("가격이 음수인 경우") {
            val invalidPriceRequest = createProductRequest.copy(price = -1000)

            then("예외가 발생해야 한다") {
                val exception = shouldThrow<ProductException> {
                    sut.createProduct(invalidPriceRequest)
                }
                exception.message shouldBe "가격은 0이하로 설정할 수 없습니다."
            }
        }

        `when`("잘못된 카테고리 정보가 제공되면") {
            val invalidCategoryRequest = createProductRequest.copy(category = "INVALID_CATEGORY")

            then("예외가 발생해야 한다") {
                val exception = shouldThrow<ProductException> {
                    sut.createProduct(invalidCategoryRequest)
                }
                exception.message shouldBe "잘못된 카테고리입니다."
            }
        }

        `when`("같은 카테고리와 브랜드에 대한 상품이 존재하면") {
            every { productDomainService.existsByCategoryAndBrandId(category, createProductRequest.brandId) } returns true

            then("예외가 발생해야 한다") {
                val exception = shouldThrow<ProductException> {
                    sut.createProduct(createProductRequest)
                }
                exception.message shouldBe "해당 브랜드에 이미 존재하는 상품입니다."
            }
        }

        `when`("존재하지 않는 브랜드인 경우") {
            val notFountBrandRequest = createProductRequest.copy(brandId = 9999)
            every { brandDomainService.getBrand(notFountBrandRequest.brandId) } returns null

            then("예외가 발생해야 한다") {
                val exception = shouldThrow<BrandException> {
                    sut.createProduct(notFountBrandRequest)
                }

                exception.message shouldBe "존재하지 않는 브랜드입니다."
            }
        }
    }

    given("상품을 수정하는 경우") {
        val brandDto = BrandFixture.generate(id = 1, name = "A")
        val category = CategoryType.TOP
        val existingProductDto = ProductFixture.generate(id = 1, category = category, brandId = brandDto.id, price = 5000)
        val updateProductRequest = UpdateProductRequest(category = category.name, brandId = brandDto.id, price = 6000)
        val updatedProductDto = existingProductDto.copy(price = 6000)
        val productEventDto = ProductEventFixture.generate(updatedProductDto, existingProductDto, ProductEventType.UPDATE)

        every { productDomainService.getProduct(existingProductDto.id) } returns existingProductDto
        every { productDomainService.existsByCategoryAndBrandId(category, updateProductRequest.brandId) } returns false
        every { brandDomainService.getBrand(updateProductRequest.brandId) } returns brandDto
        every { productDomainService.save(updatedProductDto) } returns updatedProductDto
        every { applicationEventPublisher.publishEvent(productEventDto) } returns Unit

        `when`("수정 요청을 하면") {
            val response = sut.updateProduct(existingProductDto.id, updateProductRequest)
            val updateProductResponse = UpdateProductResponse(
                id = updatedProductDto.id,
                brandId = brandDto.id,
                category = updatedProductDto.category,
                price = updatedProductDto.price
            )

            then("상품이 정상적으로 수정되어야 한다.") {
                response shouldBe updateProductResponse
            }

            then("상품 수정 이벤트가 발송되어야 한다.") {
                verify { applicationEventPublisher.publishEvent(productEventDto) }
            }
        }

        `when`("가격이 음수인 경우") {
            val invalidPriceRequest = updateProductRequest.copy(price = -1000)

            then("예외가 발생해야 한다") {
                val exception = shouldThrow<ProductException> {
                    sut.updateProduct(existingProductDto.id, invalidPriceRequest)
                }
                exception.message shouldBe "가격은 0이하로 설정할 수 없습니다."
            }
        }

        `when`("같은 카테고리와 브랜드에 대한 상품이 존재하면") {
            every { productDomainService.existsByCategoryAndBrandId(category, updateProductRequest.brandId) } returns true

            then("예외가 발생해야 한다") {
                val exception = shouldThrow<ProductException> {
                    sut.updateProduct(existingProductDto.id, updateProductRequest)
                }
                exception.message shouldBe "해당 브랜드에 이미 존재하는 상품입니다."
            }
        }

        `when`("잘못된 카테고리 정보가 제공되면") {
            val invalidCategoryRequest = updateProductRequest.copy(category = "INVALID_CATEGORY")

            then("예외가 발생해야 한다") {
                val exception = shouldThrow<ProductException> {
                    sut.updateProduct(existingProductDto.id, invalidCategoryRequest)
                }
                exception.message shouldBe "잘못된 카테고리입니다."
            }
        }

        `when`("상품을 찾을 수 없으면") {
            val nonExistentProductId = 999L
            every { productDomainService.getProduct(nonExistentProductId) } returns null

            then("상품을 찾을 수 없다는 예외가 발생해야 한다") {
                val exception = shouldThrow<ProductException> {
                    sut.updateProduct(nonExistentProductId, updateProductRequest)
                }
                exception.message shouldBe "존재하지 않는 상품입니다."
            }
        }

        `when`("존재하지 않는 브랜드인 경우") {
            val notFoundBrandRequest = updateProductRequest.copy(brandId = 9999)
            every { brandDomainService.getBrand(notFoundBrandRequest.brandId) } returns null

            then("예외가 발생해야 한다") {
                val exception = shouldThrow<BrandException> {
                    sut.updateProduct(existingProductDto.id, notFoundBrandRequest)
                }

                exception.message shouldBe "존재하지 않는 브랜드입니다."
            }
        }
    }

    given("상품을 삭제하는 경우") {
        val productDto =
            ProductFixture.generate(id = 1, category = CategoryType.TOP, brandId = 1, price = 5000)
        every { productDomainService.getProduct(productDto.id) } returns productDto
        every { productDomainService.deleteProduct(productDto.id) } returns productDto.id
        val productEventDto = ProductEventFixture.generate(productDto, null, ProductEventType.DELETE)
        every { applicationEventPublisher.publishEvent(productEventDto) } returns Unit
        `when`("삭제 요청을 하면") {
            val response = sut.deleteProduct(productDto.id)
            val deleteProductResponse = DeleteProductResponse(
                productId = productDto.id
            )

            then("상품이 정상적으로 삭제되어야 한다.") {
                response shouldBe deleteProductResponse
            }

            then("상품 삭제 이벤트가 발송되어야 한다.") {
                verify { applicationEventPublisher.publishEvent(productEventDto) }
            }
        }
    }
})
